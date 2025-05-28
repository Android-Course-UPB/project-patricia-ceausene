package com.example.ecotracker.data.actions

import android.util.Log
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.model.Action
import com.example.ecotracker.model.User
import com.example.ecotracker.model.UserAction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UpdateHabitViewModel(private val actionId: String): ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    private val _userAction = MutableStateFlow<UserAction?>(null)

    private val _action = MutableStateFlow<Action?>(null)
    val action: StateFlow<Action?> = _action

    private val _weight = MutableStateFlow<String>("")
    val weight: StateFlow<String> = _weight

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showUpdateHabitDialog = MutableStateFlow(false)
    val showUpdateHabit: StateFlow<Boolean> = _showUpdateHabitDialog

    private val _showStopHabitDialog = MutableStateFlow(false)
    val showStopHabit: StateFlow<Boolean> = _showStopHabitDialog

    init {
        fetchHabit()
    }

    private fun fetchHabit() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Start both fetches concurrently
                val fetchAction = async(Dispatchers.IO) {
                    db.collection("actions").document(actionId).get().await()
                }
                val fetchUser = async(Dispatchers.IO) {
                    db.collection("users").document(auth.currentUser!!.uid).get().await()
                }

                // Await both results
                val results = awaitAll(fetchAction, fetchUser)

                // Handle action document
                val actionDocument = results[0]
                _action.value = actionDocument.toObject(Action::class.java)!!

                // Handle user document
                val userDocument = results[1]
                _user.value = userDocument.toObject(User::class.java)!!
                _userAction.value = _user.value?.actions?.find { it.id == actionId }

            } catch (e: Exception) {
                // Log the exception or handle the error

            } finally {
                _weight.value = calculatedSavedCo2(_userAction.value!!.frequency, _action.value!!.weight)
                _isLoading.value = false
            }
        }
    }

    fun updateFrequency(frequency: String) {
        viewModelScope.launch(Dispatchers.IO) {  // Switch to IO dispatcher for network/database operations
            try {
                val currentUser = _user.value ?: throw IllegalStateException("User not found")

                val updatedActions = currentUser.actions.map { userAction ->
                    if (userAction.id == actionId) {
                        // Update existing action
                        userAction.copy(frequency = frequency)
                    } else {
                        userAction
                    }
                }.toMutableList()

                // Prepare the updated user object
                val userToUpdate = currentUser.copy(actions = updatedActions)

                // Update Firestore document
                db.collection("users").document(auth.currentUser!!.uid)
                    .set(userToUpdate)
                    .await()

            } catch (e: Exception) {
                Log.d("Update user action", e.message.toString())
            } finally {
                withContext(Dispatchers.Main) {
                    _showUpdateHabitDialog.value = true
                }
            }
        }
    }

    fun stopHabit() {
        viewModelScope.launch(Dispatchers.IO) {  // Switch to IO dispatcher for network/database operations
            try {
                val currentUser = _user.value ?: throw IllegalStateException("User not found")

                val updatedActions = currentUser.actions.map { userAction ->
                    if (userAction.id == actionId) {
                        // Update existing action
                        userAction.copy(frequency = "", habit = false)
                    } else {
                        userAction
                    }
                }.toMutableList()

                // Prepare the updated user object
                val userToUpdate = currentUser.copy(actions = updatedActions)

                // Update Firestore document
                db.collection("users").document(auth.currentUser!!.uid)
                    .set(userToUpdate)
                    .await()

            } catch (e: Exception) {
                Log.d("Update user action", e.message.toString())
            } finally {
                withContext(Dispatchers.Main) {
                    _showStopHabitDialog.value = true
                }
            }
        }
    }

    fun resetUpdateHabitDialog() {
        _showUpdateHabitDialog.value= false
    }

    fun resetStopHabitDialog() {
        _showStopHabitDialog.value = false
    }

    fun calculatedSavedCo2(frequency: String, weight: String): String {
        val numberRegex = "\\d+".toRegex()
        val wordRegex = "[a-zA-Z]+".toRegex()

        val number = numberRegex.find(weight)?.value?.toInt() ?: 1
        val unit = wordRegex.find(weight)?.value
        var freq = 1
        when(frequency) {
            "Once a month" -> freq = 12
            "Twice a month" -> freq = 24
            "Three times a month"-> freq = 36
            "Once a week" -> freq = 48
            "Twice a week" -> freq = 96
            "Three times a week"-> freq = 144
            "Four times a week" -> freq = 192
            "Five times a week" -> freq = 240
            "Six times a week" -> freq = 288
            "Everyday" -> freq = 336
        }

        val totalWeight = freq * number
        return totalWeight.toString() + unit
    }

}

class UpdateHabitViewModelFactory(private val actionId: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateHabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return actionId?.let { UpdateHabitViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}