package com.example.perpustakaan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.perpustakaan.ui.view.buku.BukuDetailView
import com.example.perpustakaan.ui.view.buku.DestinasiAddBuku
import com.example.perpustakaan.ui.view.buku.DestinasiDetailBuku
import com.example.perpustakaan.ui.view.buku.DestinasiEditBuku
import com.example.perpustakaan.ui.view.buku.EditBukuView
import com.example.perpustakaan.ui.view.buku.HomeScreen
import com.example.perpustakaan.ui.view.buku.InsertBukuView

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()) {
    NavHost (
        navController = navController,
        startDestination = DestinasiBuku.route,
        modifier = Modifier
    ) {
        composable(route = DestinasiBuku.route) {
            HomeScreen(
                navigateToAddBuku = { navController.navigate(DestinasiAddBuku.route) },
                onDetailClick = { idBuku ->
                    navController.navigate("${DestinasiDetailBuku.route}/$idBuku")
                }
            )
        }
        composable(DestinasiAddBuku.route) {
            InsertBukuView(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(DestinasiEditBuku.routeWithArgs) { backStackEntry ->
            val idBuku = backStackEntry.arguments?.getString(DestinasiEditBuku.idBuku)
            EditBukuView(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.popBackStack() }
            )
        }
        composable(DestinasiDetailBuku.routeWithArgs) { backStackEntry ->
            val idBuku = backStackEntry.arguments?.getString(DestinasiDetailBuku.idBuku)
            BukuDetailView(
                navigateBack = { navController.popBackStack() },
                onEditClick = { navController.navigate("${DestinasiEditBuku.route}/$idBuku") }
            )
        }
    }
}