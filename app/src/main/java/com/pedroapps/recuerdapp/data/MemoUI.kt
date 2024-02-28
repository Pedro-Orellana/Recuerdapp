package com.pedroapps.recuerdapp.data

data class MemoUI(
    val id: Int = 0,
    val memo: String,
    val millis: Long
) {


    companion object  {

        fun getEmptyMemo() : MemoUI {
            return MemoUI(
                id = -1,
                memo = "",
                millis = -1
            )
        }

    }




}
