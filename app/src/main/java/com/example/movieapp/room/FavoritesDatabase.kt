package com.example.movieapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.data.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase: RoomDatabase() {
    abstract fun movieDatabaseDao(): FavoritesDao

    companion object{
        private var INSTANCE: FavoritesDatabase? = null

        fun getMovieDatabase(context: Context): FavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDatabase::class.java,
                    "movie_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}