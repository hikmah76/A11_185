package com.example.perpustakaan.service

import com.example.perpustakaan.model.AllAnggotaResponse
import com.example.perpustakaan.model.AllBukuResponse
import com.example.perpustakaan.model.Anggota
import com.example.perpustakaan.model.AnggotaDetailResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Headers

interface AnggotaService {
    @GET("anggota")
    suspend fun getAllAnggota(): AllAnggotaResponse // Pastikan ini mengembalikan AllAnggotaResponse

    @GET("anggota/{idAnggota}")
    suspend fun getAnggotaById(@Path("idAnggota") idAnggota: String): AnggotaDetailResponse

    @POST("anggota")
    suspend fun insertAnggota(@Body anggota: Anggota)

    @PUT("anggota/{idAnggota}")
    suspend fun updateAnggota(@Path("idAnggota") idAnggota: String, @Body anggota: Anggota)

    @DELETE("anggota/{idAnggota}")
    suspend fun deleteAnggota(@Path("idAnggota") idAnggota: String): retrofit2.Response<Void>
}