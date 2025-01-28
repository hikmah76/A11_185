package com.example.perpustakaan.ui.view.buku

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.ui.customwidget.CustomTopAppBar
import com.example.perpustakaan.ui.navigation.DestinasiNavigasi
import com.example.perpustakaan.ui.viewmodel.buku.BukuDetailViewModel
import com.example.perpustakaan.ui.viewmodel.buku.DetailBukuUiState
import com.example.perpustakaan.ui.viewmodel.buku.PenyediaBukuViewModel
import kotlinx.coroutines.delay

object DestinasiDetailBuku : DestinasiNavigasi {
    override val route = "buku_detail"
    override val titleRes = "Detail Buku"
    const val id_buku = "id_buku"
    val routeWithArgs = "$route/{$id_buku}"
}

sealed class DeleteStatus {
    object Idle : DeleteStatus()
    object Loading : DeleteStatus()
    object Success : DeleteStatus()
    data class Error(val message: String) : DeleteStatus()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BukuDetailView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onEditClick: (String) -> Unit, // Pastikan ini adalah fungsi navigasi
    detailViewModel: BukuDetailViewModel = viewModel(factory = PenyediaBukuViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val deleteStatus by detailViewModel.deleteStatus.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Effect untuk menangani status penghapusan
    LaunchedEffect(deleteStatus) {
        when (deleteStatus) {
            is DeleteStatus.Success -> {
                delay(1000) // Tunggu sebentar sebelum kembali
                navigateBack()
            }
            is DeleteStatus.Error -> {
                errorMessage = (deleteStatus as DeleteStatus.Error).message
                showErrorDialog = true
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailBuku.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val id_buku = (detailViewModel.detailBukuUiState as? DetailBukuUiState.Success)?.buku?.id_buku
                    if (id_buku != null) {
                        onEditClick(id_buku.toString()) // Pastikan ini memanggil fungsi navigasi
                    }
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Buku",
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .offset(y = (-70).dp)
        ) {
            // Menampilkan status detail buku, seperti loading, error, atau sukses
            DetailStatus(
                bukuUiState = detailViewModel.detailBukuUiState,
                retryAction = { detailViewModel.getBukuById() },
                onDeleteClick = { showDeleteDialog = true },
                onEditClick = {
                    val id_buku = (detailViewModel.detailBukuUiState as? DetailBukuUiState.Success)?.buku?.id_buku
                    if (id_buku != null) {
                        onEditClick(id_buku.toString()) // Panggil fungsi navigasi dengan id_buku
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }

    // Dialog konfirmasi hapus
    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                val id_buku = (detailViewModel.detailBukuUiState as? DetailBukuUiState.Success)?.buku?.id_buku
                if (id_buku != null) {
                    detailViewModel.deleteBuku(id_buku.toString()) // Panggil fungsi deleteBuku
                }
                showDeleteDialog = false // Tutup dialog setelah konfirmasi
            },
            onDismiss = { showDeleteDialog = false } // Tutup dialog jika dibatalkan
        )
    }
}


@Composable
fun DetailStatus(
    bukuUiState: DetailBukuUiState,
    retryAction: () -> Unit,
    onDeleteClick: () -> Unit, // Tambahkan parameter untuk menghapus buku
    onEditClick: () -> Unit, // Tambahkan parameter untuk mengedit buku
    modifier: Modifier = Modifier,
) {
    when (bukuUiState) {
        is DetailBukuUiState.Success -> {
            DetailCard(
                buku = bukuUiState.buku,
                onDeleteClick = onDeleteClick, // Kirim fungsi untuk menghapus buku
                onEditClick = onEditClick, // Kirim fungsi untuk mengedit buku
                modifier = modifier.padding(16.dp)
            )
        }

        is DetailBukuUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailBukuUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Terjadi kesalahan.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text(text = "Coba Lagi")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    buku: Buku,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pastikan untuk mengonversi id_buku ke String
            ComponentDetailBuku(judul = "ID Buku", isinya = buku.id_buku.toString()) // Ubah di sini
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Judul", isinya = buku.judul)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Penulis", isinya = buku.penulis)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Kategori", isinya = buku.kategori)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Status", isinya = buku.status)
        }
        // Tombol untuk mengedit dan menghapus buku
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton (onClick = onEditClick) { // Tombol untuk mengedit buku
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Buku")
            }
            IconButton(onClick = onDeleteClick) { // Tombol untuk menghapus buku
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus Buku")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Tambahkan padding
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onEditClick // Pastikan menggunakan onEditClick yang diterima dari parameter
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Buku",
                    tint = MaterialTheme.colorScheme.primary // Tambahkan warna untuk feedback visual
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Buku",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}

@Composable
fun ComponentDetailBuku(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String, // Pastikan ini adalah String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$judul:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya, // Pastikan ini adalah String
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}