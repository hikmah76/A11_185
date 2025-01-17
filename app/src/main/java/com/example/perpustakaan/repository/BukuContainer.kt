package com.example.perpustakaan.repository

import com.example.perpustakaan.service.AnggotaService
import com.example.perpustakaan.service.BukuService
import com.example.perpustakaan.service.PeminjamanService
import com.example.perpustakaan.service.PengembalianService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bukuRepository: BukuRepository
    val anggotaRepository: AnggotaRepository
    val peminjamanRepository: PeminjamanRepository
    val pengembalianRepository: PengembalianRepository
}

class BukuContainer : AppContainer {
    private val baseUrl = "http://192.168.56.1:3000/api/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val bukuService: BukuService by lazy { retrofit.create(BukuService::class.java) }
    private val anggotaService: AnggotaService by lazy { retrofit.create(AnggotaService::class.java) }
    private val peminjamanService: PeminjamanService by lazy { retrofit.create(PeminjamanService::class.java) }
    private val pengembalianService: PengembalianService by lazy { retrofit.create(PengembalianService::class.java) }

    override val bukuRepository: BukuRepository by lazy { NetworkBukuRepository(bukuService) }
    override val anggotaRepository: AnggotaRepository by lazy { NetworkAnggotaRepository(anggotaService) }
    override val peminjamanRepository: PeminjamanRepository by lazy { NetworkPeminjamanRepository(peminjamanService) }
    override val pengembalianRepository: PengembalianRepository by lazy { NetworkPengembalianRepository(pengembalianService) }
}