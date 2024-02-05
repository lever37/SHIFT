package com.practicum.shift.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}