package com.example.menu.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumableDao {
    @Insert
    suspend fun insert(consumable: Consumable)

    @Update
    suspend fun update(consumable: Consumable)

    @Delete
    suspend fun delete(consumable: Consumable)

    @Query("SELECT * FROM consumable WHERE id = :id")
    fun getConsumable(id: Int): Flow<Consumable>

    @Query("SELECT * FROM consumable")
    fun getAllConsumables(): Flow<List<Consumable>>
}