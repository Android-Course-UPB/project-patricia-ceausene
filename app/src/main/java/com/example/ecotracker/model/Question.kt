package com.example.ecotracker.model

import org.json.JSONObject

data class SurveyQuestion(
    val questionText: String,
    val options: List<String>,
    val type: QuestionType,
    val info: String? = null
)

enum class QuestionType {
    DROPDOWN, RADIO, SLIDER, TEXT
}

data class SurveyResponse(
    var city: String? = null,
    var apartmentType: String? = null,
    var apartmentRoomsNumber: String? = null,
    var periodOfTime: String? = null,
    var floorsNumber: String? = null,
    var insulation: String? = null,
    var isRehabilitated: String? = null,
    var surface: String? = null,
    var numberRoomates: String? = null,
    var heatingSource: String? = null,
    var hasHeatingSystem: String? = null,
    var isHeatingSystemOld: String? = null,
    var hasCondensation: String? = null,
    var temperature: String? = null,
    var photovoltaicPanels: String? = null,
    var carType: String? = null,
    var kmPerWeek: String? = null,
    var heightFloor: String? = null,
    var bus: String? = null,
    var minibus: String? = null,
    var subway: String? = null,
    var tram: String? = null,
    var shortFlights: String? = null,
    var longFlights: String? = null,
    var electricityConsumption: String? = null
)

fun SurveyResponse.toJsonString(): String {
    val jsonObject = JSONObject()

    this.city?.let { jsonObject.put("oras", it) }
    this.apartmentType?.let { jsonObject.put("locuinta", it) }
    this.apartmentRoomsNumber?.let { jsonObject.put("apartament", it) }
    this.isRehabilitated?.let { jsonObject.put("reabilitare", it) }
    this.surface?.let { jsonObject.put("suprafata", it) }
    this.numberRoomates?.let { jsonObject.put("persoane", it) }
    this.heightFloor?.let { jsonObject.put("inaltime-etaj", it) }
    this.floorsNumber?.let { jsonObject.put("etaje-casa", it) }
    this.insulation?.let { jsonObject.put("izolatie", it) }
    this.periodOfTime?.let { jsonObject.put("perioada-construirii", it) }
    this.heatingSource?.let { jsonObject.put("sursa-incalzire", it) }
    this.hasHeatingSystem?.let { jsonObject.put("centrala-termica", it) }
    this.isHeatingSystemOld?.let { jsonObject.put("vechime-centrala", it) }
    this.hasCondensation?.let { jsonObject.put("centrala-condensare", it) }
    this.temperature?.let { jsonObject.put("temperatura", it) }
    this.electricityConsumption?.let { jsonObject.put("consum-energie-electrica", it) }
    this.photovoltaicPanels?.let { jsonObject.put("panouri-fotovoltaice", it) }
    this.carType?.let { jsonObject.put("tip-autoturism", it) }
    this.kmPerWeek?.let { jsonObject.put("km-saptamanal", it) }
    this.bus?.let { jsonObject.put("transport-autobuz", it) }
    this.minibus?.let { jsonObject.put("transport-microbuz", it) }
    this.subway?.let { jsonObject.put("transport-metrou", it) }
    this.tram?.let { jsonObject.put("transport-tramvai", it) }
    this.shortFlights?.let { jsonObject.put("zboruri-scurte", it) }
    this.longFlights?.let { jsonObject.put("zboruri-lungi", it) }

    return jsonObject.toString()
}


val citiesInRomania = listOf(
    "Alba Iulia", "Alexandria", "Arad", "Bacau", "Baia Mare", "Barlad",
    "Bistrița", "Botoșani", "Brașov", "Braila", "București", "Buzau",
    "Calafat", "Caracal", "Caransebes", "Calarasi", "Campina", "Campulung-Moldovenesc", "Campulung-Muscel", "Cluj-Napoca", "Constanța", "Craiova","Curtea de Arges", "Deva",
    "Dorohoi", "Faragas", "Focșani", "Galați", "Giurgiu", "Iași", "Lugoj", "Mangalia",
    "Medgidia", "Miercurea Ciuc", "Mioveni", "Moreni", "Negrești-Oaș", "Oltenița",
    "Onești", "Oradea", "Oradea", "Paltinis-Sibiu", "Petrosani", "Piatra-Neamț", "Pitești", "Ploiești", "Predeal",
    "Râmnicu-Sarat", "Râmnicu Vâlcea", "Rădăuți", "Reșița", "Roman", "Satu Mare", "Sf. Gheorghe Delta", "Sibiu", "Sighisoara",
    "Slatina", "Slobozia", "Suceava", "Sulina", "Targoviste", "Targu Jiu", "Targu Mureș", "Targu Ocna", "Timisoara",
    "Tulcea", "Turda", "Turnu Magurele", "Turnu Severin", "Urziceni", "Vaslui", "Vatra Dornei", "Zalau"
)

