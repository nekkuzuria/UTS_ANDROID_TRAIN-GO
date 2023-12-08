package com.example.uts_traingo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Train(
    @ColumnInfo(name = "asal") val asal: String,
    @ColumnInfo(name = "tujuan") val tujuan: String,
    @ColumnInfo(name = "kelas") val kelas: String,
    @ColumnInfo(name = "fasilitas") val fasilitas: List<String>,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

