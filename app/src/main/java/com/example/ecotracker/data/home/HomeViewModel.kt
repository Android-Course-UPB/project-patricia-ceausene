package com.example.ecotracker.data.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale


data class WeeklyTip(
    val title: String = "",
    val introduction: String = "",
    val tip1: String = "",
    val tip2: String = "",
    val ending: String = "",
    val week: Number = 0
)

class HomeViewModel: ViewModel() {

    private val _weeklyTip = MutableStateFlow<WeeklyTip?>(null)
    val weeklyTip = _weeklyTip.asStateFlow()
    init {
        fetchWeeklyTipForCurrentWeek()
    }

    private fun fetchWeeklyTipForCurrentWeek() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weekNumber = getCurrentWeekNumber()
//                val weekNumber = 27

                val db = Firebase.firestore
                val snapshot = db.collection("weeklyTips")
                    .whereEqualTo("week", weekNumber)
                    .limit(1)  // Assuming there's only one tip per week
                    .get()
                    .await()

                if (snapshot.documents.isNotEmpty()) {
                    val document = snapshot.documents.first()
                    val title = document.getString("title") ?: ""
                    val introduction = document.getString("introduction") ?: ""
                    val tip1 = document.getString("tip1") ?: ""
                    val tip2 = document.getString("tip2") ?: ""
                    val ending = document.getString("ending") ?: ""
                    _weeklyTip.value = WeeklyTip(title, introduction, tip1, tip2, ending, weekNumber)
                }
            } catch (e: Exception) {
                _weeklyTip.value = WeeklyTip("Error", "Failed to fetch tip")
            }
        }
    }

    fun getCurrentWeekNumber(date: LocalDate = LocalDate.now()): Int {
        return date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
    }
}