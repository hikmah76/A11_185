package com.example.perpustakaan.repository

import com.example.perpustakaan.model.AllPeminjamanResponse
import com.example.perpustakaan.model.Peminjaman
import com.example.perpustakaan.service.PeminjamanService
import java.io.IOException

interface PeminjamanRepository {
    suspend fun getPeminjaman(): AllPeminjamanResponse
    suspend fun insertPeminjaman(peminjaman: Peminjaman)
    suspend fun updatePeminjaman(idPeminjaman: String, peminjaman: Peminjaman)
    suspend fun deletePeminjaman(idPeminjaman: String)
    suspend fun getPeminjamanById(idPeminjaman: String): Peminjaman
}

class NetworkPeminjamanRepository(
    private val peminjamanApiService: PeminjamanService
) : PeminjamanRepository {
    override suspend fun getPeminjaman(): AllPeminjamanResponse =
        peminjamanApiService.getAllPeminjaman()

    override suspend fun insertPeminjaman(peminjaman: Peminjaman) {
        peminjamanApiService.insertPeminjaman(peminjaman)
    }

    override suspend fun updatePeminjaman(idPeminjaman: String, peminjaman: Peminjaman) {
        peminjamanApiService.updatePeminjaman(idPeminjaman, peminjaman)
    }

    override suspend fun deletePeminjaman(idPeminjaman: String) {
        try {
            val response = peminjamanApiService.deletePeminjaman(idPeminjaman)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete peminjaman. HTTP Status code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPeminjamanById(idPeminjaman: String): Peminjaman {
        return peminjamanApiService.getPeminjamanById(idPeminjaman).data
    }
}