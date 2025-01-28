package com.example.perpustakaan.ui.view.anggota

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.perpustakaan.ui.viewmodel.anggota.EditAnggotaViewModel
import com.example.perpustakaan.ui.viewmodel.anggota.InsertUiEvent
import com.example.perpustakaan.ui.viewmodel.anggota.InsertUiState
import com.example.perpustakaan.ui.viewmodel.anggota.PenyediaAnggotaViewModel
import com.example.perpustakaan.ui.viewmodel.buku.DetailBukuUiState
import kotlinx.coroutines.launch

object DestinasiEditAnggota : DestinasiNavigasi {
    override val route = "anggota_edit"
    override val titleRes = "Edit Anggota"
    const val idAnggota = "idAnggota"
    val routeWithArgs = "$route/{$idAnggota}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnggotaEditView(
    navigateBack: () -> Unit, // Fungsi callback untuk navigasi kembali.
    onNavigateUp: () -> Unit,
    viewModel: EditAnggotaViewModel = viewModel(factory = PenyediaAnggotaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) } // State untuk menampilkan pesan error.

    Scaffold(
        topBar = {
            // AppBar kustom dengan judul dan tombol kembali.
            CustomTopAppBar(
                title = DestinasiEditAnggota.titleRes,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        EditEntryBodyAnggota(
            insertUiState = viewModel.uiState,
            onAnggotaValueChange = viewModel::updateInsertAnggotaState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.editAnggota()) {
                        navigateBack()
                    } else {
                        showError = true
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )

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
fun EditEntryBodyAnggota(
    insertUiState: InsertUiState,
    onAnggotaValueChange: (InsertUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInputAnggota(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onAnggotaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@Composable
fun FormInputAnggota(
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
            value = insertUiEvent.nama,
            onValueChange = { onValueChange(insertUiEvent.copy(nama = it)) },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.email,
            onValueChange = { onValueChange(insertUiEvent.copy(email = it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.nomorTelepon,
            onValueChange = { onValueChange(insertUiEvent.copy(nomorTelepon = it)) },
            label = { Text("Nomor Telepon") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}