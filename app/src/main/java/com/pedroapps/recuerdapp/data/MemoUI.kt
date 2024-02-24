package com.pedroapps.recuerdapp.data

data class MemoUI(
    val id: Int = 0,
    val memo: String,
    val day: String,
    val time: String
) {


    companion object  {

        fun getEmptyMemo() : MemoUI {
            return MemoUI(
                id = -1,
                memo = "",
                day = "",
                time = ""
            )
        }

    }




}
