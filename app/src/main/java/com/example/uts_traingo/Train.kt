package com.example.uts_traingo

data class Train(
    val asal: String,
    val tujuan: String,
    val kelas: String,
    val fasilitas: MutableList<Boolean>,
    val harga: Int
) {
    // Konstruktor tanpa argumen untuk deserialisasi Firestore
    constructor() : this("", "", "", mutableListOf(), 0)
}
