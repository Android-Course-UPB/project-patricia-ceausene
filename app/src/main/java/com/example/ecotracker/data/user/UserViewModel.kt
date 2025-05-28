package com.example.ecotracker.data.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _imageUri = MutableStateFlow("")
    val imageUri: StateFlow<String> = _imageUri

    private val _showUpdatedProfileImage = MutableStateFlow(false)
    val showUpdatedProfileImage: StateFlow<Boolean> = _showUpdatedProfileImage

    companion object {
        private const val TAG = "UserViewModel"
    }

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        val user = auth.currentUser
        _isLoading.value = true
        if (user != null) {
            fetchUser(user.uid)
        } else {
            _currentUser.value = null  // No user logged in
        }
        _isLoading.value = false
    }

    private fun fetchUser(userId: String) {
        viewModelScope.launch {
            try {
                val document = withContext(Dispatchers.IO) {
                    db.collection("users").document(userId).get().await()
                }
                _currentUser.value = document.toObject(User::class.java)
                if (_currentUser.value?.hasProfilePicture == true) {
                    fetchProfileImage(userId)
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    private fun fetchProfileImage(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val profilesRef = storage.reference.child("profiles/$userId")

            try {
                val uri = profilesRef.downloadUrl.await()
                _imageUri.value = uri.toString()
            } catch (e: Exception) {
                // Handle any errors here
                _isLoading.value = false
            }
        }
    }

    fun updateUserProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            val userUid = auth.uid ?: return@launch
            val profilesRef = storage.reference.child("profiles/$userUid")

            try {
                withContext(Dispatchers.IO) {
                    // Upload the file to Firebase Storage
                    profilesRef.putFile(imageUri).await()

                    val userToUpdate = _currentUser.value!!.copy(hasProfilePicture = true)
                    //Update user
                    db.collection("users").document(auth.currentUser!!.uid)
                        .set(userToUpdate)
                        .await()

                    withContext(Dispatchers.Main) {
                        _imageUri.value = imageUri.toString()
                        _showUpdatedProfileImage.value = true
                    }
                }
            } catch (e: Exception) {
                Log.d("UserViewModel update profile image", e.message.toString())
            }
        }
    }

    fun resetShowUpdatedProfileImage() {
        _showUpdatedProfileImage.value = false
    }

    fun logout() {
        try {
            FirebaseAuth.getInstance().signOut()
            _currentUser.value = null
            Log.d(TAG, "User logged out, navigating to login screen")
        } catch (e: Exception) {
            Log.e(TAG, "Error during logout: ${e.message}")
        }
    }
}