package com.dicoding.picodiploma.submission3.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavorite(favoriteUser: Favorite)

    @Query("SELECT * FROM favorite")
    fun getFavoriteUser(): LiveData<List<Favorite>>

    @Query("SELECT COUNT(*) FROM favorite WHERE favorite.id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    fun removeFavorite(id: Int): Int
}