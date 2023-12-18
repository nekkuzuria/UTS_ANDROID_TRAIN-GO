package com.example.uts_traingo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Train::class], version = 1, exportSchema = false)
abstract class TrainDatabase : RoomDatabase() {
    abstract fun trainDao(): TrainDao

    companion object {
        @Volatile
        private var INSTANCE: TrainDatabase? = null

        fun getInstance(context : Context) : TrainDatabase ? {
            if (INSTANCE == null){
                synchronized(TrainDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, TrainDatabase::class.java,
                        "train_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}