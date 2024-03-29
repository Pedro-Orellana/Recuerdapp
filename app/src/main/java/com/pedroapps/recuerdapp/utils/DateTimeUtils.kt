package com.pedroapps.recuerdapp.utils

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerState.formatToStringDate(languageCode: String, initialValue: String): String {

    var currentDate: Date? = null
    this.selectedDateMillis?.let {
        currentDate = Date.from(Instant.ofEpochMilli(it))
    }

    currentDate?.let {

        val stringDate = it.toInstant().toString()
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val localDate = LocalDate.parse(stringDate, dateTimeFormatter)

        return when (languageCode) {
            ENGLISH_LANGUAGE_CODE -> getEnglishStringDate(localDate)
            SPANISH_LANGUAGE_CODE -> getSpanishStringDate(localDate)
            else -> "Something in another language"
        }
    }


    return initialValue
}


@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerState.getLocalDate(): LocalDate? {

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

    val day = when (currentDate.dayOfWeek) {
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.SATURDAY -> "Saturday"
        DayOfWeek.SUNDAY -> "Sunday"
        else -> ""
    }

    val month = when (currentDate.month) {
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


fun getSpanishStringDate(currentDate: LocalDate): String {
    val day = currentDate.dayOfMonth

    val dayOfWeek = when (currentDate.dayOfWeek) {
        DayOfWeek.MONDAY -> "Lunes"
        DayOfWeek.TUESDAY -> "Martes"
        DayOfWeek.WEDNESDAY -> "Miércoles"
        DayOfWeek.THURSDAY -> "Jueves"
        DayOfWeek.FRIDAY -> "Viernes"
        DayOfWeek.SATURDAY -> "Sábado"
        DayOfWeek.SUNDAY -> "Domingo"
        else -> "Sin día"
    }

    val month = when (currentDate.month) {
        Month.JANUARY -> "Enero"
        Month.FEBRUARY -> "Febrero"
        Month.MARCH -> "Marzo"
        Month.APRIL -> "Abril"
        Month.MAY -> "Mayo"
        Month.JUNE -> "Junio"
        Month.JULY -> "Julio"
        Month.AUGUST -> "Agosto"
        Month.SEPTEMBER -> "Septiembre"
        Month.OCTOBER -> "Octubre"
        Month.NOVEMBER -> "Noviembre"
        Month.DECEMBER -> "Diciembre"
        else -> "Sin mes"
    }

    val year = currentDate.year

    return "$dayOfWeek $day de $month del $year"
}


fun ZonedDateTime.getEnglishScheduledTime(): String {

    val formattedMinute = when {
        (minute < 10) -> {
            minute.toString().padStart(2, '0')
        }

        else -> minute.toString()
    }

    val formattedHour = when {
        (hour <= 12) -> hour
        else -> (hour - 12)
    }

    val timeOfDay = when {
        (hour < 12) -> "AM"
        else -> "PM"
    }


    val dayOfWeek = when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.SATURDAY -> "Saturday"
        DayOfWeek.SUNDAY -> "Sunday"
        null -> ""
    }

    val month = when (month) {
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
        null -> ""
    }

    return "$month, $dayOfWeek $dayOfMonth at $formattedHour:$formattedMinute $timeOfDay"
}

fun ZonedDateTime.getSpanishScheduledTime(): String {

    val formattedMinute = when {
        (minute < 10) -> {
            minute.toString().padStart(2, '0')
        }

        else -> minute.toString()
    }

    val formattedHour = when {
        (hour <= 12) -> hour
        else -> (hour - 12)
    }

    val timeOfDay = when {
        (hour < 12) -> "AM"
        else -> "PM"
    }

    val dayOfWeek = when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lunes"
        DayOfWeek.TUESDAY -> "Martes"
        DayOfWeek.WEDNESDAY -> "Miércoles"
        DayOfWeek.THURSDAY -> "Jueves"
        DayOfWeek.FRIDAY -> "Viernes"
        DayOfWeek.SATURDAY -> "Sábado"
        DayOfWeek.SUNDAY -> "Domingo"
        null -> ""
    }

    val month = when (month) {
        Month.JANUARY -> "Enero"
        Month.FEBRUARY -> "Febrero"
        Month.MARCH -> "Marzo"
        Month.APRIL -> "Abril"
        Month.MAY -> "Mayo"
        Month.JUNE -> "Junio"
        Month.JULY -> "Julio"
        Month.AUGUST -> "Agosto"
        Month.SEPTEMBER -> "Septiembre"
        Month.OCTOBER -> "Octubre"
        Month.NOVEMBER -> "Noviembre"
        Month.DECEMBER -> "Diciembre"
        null -> ""
    }

    return "$dayOfWeek $dayOfMonth de $month, a las $formattedHour:$formattedMinute $timeOfDay"
}


fun Long.getLocalTimeFromDateTimeMillis(): LocalTime {
    val instant = Instant.ofEpochMilli(this)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    return localDateTime.toLocalTime()
}


@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.formatTime(): String {
    return "$hour: $minute"
}


@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.getLocalTime(): LocalTime {
    return LocalTime.of(hour, minute)
}
