package com.example.perpustakaan.service

import com.example.perpustakaan.model.AllBukuResponse
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.model.BukuDetailResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Headers

interface BukuService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("buku")
    suspend fun getAllBuku(): AllBukuResponse

    @GET("buku/{idBuku}")
    suspend fun getBukuById(@Path("idBuku") idBuku: String): BukuDetailResponse

    @POST("buku")
    suspend fun insertBuku(@Body buku: Buku)

    @PUT("buku/{idBuku}")
    suspend fun updateBuku(@Path("idBuku") idBuku: String, @Body buku: Buku)

    @DELETE("buku/{idBuku}")
    suspend fun deleteBuku(@Path("idBuku") idBuku: String): retrofit2.Response<Void>
}