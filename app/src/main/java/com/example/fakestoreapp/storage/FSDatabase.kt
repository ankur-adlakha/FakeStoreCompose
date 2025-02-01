package com.example.fakestoreapp.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.storage.daos.ProductDao
import com.example.fakestoreapp.storage.entities.CategoryEntity

@Database(
    entities = [Product::class, CategoryEntity::class], version = 1
)
abstract class FSDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: FSDatabase? = null
        fun getDatabase(context: Context): FSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FSDatabase::class.java,
                    "fs_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}