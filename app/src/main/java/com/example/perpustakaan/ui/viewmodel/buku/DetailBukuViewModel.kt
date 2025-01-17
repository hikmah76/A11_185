package com.example.perpustakaan.ui.viewmodel.buku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.repository.BukuRepository
import com.example.perpustakaan.ui.view.buku.DestinasiDetailBuku
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailBukuUiState {
    data class Success(val buku: Buku) : DetailBukuUiState()
    object Error : DetailBukuUiState()
    object Loading : DetailBukuUiState()
}

class BukuDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val bukuRepository: BukuRepository
) : ViewModel() {

    private val idBuku: String = checkNotNull(savedStateHandle[DestinasiDetailBuku.idBuku])
    var detailBukuUiState: DetailBukuUiState by mutableStateOf(DetailBukuUiState.Loading)
        private set

    init {
        getBukuById()
    }

    fun getBukuById() {
        viewModelScope.launch {
            detailBukuUiState = DetailBukuUiState.Loading
            detailBukuUiState = try {
                DetailBukuUiState.Success(buku = bukuRepository.getBukuById(idBuku))
            } catch (e: IOException) {
                DetailBukuUiState.Error
            }
        }
    }
}
