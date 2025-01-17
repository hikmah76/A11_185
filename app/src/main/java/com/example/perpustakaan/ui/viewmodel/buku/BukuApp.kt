package com.example.perpustakaan.ui.viewmodel.buku

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.perpustakaan.ui.navigation.PengelolaHalaman

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BukuApp(modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            PengelolaHalaman()
        }
    }
}