package com.example.perpustakaan.ui.viewmodel.buku

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.perpustakaan.BukuApplications

object PenyediaBukuViewModel {
    val Factory = viewModelFactory {
        initializer { HomeBukuViewModel(aplikasiBuku().container.bukuRepository) }
        initializer { InsertBukuViewModel(aplikasiBuku().container.bukuRepository) }
        initializer { BukuDetailViewModel(createSavedStateHandle(), bukuRepository = aplikasiBuku().container.bukuRepository) }
        // Tambahkan ViewModel lain untuk Anggota, Peminjaman, dan Pengembalian sesuai kebutuhan
    }
}

fun CreationExtras.aplikasiBuku(): BukuApplications =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BukuApplications)