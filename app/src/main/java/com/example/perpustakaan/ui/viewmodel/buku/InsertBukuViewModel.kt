package com.example.perpustakaan.ui.viewmodel.buku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.repository.BukuRepository
import kotlinx.coroutines.launch

class InsertBukuViewModel(private val bukuRepository: BukuRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertUiState())

    fun updateInsertBukuState(insertUiEvent: InsertUiEvent) {
        uiState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun insertBuku(): Boolean {
        if (uiState.insertUiEvent.judul.isBlank() ||
            uiState.insertUiEvent.penulis.isBlank() ||
            uiState.insertUiEvent.kategori.isBlank() ||
            uiState.insertUiEvent.status.isBlank()) {
            return false // Validasi gagal
        }
        viewModelScope.launch {
            try {
                bukuRepository.insertBuku(uiState.insertUiEvent.toBuku())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true // Validasi berhasil
    }
}

// Data class untuk menyimpan data input form
data class InsertUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent()
)

// Data class untuk variabel yang menyimpan data input form
data class InsertUiEvent(
    val idBuku: String = "",
    val judul: String = "",
    val penulis: String = "",
    val kategori: String = "",
    val status: String = ""
)

// Fungsi untuk mengonversi input form ke dalam entity Buku
fun InsertUiEvent.toBuku(): Buku = Buku(
    idBuku = idBuku,
    judul = judul,
    penulis = penulis,
    kategori = kategori,
    status = status
)