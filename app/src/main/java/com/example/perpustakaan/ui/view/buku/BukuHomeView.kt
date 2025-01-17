package com.example.perpustakaan.ui.view.buku

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpustakaan.model.Buku
import com.example.perpustakaan.ui.customwidget.CustomTopAppBar
import com.example.perpustakaan.ui.navigation.DestinasiNavigasi
import com.example.perpustakaan.ui.viewmodel.buku.HomeBukuUiState
import com.example.perpustakaan.ui.viewmodel.buku.HomeBukuViewModel
import com.example.perpustakaan.ui.viewmodel.buku.PenyediaBukuViewModel


object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Daftar Buku"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAddBuku: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeBukuViewModel = viewModel(factory = PenyediaBukuViewModel.Factory)
) {

    var searchQuery by remember  { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<Buku?>(null) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                CustomTopAppBar(
                    title = DestinasiHome.titleRes,
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior,
                    onRefresh = {
                        viewModel.getBuku()
                    }
                )
                // Tambah SearchBar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Cari buku berdasarkan judul, penulis, atau kategori") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari") },
                    singleLine = true
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddBuku,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Buku")
            }
        }
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.bukuUIState,
            searchQuery = searchQuery,
            retryAction = { viewModel.getBuku() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { buku -> showDeleteDialog = buku }
        )

        // Dialog konfirmasi hapus
        showDeleteDialog?.let { buku ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Apakah Anda yakin ingin menghapus buku '${buku.judul}'?") },
                confirmButton = {
                    TextButton (onClick = {
                        viewModel.deleteBuku(buku.idBuku)
                        showDeleteDialog = null
                    }) {
                        Text("Ya, Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun HomeStatus(
    homeUiState: HomeBukuUiState,
    searchQuery: String,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Buku) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (homeUiState) {
        is HomeBukuUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeBukuUiState.Success -> {
            val filteredBooks = homeUiState.buku.filter { buku ->
                val searchLower = searchQuery.lowercase()
                buku.judul.lowercase().contains(searchLower) ||
                        buku.penulis.lowercase().contains(searchLower) ||
                        buku.kategori.lowercase().contains(searchLower)
            }

            if (filteredBooks.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (searchQuery.isEmpty())
                            "Tidak ada data buku"
                        else
                            "Tidak ditemukan buku yang sesuai"
                    )
                }
            } else {
                BukuLayout(
                    bukuList = filteredBooks,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { buku -> onDetailClick(buku.idBuku) },
                    onDeleteClick = onDeleteClick
                )
            }
        }
        is HomeBukuUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}
@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Konfirmasi Hapus") },
        text = { Text("Apakah Anda yakin ingin menghapus buku ini?") },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("Hapus")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier.size(100.dp))
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Terjadi kesalahan.")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = retryAction) {
            Text(text = "Coba Lagi")
        }
    }
}

@Composable
fun BukuLayout(
    bukuList: List<Buku>,
    modifier: Modifier = Modifier,
    onDetailClick: (Buku) -> Unit,
    onDeleteClick: (Buku) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(bukuList) { buku ->
            BukuCard(
                buku = buku,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(buku) },
                onDeleteClick = { onDeleteClick(buku) }
            )
        }
    }
}

@Composable
fun BukuCard(
    buku: Buku,
    modifier: Modifier = Modifier,
    onDeleteClick: (Buku) -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = buku.judul, style = MaterialTheme.typography.titleLarge)
            Text(text = "Penulis: ${buku.penulis}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Kategori: ${buku.kategori}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Status: ${buku.status}", style = MaterialTheme.typography.bodyMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onDeleteClick(buku) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus Buku")
                }
            }
        }
    }
}
