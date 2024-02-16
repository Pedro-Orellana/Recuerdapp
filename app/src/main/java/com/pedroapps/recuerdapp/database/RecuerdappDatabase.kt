package com.pedroapps.recuerdapp.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedroapps.recuerdapp.viewmodels.RecuerdappState

abstract class RecuerdappDatabase: RoomDatabase() {

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