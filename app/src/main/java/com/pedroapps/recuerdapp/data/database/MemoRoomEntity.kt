package com.pedroapps.recuerdapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo_table")
data class MemoRoomEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    val id: Int = 0,
    @ColumnInfo(name = "memo_string")
    val memo: String,
    @ColumnInfo(name = "memo_day")
    val day: String,
    @ColumnInfo(name = "memo_time")
    val time: String
)