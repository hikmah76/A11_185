package com.example.perpustakaan.model

import kotlinx.serialization.Serializable

@Serializable
data class Pengembalian(
    val idPengembalian: String,
    val idPeminjaman: String,
    val tanggalDikembalikan: String
)

@Serializable
data class AllPengembalianResponse(
    val status: Boolean,
    val message: String,
    val data: List<Pengembalian>
)

@Serializable
data class PengembalianDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Pengembalian
)
