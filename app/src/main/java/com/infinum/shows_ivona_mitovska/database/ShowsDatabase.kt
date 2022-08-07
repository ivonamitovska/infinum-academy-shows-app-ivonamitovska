package com.infinum.shows_ivona_mitovska.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinum.shows_ivona_mitovska.database.converter.Converters
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.model.Show
import com.infinum.shows_ivona_mitovska.utils.Constants

@Database(
    entities = [Show::class, Review::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ShowsDatabase : RoomDatabase() {
    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    Constants.SHOW_DB
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun showDao(): ShowDao
    abstract fun reviewDao(): ReviewDao
}