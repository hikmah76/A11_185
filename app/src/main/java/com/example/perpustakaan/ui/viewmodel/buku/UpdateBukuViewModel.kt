package com.example.perpustakaan.ui.viewmodel.buku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.repository.BukuRepository
import com.example.perpustakaan.ui.view.buku.DestinasiEditBuku
import com.example.perpustakaan.ui.viewmodel.buku.InsertUiEvent
import com.example.perpustakaan.ui.viewmodel.buku.InsertUiState
import kotlinx.coroutines.launch

class EditBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val bukuRepository: BukuRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertUiState())
        private set

    val idBuku: String = checkNotNull(savedStateHandle[DestinasiEditBuku.idBuku])

    init {
        viewModelScope.launch {
            uiState = bukuRepository.getBukuById(idBuku).toUiStateBuku()
        }
    }

    fun updateInsertBukuState(insertUiEvent: InsertUiEvent) {
        uiState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun editBuku(): Boolean {
        if (uiState.insertUiEvent.judul.isBlank() ||
            uiState.insertUiEvent.penulis.isBlank() ||
            uiState.insertUiEvent.kategori.isBlank() ||
            uiState.insertUiEvent.status.isBlank()) {
            return false // Validasi gagal
        }
        viewModelScope.launch {
            try {
                bukuRepository.updateBuku(idBuku, uiState.insertUiEvent.toBuku())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true // Validasi berhasil
    }
}

// Fungsi untuk mengonversi Buku ke dalam InsertUiState
fun Buku.toUiStateBuku(): InsertUiState = InsertUiState(
    insertUiEvent = toInsertUiEvent()
)

fun Buku.toInsertUiEvent(): InsertUiEvent = InsertUiEvent(
    idBuku = idBuku,
    judul = judul,
    penulis = penulis,
    kategori = kategori,
    status = status
)
