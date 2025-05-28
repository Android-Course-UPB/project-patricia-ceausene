package com.example.ecotracker.data.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.model.UserLeaderboard
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LeaderboardViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _users = MutableStateFlow<List<UserLeaderboard>>(emptyList())
    val users: StateFlow<List<UserLeaderboard>> = _users

    companion object {
        private const val TAG = "Leaderboard ViewModel"
    }


    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Switch the coroutine context to Dispatchers.IO for the I/O operation
                _users.value = withContext(Dispatchers.IO) {
                    getUsers()
                }
            } catch (exception: Exception) {
                Log.d(TAG, "Error getting users: ", exception)
                _isLoading.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getUsers(): List<UserLeaderboard> {
        val result = db.collection("users")
            .get()
            .await()
        return result.documents.mapNotNull { document ->
            val user = document.toObject(UserLeaderboard::class.java)?.copy(id = document.id)
            if (user?.hasProfilePicture == true) {
                val profilesRef = storage.reference.child("profiles/${user.id}")
                try {
                    val uri = profilesRef.downloadUrl.await()
                    user.copy(imageUri = uri.toString())
                } catch (e: Exception) {
                    user.copy(imageUri = "")
                }
            } else {
                user?.copy(imageUri = "")
            }
        }
    }
}