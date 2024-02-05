package com.practicum.shift.data

import android.app.Application
import androidx.room.Room

class App : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,
            AppDatabase::class.java, "contacts-db").build()
    }
}