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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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

object DestinasiDetailBuku : DestinasiNavigasi {
    override val route = "buku_detail"
    override val titleRes = "Detail Buku"
    const val idBuku = "idBuku"
    val routeWithArgs = "$route/{$idBuku}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BukuDetailView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    detailViewModel: BukuDetailViewModel = viewModel(factory = PenyediaBukuViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                    val idBuku = (detailViewModel.detailBukuUiState as? DetailBukuUiState.Success)?.buku?.idBuku
                    if (idBuku != null) onEditClick(idBuku)
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
                .padding(innerPadding).offset(y = (-70).dp)
        ) {
            DetailStatus(
                bukuUiState = detailViewModel.detailBukuUiState,
                retryAction = { detailViewModel.getBukuById() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailStatus(
    bukuUiState: DetailBukuUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (bukuUiState) {
        is DetailBukuUiState.Success -> {
            DetailCard(
                buku = bukuUiState.buku,
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
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Terjadi kesalahan.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button (onClick = retryAction) {
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
            ComponentDetailBuku(judul = "ID Buku", isinya = buku.idBuku)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Judul", isinya = buku.judul)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Penulis", isinya = buku.penulis)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Kategori", isinya = buku.kategori)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailBuku(judul = "Status", isinya = buku.status)
        }
    }
}

@Composable
fun ComponentDetailBuku(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
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
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}