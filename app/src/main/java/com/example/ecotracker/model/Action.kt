package com.example.ecotracker.model
data class Action(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val weight: String = "",
    val points: Int = 0,
    val stayHealthy: Boolean = false,
    val saveMoney: Boolean = false,
    val travel: Boolean = false,
    val food: Boolean = false,
    val sdgs: List<Int> = listOf()
)

data class ProgressAction(
    val id: String = "",
    val title: String = "",
    val weight: String = "",
    val points: Int = 0,
    val counter: Int = 0,
    val frequency: String = ""
)