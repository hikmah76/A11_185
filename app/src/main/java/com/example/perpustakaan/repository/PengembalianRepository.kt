package com.example.perpustakaan.repository

import com.example.perpustakaan.model.AllPengembalianResponse
import com.example.perpustakaan.model.Pengembalian
import com.example.perpustakaan.service.PengembalianService
import java.io.IOException

interface PengembalianRepository {
    suspend fun getPengembalian(): AllPengembalianResponse
    suspend fun insertPengembalian(pengembalian: Pengembalian)
    suspend fun updatePengembalian(idPengembalian: String, pengembalian: Pengembalian)
    suspend fun deletePengembalian(idPengembalian: String)
    suspend fun getPengembalianById(idPengembalian: String): Pengembalian
}

class NetworkPengembalianRepository(
    private val pengembalianApiService: PengembalianService
) : PengembalianRepository {
    override suspend fun getPengembalian(): AllPengembalianResponse =
        pengembalianApiService.getAllPengembalian()

    override suspend fun insertPengembalian(pengembalian: Pengembalian) {
        pengembalianApiService.insertPengembalian(pengembalian)
    }

    override suspend fun updatePengembalian(idPengembalian: String, pengembalian: Pengembalian) {
        pengembalianApiService.updatePengembalian(idPengembalian, pengembalian)
    }

    override suspend fun deletePengembalian(idPengembalian: String) {
        try {
            val response = pengembalianApiService.deletePengembalian(idPengembalian)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete pengembalian. HTTP Status code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPengembalianById(idPengembalian: String): Pengembalian {
        return pengembalianApiService.getPengembalianById(idPengembalian).data
    }
}