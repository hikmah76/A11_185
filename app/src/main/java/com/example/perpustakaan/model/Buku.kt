package com.example.perpustakaan.model

import kotlinx.serialization.Serializable

@Serializable
data class Buku(
    val idBuku: String,
    val judul: String,
    val penulis: String,
    val kategori: String,
    val status: String // Ketersediaan
)

@Serializable
data class AllBukuResponse(
    val status: Boolean,
    val message: String,
    val data: List<Buku>
)

@Serializable
data class BukuDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Buku
)
