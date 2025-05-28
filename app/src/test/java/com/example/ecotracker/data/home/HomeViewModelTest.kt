package com.example.ecotracker.data.home

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel()
    }


    @Test
    fun getWeekNumberForDate_SpecificDate_ReturnsCorrectWeekNumber() {
        val date = LocalDate.of(2024, 1, 1)
        val expectedWeekNumber = 1 // Assuming it is the 1st week of the year
        val actualWeekNumber =  homeViewModel.getCurrentWeekNumber(date)
        assertEquals(expectedWeekNumber, actualWeekNumber)
    }

    @Test
    fun getWeekNumberForDate_AnotherSpecificDate_ReturnsCurrentWeekNumber() {
        val date = LocalDate.of(2024, 5, 23)
        val expectedWeekNumber = 21 // Assuming it is the 1st week of the year
        val actualWeekNumber =  homeViewModel.getCurrentWeekNumber(date)
        assertEquals(expectedWeekNumber, actualWeekNumber)
    }

}