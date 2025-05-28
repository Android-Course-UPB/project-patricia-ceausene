package com.example.ecotracker.model

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val points: Int = 0,
    val weight: Float = 0f,
    val actions: List<UserAction> = listOf(),
    val carbonFootprints: List<EntryCarbonFootprint> = listOf(),
    val hasProfilePicture: Boolean = false
)

data class UserAction(
    val id: String = "",
    val counter: Int = 0,
    val habit: Boolean = false,
    val frequency: String = ""
)

data class UserLeaderboard(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val points: Int = 0,
    val weight: Float = 0f,
    val imageUri: String = "",
    val hasProfilePicture: Boolean = false
)