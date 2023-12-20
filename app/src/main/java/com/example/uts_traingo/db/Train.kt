package com.example.uts_traingo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(BooleanArrayConverter::class)
data class Train(
    @ColumnInfo(name = "asal") val asal: String,
    @ColumnInfo(name = "tujuan") val tujuan: String,
    @ColumnInfo(name = "kelas") val kelas: String,
    @ColumnInfo(name = "fasilitas") val fasilitas: MutableList<Boolean>,
    @ColumnInfo(name = "harga") val harga: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

