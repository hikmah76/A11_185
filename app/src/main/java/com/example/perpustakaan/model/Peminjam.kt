package com.example.perpustakaan.model

import kotlinx.serialization.Serializable

@Serializable
data class Peminjaman(
    val idPeminjaman: String,
    val idBuku: String,
    val idAnggota: String,
    val tanggalPeminjaman: String,
    val tanggalPengembalian: String
)

@Serializable
data class AllPeminjamanResponse(
    val status: Boolean,
    val message: String,
    val data: List<Peminjaman>
)

@Serializable
data class PeminjamanDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Peminjaman
)
