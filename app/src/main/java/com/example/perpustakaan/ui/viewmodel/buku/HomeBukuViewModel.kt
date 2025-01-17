package com.example.perpustakaan.ui.viewmodel.buku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.repository.BukuRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeBukuUiState {
    data class Success(val buku: List<Buku>) : HomeBukuUiState()
    object Error : HomeBukuUiState()
    object Loading : HomeBukuUiState()
}
class HomeBukuViewModel(private val bukuRepository: BukuRepository) : ViewModel() {
    var bukuUIState: HomeBukuUiState by mutableStateOf(HomeBukuUiState.Loading)
        private set

    var showDialog by mutableStateOf(false)
    var bukuToDelete: Buku? = null

    init {
        getBuku()
    }

    fun getBuku() {
        viewModelScope.launch {
            bukuUIState = HomeBukuUiState.Loading
            bukuUIState = try {
                val bukuList = bukuRepository.getBuku().data
                HomeBukuUiState.Success(bukuList)
            } catch (e: IOException) {
                HomeBukuUiState.Error
            }
        }
    }

    fun deleteBuku(idBuku: String) {
        viewModelScope.launch {
            try {
                bukuRepository.deleteBuku(idBuku)
                getBuku() // Refresh the list after deletion
            } catch (e: IOException) {
                bukuUIState = HomeBukuUiState.Error
            }
        }
    }

    fun showDeleteConfirmation(buku: Buku) {
        bukuToDelete = buku
        showDialog = true
    }

    fun confirmDelete() {
        bukuToDelete?.let {
            deleteBuku(it.idBuku)
            showDialog = false
            bukuToDelete = null
        }
    }

    fun filterBuku(query: String) {
        viewModelScope.launch {
            bukuUIState = HomeBukuUiState.Loading
            bukuUIState = try {
                val allBuku = bukuRepository.getBuku().data
                val filteredBuku = allBuku.filter {
                    it.judul.contains(query, ignoreCase = true) ||
                            it.penulis.contains(query, ignoreCase = true) ||
                            it.kategori.contains(query, ignoreCase = true)
                }
                HomeBukuUiState.Success(filteredBuku)
            } catch (e: IOException) {
                HomeBukuUiState.Error
            }
        }
    }
}
