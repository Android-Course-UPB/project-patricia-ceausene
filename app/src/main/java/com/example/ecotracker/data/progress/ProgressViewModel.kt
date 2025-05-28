package com.example.ecotracker.data.progress

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.model.Action
import com.example.ecotracker.model.ProgressAction
import com.example.ecotracker.model.User
import com.example.ecotracker.model.UserAction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProgressViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var userActions = mutableStateOf<List<UserAction>>(listOf())

    private val _actions = MutableStateFlow<List<ProgressAction>>(listOf())
    val actions: StateFlow<List<ProgressAction>> = _actions

    private val _habits = MutableStateFlow<List<ProgressAction>>(listOf())
    val habits: StateFlow<List<ProgressAction>> = _habits


    init {
        loadData()
    }


    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = db.collection("users").document(auth.currentUser!!.uid).get().await()
                .toObject(User::class.java)
            userActions.value = user?.actions ?: listOf()

            fetchActions()
        }
    }


    private suspend fun fetchActions() = coroutineScope {
        val actionDetails = userActions.value.map { userAction ->
//            Log.d("User Action", userAction.toString())
            async {
                val actionDocument = db.collection("actions").document(userAction.id).get().await()
                val action = actionDocument.toObject(Action::class.java)
//                Log.d("Action", action.toString())
                if (action != null) {
                    ProgressAction(
                        id = userAction.id,
                        title = action.title,
                        weight = action.weight,
                        points = action.points,
                        counter = userAction.counter,
                        frequency = userAction.frequency
                    )
                } else {
                   null
                }
            }
        }.awaitAll().filterNotNull()

        _actions.value = actionDetails.filter { it.frequency.isEmpty() }
        _habits.value = actionDetails.filter { it.frequency.isNotEmpty() }
    }
}