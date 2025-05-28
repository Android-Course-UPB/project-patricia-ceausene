package com.example.ecotracker.model

data class CarbonFootprint(
    val averageRomania: Double = 0.0,
    val transportCarbonFootprint: Double = 0.0,
    val travelCarbonFootprint: Double = 0.0,
    val homeCarbonFootprint: Double = 0.0,
    val totalCarbonFootprint: Double = 0.0,
    val percentageTransport: Double = 0.0,
    val percentageTravel: Double = 0.0,
    val percentageHome: Double = 0.0,
    val trees: Double = 0.0
)

data class EntryCarbonFootprint(
    val carbonFootprint: CarbonFootprint = CarbonFootprint(),
    val date: String = ""
)