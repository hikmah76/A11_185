package com.example.perpustakaan.ui.view.buku

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpustakaan.ui.customwidget.CustomTopAppBar
import com.example.perpustakaan.ui.navigation.DestinasiNavigasi
import com.example.perpustakaan.ui.viewmodel.buku.DetailBukuUiState
import com.example.perpustakaan.ui.viewmodel.buku.InsertBukuViewModel
import com.example.perpustakaan.ui.viewmodel.buku.InsertUiEvent
import com.example.perpustakaan.ui.viewmodel.buku.InsertUiState
import com.example.perpustakaan.ui.viewmodel.buku.PenyediaBukuViewModel
import kotlinx.coroutines.launch

object DestinasiAddBuku : DestinasiNavigasi {
    override val route = "buku_add"
    override val titleRes = "Tambah Buku"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertBukuView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertBukuViewModel = viewModel(factory = PenyediaBukuViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = DestinasiAddBuku.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBody(
            insertUiState = viewModel.uiState,
            onBukuValueChange = viewModel::updateInsertBukuState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.insertBuku()) {
                        navigateBack()
                    } else {
                        showError = true // Tampilkan pesan kesalahan
                    }
                }
            },
            modifier = modifier
                .padding(innerPadding)
                .offset(y = (-70).dp)
        )

        // Tampilkan pesan kesalahan jika validasi gagal
        if (showError) {
            Text(
                text = "Semua field harus diisi!",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun EntryBody(
    insertUiState: InsertUiState,
    onBukuValueChange: (InsertUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInput(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onBukuValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button (
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputEdit(
    insertUiEvent: InsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.judul,
            onValueChange = { onValueChange(insertUiEvent.copy(judul = it)) },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.penulis,
            onValueChange = { onValueChange(insertUiEvent.copy(penulis = it)) },
            label = { Text("Penulis") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.kategori,
            onValueChange = { onValueChange(insertUiEvent.copy(kategori = it)) },
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.status,
            onValueChange = { onValueChange(insertUiEvent.copy(status = it)) },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}