val questionsList = listOf(
    SurveyQuestion(
        questionText = "Where do you live?",
        options = citiesInRomania,
        type = QuestionType.DROPDOWN,
        info = "If you don't find your city in the list, choose the closest city to you."
    ),
    SurveyQuestion(
        questionText = "Select the type of housing",
        options = listOf("House", "Studio", "Apartment"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "In what period was the house built?",
        options = listOf("Before 1990", "Between 1990 - 2020", "After 2020"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "What is the insulation level of the building?",
        options = listOf("Uninsulated house: No envelope elements (walls, floors under the attic, terrace) are thermally insulated",
            "Insulated house - low level: Envelope elements are partially insulated and/or the thermal insulation was done before 2010",
            "Insulated house - standard: The building envelope is fully insulated, and thermal insulation was done between 2010 and 2020",
            "Well-insulated house:  The building envelope is fully insulated, and the thermal insulation was done after 2020"
        ),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "How many levels does the house have?",
        options = listOf("1 (Ground floor)", "2 (Ground floor + 1 floor)", "3 (Ground floor + 2 floors)", "4 (Ground floor + 3 floors)"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "How many rooms does the apartment have?",
        options = listOf("2 rooms", "3 rooms", "4 rooms", "5 rooms"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "Has the building been thermally rehabilitated?",
        options = listOf("Yes", "No"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "What is approximate height from floor to ceiling?",
        options = listOf("Below 2.20 m", "2.20 m","2.40 m","2.60 m", "2.80 m","3 m","Above 3 m"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "What is the usable area of the dwelling in square meters?",
        options = listOf("m2"),
        type = QuestionType.TEXT
    ),
    SurveyQuestion(
        questionText = "How many people live in the house?",
        options = listOf(""),
        type = QuestionType.TEXT
    ),
    SurveyQuestion(
        questionText = "What is the main source of heating for your home?",
        options = listOf("Natural gas", "Electricity","Biomass - firewood","Thermal energy from solar collectors","District heating (centralized system) / cogeneration"
            ,"Electricity from photovoltaic panels","Biomass - briquettes/pellets","Thermal energy from heat pumps"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "Do you have your own heating system?",
        options = listOf("Yes", "No"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "Is your heating system older than 7 years?",
        options = listOf("Yes", "No"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "Is it a condensing boiler?",
        options = listOf("Yes", "No"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "What is the average temperature in your house??",
        options = listOf("Below 18", "18", "19", "20", "21", "22", "23", "24", "Above 24"),
        type = QuestionType.SLIDER,
        info = "Select the average temperature in degrees Celsius."
    ),
    SurveyQuestion(
        questionText = "What is the estimated monthly electricity consumption in kWh?",
        options = listOf("kWh"),
        type = QuestionType.TEXT
    ),
    SurveyQuestion(
        questionText = "Do you have photovoltaic panels installed?",
        options = listOf("Yes", "No"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "What type of personal car do you own?",
        options = listOf("I don't own", "Petrol car", "Diesel car", "Hybrid car", "GPL car", "GNC car", "Plug-in hybrid car",
            "Electric car"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "How many kilometers do you drive weekly?",
        options = listOf("km"),
        type = QuestionType.TEXT
    ),
    SurveyQuestion(
        questionText = "How many hours per week do you use the bus?",
        options = listOf("0h", "0-5h", "5-10h", "10-15h"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "How many hours per week do you use the minibus?",
        options = listOf("0h", "0-5h", "5-10h", "10-15h"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "How many hours per week do you use the subway?",
        options = listOf("0h", "0-5h", "5-10h", "10-15h"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "How many hours per week do you use the tram?",
        options = listOf("0h", "0-5h", "5-10h", "10-15h"),
        type = QuestionType.RADIO
    ),
    SurveyQuestion(
        questionText = "How many short annual flights (maximum 3h30) do you have?",
        options = (0..24).map { it.toString() },
        type = QuestionType.SLIDER,
        info = "One flight represents a single segment (one single flight between 2 airports). Round-trip flights include 2 flight segments."
    ),
    SurveyQuestion(
        questionText = "How many long annual flights (over 4h) do you have?",
        options = (0..24).map { it.toString() },
        type = QuestionType.SLIDER,
        info = "One flight represents a single segment (one single flight between 2 airports). Round-trip flights include 2 flight segments."
    ),
    )

fun formatCityName(cityName: String): String {
    val words = cityName.split(" ")
    return if (words.size == 2) {
        "${words[0]}+${words[1]}"
    } else {
        cityName
    }
}

fun formatRoomString(roomString: String): String {
    // Split the input string by space
    val parts = roomString.split(" ")
    // Check if the input format is as expected
    return if (parts.size == 2 && parts[1] == "rooms") {
        "${parts[0]}+camere"
    } else {
        // Return the input string if the format is not as expected
        roomString
    }
}

fun translateYesNo(input: String): String {
    return when (input.lowercase()) {
        "Yes" -> "da"
        "No" -> "nu"
        else -> input // return the input as is if it doesn't match "yes" or "no"
    }
}

fun translateInsulationLevel(description: String): String {
    return when (description) {
        "Uninsulated house: No envelope elements (walls, floors under the attic, terrace) are thermally insulated" ->
            "ne-izolata"
        "Insulated house - low level: Envelope elements are partially insulated and/or the thermal insulation was done before 2010" ->
            "izolata-redus"
        "Insulated house - standard: The building envelope is fully insulated, and thermal insulation was done between 2010 and 2020" ->
            "standard"
        "Well-insulated house: The building envelope is fully insulated, and the thermal insulation was done after 2020" ->
            "bine-izolata"
        else -> description // return the input as is if it doesn't match any of the known descriptions
    }
}

fun translateTimePeriod(period: String): String {
    return when (period) {
        "Before 1990" -> "1990"
        "Between 1990 - 2020" -> "1990-2020"
        "After 2020" -> "2020"
        else -> period // return the input as is if it doesn't match any of the known descriptions
    }
}

fun translateEnergySource(source: String): String {
    return when (source) {
        "Natural gas" -> "gaz-natural"
        "Electricity" -> "energie-electrica"
        "Biomass - firewood" -> "biomasa-lemne"
        "Thermal energy from solar collectors" -> "energie-colectoare"
        "District heating (centralized system) / cogeneration" -> "termoficare"
        "Electricity from photovoltaic panels" -> "energie-panouri"
        "Biomass - briquettes/pellets" -> "biomasa-peleti"
        "Thermal energy from heat pumps" -> "energie-pompe"
        else -> source // return the input as is if it doesn't match any of the known descriptions
    }
}

fun translateCarType(carType: String): String {
    return when (carType) {
        "I don't own" -> "niciunul"
        "Petrol car" -> "benzina"
        "Diesel car" -> "diesel"
        "Hybrid car" -> "hibrid"
        "GPL car" -> "gpl"
        "GNC car" -> "gnc"
        "Plug-in hybrid car" -> "hibrid-plug-in"
        "Electric car" -> "electric"
        else -> carType // return the input as is if it doesn't match any of the known descriptions
    }
}

fun translateFloorDescription(description: String): String {
    return when (description) {
        "1 (Ground floor)" -> "1"
        "2 (Ground floor + 1 floor)" -> "2"
        "3 (Ground floor + 2 floors)" -> "3"
        "4 (Ground floor + 3 floors)" -> "4"
        else -> description // return the input as is if it doesn't match any of the known descriptions
    }
}

fun translateFloorHeight(height: String): String {
    return when(height) {
        "Below 2.20 m" -> "2"
        "2.20 m" -> "2.2"
        "2.40 m"-> "2.4"
        "2.60 m" -> "2,6"
        "2.80 m"-> "2.8"
        "3 m"-> "3"
        "Above 3 m" -> "3.2"
        else -> height
    }
}
fun translateTemperature(temperature: String): String {
    return when(temperature) {
        "Below 18" -> "18"
        "Above 24" -> "25"
        else -> temperature
    }
}

fun transformNumber(numberString: String): String {
    // Convert the string to a double
    val number = numberString.toDouble()
    // Convert the double to an integer (this will truncate the decimal part)
    val integerPart = number.toInt()
    // Return the integer part as a string
    return integerPart.toString()
}