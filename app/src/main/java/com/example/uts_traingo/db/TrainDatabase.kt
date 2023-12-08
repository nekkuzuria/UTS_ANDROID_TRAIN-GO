package com.example.uts_traingo.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Train::class], version = 1)
abstract class TrainDatabase : RoomDatabase() {
    abstract fun trainDao(): TrainDao
}