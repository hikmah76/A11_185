package com.example.perpustakaan.service

import com.example.perpustakaan.model.AllPeminjamanResponse
import com.example.perpustakaan.model.Peminjaman
import com.example.perpustakaan.model.PeminjamanDetailResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Headers

interface PeminjamanService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("peminjaman")
    suspend fun getAllPeminjaman(): AllPeminjamanResponse

    @GET("peminjaman/{idPeminjaman}")
    suspend fun getPeminjamanById(@Path("idPeminjaman") idPeminjaman: String): PeminjamanDetailResponse

    @POST("peminjaman")
    suspend fun insertPeminjaman(@Body peminjaman: Peminjaman)

    @PUT("peminjaman/{idPeminjaman}")
    suspend fun updatePeminjaman(@Path("idPeminjaman") idPeminjaman: String, @Body peminjaman: Peminjaman)

    @DELETE("peminjaman/{idPeminjaman}")
    suspend fun deletePeminjaman(@Path("idPeminjaman") idPeminjaman: String): retrofit2.Response<Void>
}