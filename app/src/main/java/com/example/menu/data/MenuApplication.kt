package com.example.menu.data

import android.app.Application

class MenuApplication : Application() {
    val database: ConsumableRoomDatabase by lazy {
        ConsumableRoomDatabase.getDatabase(this)
    }
}