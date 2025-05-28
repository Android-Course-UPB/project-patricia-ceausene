package com.example.ecotracker.data.survey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.model.EntryCarbonFootprint
import com.example.ecotracker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CalculatorViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentUser = MutableStateFlow<User?>(null)

    private val _carbonFootprint = MutableStateFlow<EntryCarbonFootprint?>(null)
    val carbonFootprint: StateFlow<EntryCarbonFootprint?> = _carbonFootprint

    companion object {
        private const val TAG = "CalculatorViewModel"
    }

    init {
        fetchLastCarbonFootprint()
    }

    private fun fetchLastCarbonFootprint() {
        val user = auth.currentUser
        if (user != null) {
            fetchUser()
        } else {
            _currentUser.value = null  // No user logged in
            Log.d(TAG, "No user")
        }
    }

    private fun fetchUser() {
        Log.d(TAG, "Fetch user")
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val document = withContext(Dispatchers.IO) {
                    db.collection("users").document(auth.currentUser!!.uid).get().await()
                }
                withContext(Dispatchers.Main) {
                    _currentUser.value = document.toObject(User::class.java)
                    _carbonFootprint.value = _currentUser.value?.carbonFootprints?.lastOrNull()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
                // Optionally log or handle the exception as needed
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    Log.d(TAG, _currentUser.value.toString())
                    Log.d(TAG, _carbonFootprint.value.toString())
                }
            }
        }
    }
}