package com.example.mapsapp.data.room

import androidx.room.*

@Dao
interface MarkerDao {

    @Query("SELECT * FROM MarkerModel ")
    suspend fun all(): List<MarkerModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(model: MarkerModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(model: MarkerModel)

    @Delete
    suspend fun delete(model: MarkerModel)

    @Update
    suspend fun updateAll(list: List<MarkerModel>)
}