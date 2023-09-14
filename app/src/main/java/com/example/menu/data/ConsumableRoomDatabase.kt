package com.example.menu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Consumable::class], version = 1, exportSchema = false)
abstract class ConsumableRoomDatabase : RoomDatabase() {
    abstract fun getConsumableDao(): ConsumableDao

    companion object {
        @Volatile
        private var INSTANCE: ConsumableRoomDatabase? = null

        fun getDatabase(context: Context): ConsumableRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context,
                    ConsumableRoomDatabase::class.java,
                    "menu_database"
                ).build()

                INSTANCE = tempInstance
                return tempInstance
            }
        }
    }
}