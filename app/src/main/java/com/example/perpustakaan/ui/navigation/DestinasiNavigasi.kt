package com.example.perpustakaan.ui.navigation

interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}

object DestinasiBuku : DestinasiNavigasi {
    override val route = "buku"
    override val titleRes = "Daftar Buku"
}

object DestinasiAnggota : DestinasiNavigasi {
    override val route = "anggota"
    override val titleRes = "Daftar Anggota"
}

object DestinasiPeminjaman : DestinasiNavigasi {
    override val route = "peminjaman"
    override val titleRes = "Daftar Peminjaman"
}

object DestinasiPengembalian : DestinasiNavigasi {
    override val route = "pengembalian"
    override val titleRes = "Daftar Pengembalian"
}