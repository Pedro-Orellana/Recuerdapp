package com.pedroapps.recuerdapp.utils

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerState.formatToStringDate(languageCode: String, initialValue: String) : String {

    var currentDate: Date? = null
    this.selectedDateMillis?.let {
        currentDate = Date.from(Instant.ofEpochMilli(it))
    }

    currentDate?.let {

        val stringDate = it.toInstant().toString()
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val localDate = LocalDate.parse(stringDate, dateTimeFormatter)

        return when(languageCode) {
            "en" -> getEnglishStringDate(localDate)
            "es" -> "Something in Spanish"
            else -> "Something in another language"
        }
    }


    return initialValue
}


@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerState.getLocalDate() : LocalDate? {

    var currentDate: Date? = null
    this.selectedDateMillis?.let {
        currentDate = Date.from(Instant.ofEpochMilli(it))
    }

    currentDate?.let {
        val stringDate = it.toInstant().toString()
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        return LocalDate.parse(stringDate, dateTimeFormatter)
    }

    return null
}


fun getEnglishStringDate(currentDate: LocalDate): String {
    val numberDay = currentDate.dayOfMonth

    val day = when(currentDate.dayOfWeek) {
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.SATURDAY -> "Saturday"
        DayOfWeek.SUNDAY -> "Sunday"
        else -> ""
    }

    val month = when(currentDate.month) {
        Month.JANUARY -> "January"
        Month.FEBRUARY -> "February"
        Month.MARCH -> "March"
        Month.APRIL -> "April"
        Month.MAY -> "May"
        Month.JUNE -> "June"
        Month.JULY -> "July"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "October"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "December"
        else -> ""
    }

    val year = currentDate.year


    return "$day, $month $numberDay of $year"
}



@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.formatTime() : String {
    return "$hour: $minute"
}


@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.getLocalTime(): LocalTime {
    return LocalTime.of(hour, minute)
}
