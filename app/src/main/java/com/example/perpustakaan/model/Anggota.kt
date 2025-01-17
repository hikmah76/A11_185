package com.example.perpustakaan.model


import kotlinx.serialization.Serializable

@Serializable
data class Anggota(
    val idAnggota: String,
    val nama: String,
    val email: String,
    val nomorTelepon: String
)

@Serializable
data class AllAnggotaResponse(
    val status: Boolean,
    val message: String,
    val data: List<Anggota>
)

@Serializable
data class AnggotaDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Anggota
)