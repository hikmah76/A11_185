package com.example.perpustakaan

import android.app.Application
import com.example.perpustakaan.repository.AppContainer
import com.example.perpustakaan.repository.BukuContainer

class BukuApplications : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = BukuContainer()
    }
}
