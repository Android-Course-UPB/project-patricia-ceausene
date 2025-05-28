package com.example.ecotracker.data.actions

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.model.Action
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ActionsViewModel : ViewModel() {
    val response: MutableState<ActionState> = mutableStateOf(ActionState.Empty)

    init {
        loadActions()
    }

    private fun loadActions() {
        viewModelScope.launch {
            response.value = ActionState.Loading
            try {
                // Switch the coroutine context to Dispatchers.IO for the I/O operation
                val actions = withContext(Dispatchers.IO) {
                    getActions()
                }
                response.value = ActionState.Success(actions)
            } catch (exception: Exception) {
                Log.d(TAG, "Error getting documents: ", exception)
                response.value = ActionState.Error(exception)
            }
        }
    }

    // Don't forget to define TAG for logging
    companion object {
        private const val TAG = "YourViewModel"
    }

   private suspend fun getActions(): List<Action> {
        val db = Firebase.firestore
        val result = db.collection("actions")
            .get()
            .await()
        return result.documents.mapNotNull { document ->
            val actionItem = document.toObject(Action::class.java)
            actionItem?.copy(id = document.id)
        }
}

    sealed class ActionState {
        object Loading : ActionState()
        object Empty : ActionState()
        data class Success(val actions: List<Action>) : ActionState()
        data class Error(val exception: Throwable) : ActionState()
    }
}