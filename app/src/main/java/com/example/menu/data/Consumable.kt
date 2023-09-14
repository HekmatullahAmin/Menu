package com.example.menu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumable")
data class Consumable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var price: Double,
    @ColumnInfo(name = "image_url")
    var imageUrl: String,
    var description: String
)
