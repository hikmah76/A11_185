package com.example.perpustakaan.repository

import com.example.perpustakaan.model.AllBukuResponse
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.service.BukuService
import java.io.IOException

interface BukuRepository {
    suspend fun getBuku(): AllBukuResponse
    suspend fun insertBuku(buku: Buku)
    suspend fun updateBuku(idBuku: String, buku: Buku)
    suspend fun deleteBuku(idBuku: String)
    suspend fun getBukuById(idBuku: String): Buku
}

// Implementasi NetworkBukuRepository
class NetworkBukuRepository(
    private val bukuApiService: BukuService
) : BukuRepository {
    override suspend fun insertBuku(buku: Buku) {
        bukuApiService.insertBuku(buku)
    }

    override suspend fun updateBuku(idBuku: String, buku: Buku) {
        bukuApiService.updateBuku(idBuku, buku)
    }

    override suspend fun deleteBuku(idBuku: String) {
        try {
            val response = bukuApiService.deleteBuku(idBuku)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete buku. HTTP Status code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getBuku(): AllBukuResponse =
        bukuApiService.getAllBuku()

    override suspend fun getBukuById(idBuku: String): Buku {
        return bukuApiService.getBukuById(idBuku).data
    }
}