package com.example.perpustakaan.ui.view.anggota

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpustakaan.model.Anggota
import com.example.perpustakaan.ui.customwidget.CustomTopAppBar
import com.example.perpustakaan.ui.navigation.DestinasiNavigasi
import com.example.perpustakaan.ui.view.buku.OnError
import com.example.perpustakaan.ui.view.buku.OnLoading
import com.example.perpustakaan.ui.viewmodel.anggota.HomeAnggotaUiState
import com.example.perpustakaan.ui.viewmodel.anggota.HomeAnggotaViewModel
import com.example.perpustakaan.ui.viewmodel.anggota.PenyediaAnggotaViewModel
import com.example.perpustakaan.ui.viewmodel.buku.DetailBukuUiState

object DestinasiHomeAnggota : DestinasiNavigasi {
    override val route = "anggota"
    override val titleRes = "Daftar Anggota"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnggotaHomeView(
    navigateToAddAnggota: () -> Unit, // Navigasi ke halaman tambah anggota
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeAnggotaViewModel = viewModel(factory = PenyediaAnggotaViewModel.Factory)
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<Anggota?>(null) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiHomeAnggota.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = { viewModel.getAnggota() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddAnggota,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Anggota")
            }
        }
    ) { innerPadding ->
        HomeStatusAnggota(
            homeUiState = viewModel.anggotaUIState,
            searchQuery = searchQuery,
            retryAction = { viewModel.getAnggota() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { anggota -> showDeleteDialog = anggota }
        )

        showDeleteDialog?.let { anggota ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Apakah Anda yakin ingin menghapus anggota '${anggota.nama}'?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteAnggota(anggota.id_anggota.toString())
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
fun HomeStatusAnggota(
    homeUiState: HomeAnggotaUiState,
    searchQuery: String,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Anggota) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (homeUiState) {
        is HomeAnggotaUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeAnggotaUiState.Success -> {
            val filteredAnggota = homeUiState.anggota.filter { anggota ->
                val searchLower = searchQuery.lowercase()
                anggota.nama.lowercase().contains(searchLower) ||
                        anggota.email.lowercase().contains(searchLower) ||
                        anggota.nomorTelepon.lowercase().contains(searchLower)
            }

            if (filteredAnggota.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (searchQuery.isEmpty())
                            "Tidak ada data anggota"
                        else
                            "Tidak ditemukan anggota yang sesuai"
                    )
                }
            } else {
                AnggotaLayout(
                    anggotaList = filteredAnggota,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = onDetailClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
        is HomeAnggotaUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun AnggotaLayout(
    anggotaList: List<Anggota>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Anggota) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ID/Nama",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Email/Nomor Telepon",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Aksi",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.End
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(anggotaList.size) { index ->
                val anggota = anggotaList[index]
                AnggotaCard(
                    anggota = anggota,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDetailClick(anggota.id_anggota.toString()) },
                    onDeleteClick = { onDeleteClick(anggota) }
                )
            }
        }
    }
}

@Composable
fun AnggotaCard(
    anggota: Anggota,
    modifier: Modifier = Modifier,
    onDeleteClick: (Anggota) -> Unit = {}
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
            Text(text = anggota.nama, style = MaterialTheme.typography.titleLarge)
            Text(text = "Email: ${anggota.email}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Nomor Telepon: ${anggota.nomorTelepon}", style = MaterialTheme.typography.bodyMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onDeleteClick(anggota) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus Anggota")
                }
            }
        }
    }
}