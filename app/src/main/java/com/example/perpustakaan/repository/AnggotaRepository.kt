package com.example.perpustakaan.repository

import com.example.perpustakaan.model.AllAnggotaResponse
import com.example.perpustakaan.model.Anggota
import com.example.perpustakaan.service.AnggotaService
import java.io.IOException

interface AnggotaRepository {
    suspend fun getAnggota(): AllAnggotaResponse
    suspend fun insertAnggota(anggota: Anggota)
    suspend fun updateAnggota(idAnggota: String, anggota: Anggota)
    suspend fun deleteAnggota(idAnggota: String)
    suspend fun getAnggotaById(idAnggota: String): Anggota
}

class NetworkAnggotaRepository(
    private val anggotaApiService: AnggotaService
) : AnggotaRepository {
    override suspend fun getAnggota(): AllAnggotaResponse =
        anggotaApiService.getAllAnggota()

    override suspend fun insertAnggota(anggota: Anggota) {
        anggotaApiService.insertAnggota(anggota)
    }

    override suspend fun updateAnggota(idAnggota: String, anggota: Anggota) {
        anggotaApiService.updateAnggota(idAnggota, anggota)
    }

    override suspend fun deleteAnggota(idAnggota: String) {
        try {
            val response = anggotaApiService.deleteAnggota(idAnggota)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete anggota. HTTP Status code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAnggotaById(idAnggota: String): Anggota {
        return anggotaApiService.getAnggotaById(idAnggota).data
    }
}