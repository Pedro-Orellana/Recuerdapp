package com.pedroapps.recuerdapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoRoomEntity::class], version = 1, exportSchema = false)
abstract class RecuerdappDatabase: RoomDatabase() {

    abstract fun memoDao() : MemoDao

    companion object {

        private var databaseInstance: RecuerdappDatabase? = null
        fun getInstance(context: Context): RecuerdappDatabase {

            return databaseInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = RecuerdappDatabase::class.java,
                    name = "recuerdapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                databaseInstance = instance
                instance
            }
        }
    }
}