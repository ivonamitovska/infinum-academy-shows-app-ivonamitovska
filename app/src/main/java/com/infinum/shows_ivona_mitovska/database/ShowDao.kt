package com.infinum.shows_ivona_mitovska.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.infinum.shows_ivona_mitovska.model.Show

@Dao
interface ShowDao {
    @Query("SELECT * FROM show")
    fun getAllShows(): LiveData<List<Show>>

    @Query("SELECT * FROM show WHERE id IS :showId")
    fun getShow(showId:String): LiveData<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<Show>)

}