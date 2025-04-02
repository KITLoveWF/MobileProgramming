package com.example.myapplication.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.DAO.ImageDAO
import com.example.myapplication.Model.Image

@Database(entities = [Image::class], version = 2)
abstract class ImageDatabase : RoomDatabase() {
    abstract  fun imageDAO (): ImageDAO
    companion object {
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDatabase(context: Context): ImageDatabase {
            val temInstance = INSTANCE
            if(temInstance!=null)
            {
                return  temInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    "image_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}