package com.example.ecotracker.data.survey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.model.CarbonFootprint
import com.example.ecotracker.model.EntryCarbonFootprint
import com.example.ecotracker.model.SurveyResponse
import com.example.ecotracker.model.User
import com.example.ecotracker.model.formatCityName
import com.example.ecotracker.model.formatRoomString
import com.example.ecotracker.model.questionsList
import com.example.ecotracker.model.toJsonString
import com.example.ecotracker.model.transformNumber
import com.example.ecotracker.model.translateCarType
import com.example.ecotracker.model.translateEnergySource
import com.example.ecotracker.model.translateFloorDescription
import com.example.ecotracker.model.translateFloorHeight
import com.example.ecotracker.model.translateInsulationLevel
import com.example.ecotracker.model.translateTemperature
import com.example.ecotracker.model.translateTimePeriod
import com.example.ecotracker.model.translateYesNo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SurveyViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableStateFlow<User?>(null)

    val surveyQuestions = questionsList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showResult = MutableStateFlow(false)
    val showResult: StateFlow<Boolean> = _showResult

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _responses = MutableStateFlow(SurveyResponse())

    private val _carbonFootprint = MutableStateFlow<CarbonFootprint?>(null)

    fun onNext(response: String) {
        when (_currentQuestionIndex.value) {
            0 -> {
                _responses.value.city = formatCityName(response)
            }
            1 -> {
                when (response) {
                    "Apartment" -> {
                        _responses.value.apartmentType = "apartament"
                        _currentQuestionIndex.value = 5 // Skip to room count question
                        return
                    }
                    "Studio" -> {
                        _responses.value.apartmentType = "garsoniera"
                        _currentQuestionIndex.value = 6
                        return
                    }
                    else -> {
                        _responses.value.apartmentType = "casa"
                        _responses.value.apartmentRoomsNumber = "4+camere"
                    }
                }
            }
            2 -> {
                _responses.value.periodOfTime = translateTimePeriod(response)
            }
            3 -> {
                _responses.value.insulation = translateInsulationLevel(response)
            }
            4 -> {
                _responses.value.floorsNumber = translateFloorDescription(response)
            }
            5 -> {
                _responses.value.apartmentRoomsNumber = formatRoomString(response)
            }
            6 -> {
                _responses.value.isRehabilitated = translateYesNo(response)
            }
            7 -> {
                _responses.value.heightFloor = translateFloorHeight(response)
            }
            8 -> {
                _responses.value.surface = response
            }
            9 -> {
                _responses.value.numberRoomates = transformNumber(response)
            }
            10 -> {
                _responses.value.heatingSource = translateEnergySource(response)
                if (response != "Natural gas") {
                    _currentQuestionIndex.value = 14 // Skip to average temp question
                    _responses.value.hasHeatingSystem = "nu"
                    _responses.value.isHeatingSystemOld = "da"
                    _responses.value.hasCondensation = "nu"
                    return
                }
            }
            11 -> {
                _responses.value.hasHeatingSystem = translateYesNo(response)
                if (response == "No") {
                    _currentQuestionIndex.value = 14 // Skip to average temp question
                    _responses.value.isHeatingSystemOld = "da"
                    _responses.value.hasCondensation = "nu"
                    return
                }
            }
            12 -> {
                _responses.value.isHeatingSystemOld = translateYesNo(response)
            }
            13 -> {
                _responses.value.hasCondensation = translateYesNo(response)
            }
            14 -> {
                _responses.value.temperature = translateTemperature(response)
            }
            15 -> {
                _responses.value.electricityConsumption = transformNumber(response)
            }
            16 -> {
                _responses.value.photovoltaicPanels = translateYesNo(response)
            }
            17 -> {
                _responses.value.carType = translateCarType(response)
                if (response == "I don't own") {
                    _responses.value.kmPerWeek = "100"
                    _currentQuestionIndex.value = 19 // Skip to public transportation question
                    return
                }
            }
            18 -> {
                _responses.value.kmPerWeek = transformNumber(response)
            }
            19 -> {
                _responses.value.bus = response
            }
            20 -> {
                _responses.value.minibus = response
            }
            21 -> {
                _responses.value.subway = response
            }
            22 -> {
                _responses.value.tram = response
            }
            23 -> {
                _responses.value.shortFlights = transformNumber(response)
            }
            24 -> {
                _responses.value.longFlights = transformNumber(response)
                makePostRequest()
                return
            }

        }
        _currentQuestionIndex.value++
    }

    private fun makePostRequest() {
        val client = OkHttpClient()
        _isLoading.value = true
        viewModelScope.launch {
            val formBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "carbon_footprint")
                .addFormDataPart("data",_responses.value.toJsonString())
                .build()

            val request = Request.Builder()
                .url("https://www.engie.ro/wp-admin/admin-ajax.php")
                .post(formBody)
                .build()

            try {
                val response: String = withContext(Dispatchers.IO) {
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        response.body?.string() ?: "No response"
                    }
                }

                _carbonFootprint.value = parseSurveyResponse(response)

                val userUid = auth.uid ?: return@launch
                val document = withContext(Dispatchers.IO) {
                    db.collection("users").document(userUid).get().await()
                }
                _user.value = document.toObject(User::class.java)
                val currentUser = _user.value ?: throw IllegalStateException("User not found")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())
                val newEntry = EntryCarbonFootprint(_carbonFootprint.value!!, currentDate)
                val updatedCarbonFootprintList = _user.value!!.carbonFootprints + newEntry
                val userToUpdate = currentUser.copy(carbonFootprints = updatedCarbonFootprintList)

                // Update Firestore document
                db.collection("users").document(auth.currentUser!!.uid)
                    .set(userToUpdate)
                    .await()

               withContext(Dispatchers.Main) {
                   _isLoading.value = false
                   _showResult.value = true
               }


            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d("SurveyViewModel", "Request failed: ${e.message}")
                    _isLoading.value = false
                }
            }

        }
    }

}

fun parseSurveyResponse(jsonString: String): CarbonFootprint {
    val jsonObject = JSONObject(jsonString).getJSONObject("body")

    return CarbonFootprint(
        averageRomania = jsonObject.getDouble("medie_emisii_romania"),
        transportCarbonFootprint = jsonObject.getDouble("rezultate_emisii_transport"),
        travelCarbonFootprint = jsonObject.getDouble("rezultate_emisii_zbor"),
        homeCarbonFootprint =jsonObject.getDouble("rezultate_emisii_casa"),
        totalCarbonFootprint = jsonObject.getDouble("rezultate_emisii_totale"),
        percentageHome =  jsonObject.getDouble("procent_casa"),
        percentageTransport = jsonObject.getDouble("procent_transport"),
        percentageTravel = jsonObject.getDouble("procent_zbor"),
        trees = jsonObject.getDouble("rezultate_amortizare_copaci")
    )
}