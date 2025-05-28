package com.example.ecotracker.data.actions

import android.util.Log
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

class ActionDetailsViewModel(private val actionId: String) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    private val _somethingToBeSubmitted =  MutableStateFlow(false)

    private val _action = MutableStateFlow<Action?>(null)
    val action: StateFlow<Action?> = _action

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _presses = MutableStateFlow(0)
    val presses: StateFlow<Int> = _presses

    private val _showHabitDialog = MutableStateFlow(false)
    val showHabitDialog: StateFlow<Boolean> = _showHabitDialog

    private val _showDoneActionAlertDialog = MutableStateFlow(false)
    val showDoneActionAlertDialog: StateFlow<Boolean> = _showDoneActionAlertDialog

    private val _showCommitHabitAlertDialog = MutableStateFlow(false)
    val showCommitHabitAlertDialog: StateFlow<Boolean> = _showCommitHabitAlertDialog

    private val _isHabit = MutableStateFlow(false)
    val isHabit: StateFlow<Boolean> = _isHabit

    private val _frequency = MutableStateFlow("")
    private val _points = MutableStateFlow(0)
    private val _weight = MutableStateFlow(0f)


    init {
        fetchActionItem()

    }

    private fun fetchActionItem() {
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
                val userAction = _user.value?.actions?.find { it.id == actionId }
                if (userAction != null) {
                    _presses.value = userAction.counter
                    _frequency.value = userAction.frequency
                    _isHabit.value = userAction.habit

                }

            } catch (e: Exception) {
                // Log the exception or handle the error

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun incrementPresses() {
        val currentPresses = _presses.value
        _presses.value = currentPresses + 1
        _points.value += _action.value!!.points
        _weight.value = addWeight(_weight.value, _action.value!!.weight)

        _showDoneActionAlertDialog.value = true
        _somethingToBeSubmitted.value = true
    }

    fun turnIntoHabit() {
        _showHabitDialog.value = true
    }

    fun submitChanges() {
        if(_somethingToBeSubmitted.value) {
            viewModelScope.launch(Dispatchers.IO) {  // Switch to IO dispatcher for network/database operations
                _isLoading.value = true
                try {
                    val currentUser = _user.value ?: throw IllegalStateException("User not found")

                    // Determine if the action exists or needs to be added
                    var actionFound = false
                    val updatedActions = currentUser.actions.map { userAction ->
                        if (userAction.id == actionId) {
                            actionFound = true
                            // Update existing action
                            userAction.copy(
                                counter = _presses.value,
                                habit = _isHabit.value,
                                frequency = _frequency.value
                            )
                        } else {
                            userAction
                        }
                    }.toMutableList()

                    // If the action wasn't found, add it as new
                    if (!actionFound) {
                        updatedActions.add(
                            UserAction(
                                id = actionId,
                                counter = _presses.value,
                                habit = _isHabit.value,
                                frequency = _frequency.value
                            )
                        )
                    }
                    // Prepare the updated user object
                    val updatedPoints = _points.value + currentUser.points
                    val updatedWeight = _weight.value + currentUser.weight
                    val userToUpdate = currentUser.copy(
                        actions = updatedActions,
                        points = updatedPoints,
                        weight = updatedWeight
                    )
                    // Update Firestore document
                    db.collection("users").document(auth.currentUser!!.uid)
                        .set(userToUpdate)
                        .await()

                } catch (e: Exception) {
                    Log.d("Update user action", e.message.toString())
                } finally {
                    withContext(Dispatchers.Main) {
                        _isLoading.value = false
                        _somethingToBeSubmitted.value = false
                    }
                }
            }
        }
    }

    fun commitToHabit(frequency: String) {
        _isHabit.value = true
        _showCommitHabitAlertDialog.value = true
        _frequency.value = frequency
    }

    fun resetHabitDialog() {
        _showCommitHabitAlertDialog.value = false
    }
    fun resetActionDialog() {
        _showDoneActionAlertDialog.value = false
    }

    private fun addWeight(totalWeightKg: Float, weight: String): Float {
        val weightRegex = Regex("([0-9]*\\.?[0-9]+)(kg|g)")
        val matchResult = weightRegex.matchEntire(weight)
            ?: throw IllegalArgumentException("Invalid weight format")

        val (value, unit) = matchResult.destructured
        val weightValue = value.toFloat()

        return when (unit) {
            "kg" -> totalWeightKg + weightValue
            "g" -> totalWeightKg + weightValue / 1000
            else -> throw IllegalArgumentException("Unknown weight unit")
        }
    }
}


class ActionDetailViewModelFactory(private val actionId: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActionDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return actionId?.let { ActionDetailsViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}