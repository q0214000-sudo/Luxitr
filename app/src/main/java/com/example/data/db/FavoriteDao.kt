package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT perfumeId FROM favorites ORDER BY savedAt DESC")
    fun getAllFavoriteIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE perfumeId = :perfumeId")
    suspend fun removeFavorite(perfumeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE perfumeId = :perfumeId)")
    fun isFavorite(perfumeId: String): Flow<Boolean>
}
