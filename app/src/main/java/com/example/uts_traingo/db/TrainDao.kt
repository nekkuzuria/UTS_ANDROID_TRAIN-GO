package com.example.uts_traingo.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrainDao {
    @Query("SELECT * FROM train")
    fun getAllTrains(): List<Train>

    @Insert
    fun insertAll(vararg trains: Train)

    @Delete
    fun delete(train: Train)
}