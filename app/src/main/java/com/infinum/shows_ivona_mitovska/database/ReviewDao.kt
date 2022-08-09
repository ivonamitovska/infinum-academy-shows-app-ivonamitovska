package com.infinum.shows_ivona_mitovska.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.model.Show

@Dao
interface ReviewDao {
    @Query("SELECT * FROM review WHERE showId IS:showId ")
    fun getAllReviews(showId:String): LiveData<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReview(review:Review)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(reviews:List<Review>)

}