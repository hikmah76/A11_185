package com.example.perpustakaan.ui.view.anggota

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import com.example.perpustakaan.model.Anggota
import com.example.perpustakaan.ui.customwidget.CustomTopAppBar
import com.example.perpustakaan.ui.navigation.DestinasiNavigasi
import com.example.perpustakaan.ui.viewmodel.anggota.DetailAnggotaUiState
import com.example.perpustakaan.ui.viewmodel.anggota.DetailAnggotaViewModel
import com.example.perpustakaan.ui.viewmodel.anggota.PenyediaAnggotaViewModel

// Object untuk mendefinisikan navigasi detail anggota
object DestinasiDetailAnggota : DestinasiNavigasi {
    override val route = "anggota_detail"
    override val titleRes = "Detail Anggota"
    const val idAnggota = "idAnggota"
    val routeWithArgs = "$route/{$idAnggota}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnggotaDetailView(
    navigateBack: () -> Unit, // Fungsi untuk navigasi kembali
    detailViewModel: DetailAnggotaViewModel = viewModel(factory = PenyediaAnggotaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    // Struktur tampilan layar dengan Scaffold
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailAnggota.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        // Menampilkan status detail anggota
        DetailStatusAnggota(
            anggotaUiState = detailViewModel.detailAnggotaUiState,
            retryAction = { detailViewModel.getAnggotaById() },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun DetailStatusAnggota(
    anggotaUiState: DetailAnggotaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (anggotaUiState) {
        is DetailAnggotaUiState.Success -> { // Jika data berhasil dimuat
            DetailCardAnggota(
                anggota = anggotaUiState.anggota,
                modifier = modifier.padding(16.dp)
            )
        }
        is DetailAnggotaUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DetailAnggotaUiState.Error -> {
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
fun DetailCardAnggota( // Fungsi untuk menampilkan detail anggota
    anggota: Anggota,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Menampilkan setiap detail anggota
            ComponentDetailAnggota(judul = "ID Anggota", isinya = anggota.id_anggota.toString())
            ComponentDetailAnggota(judul = "Nama", isinya = anggota.nama)
            ComponentDetailAnggota(judul = "Email", isinya = anggota.email)
            ComponentDetailAnggota(judul = "Nomor Telepon", isinya = anggota.nomorTelepon)
        }
    }
}

@Composable
fun ComponentDetailAnggota(
    judul: String,
    isinya: String,
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
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