package com.pedroapps.recuerdapp.data

import com.pedroapps.recuerdapp.utils.ENGLISH_LANGUAGE_CODE
import com.pedroapps.recuerdapp.utils.SPANISH_LANGUAGE_CODE
import com.pedroapps.recuerdapp.utils.getEnglishScheduledTime
import com.pedroapps.recuerdapp.utils.getSpanishScheduledTime
import java.time.Instant
import java.time.ZoneId

data class MemoUI(
    val id: Int = 0,
    var memo: String,
    var millis: Long
) {


    companion object  {

        fun getEmptyMemo() : MemoUI {
            return MemoUI(
                id = -1,
                memo = "",
                millis = -1
            )
        }


        fun getFormattedDateFromMillis(millis: Long, languageCode: String) : String {
            val instant = Instant.ofEpochMilli(millis)
            val zoneDateTime = instant.atZone(ZoneId.systemDefault())

            return when (languageCode) {
                ENGLISH_LANGUAGE_CODE -> zoneDateTime.getEnglishScheduledTime()
                SPANISH_LANGUAGE_CODE -> zoneDateTime.getSpanishScheduledTime()
                else -> "No scheduled time"
            }
        }

    }




}
