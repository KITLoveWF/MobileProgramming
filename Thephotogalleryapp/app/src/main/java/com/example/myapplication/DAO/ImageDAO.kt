package com.example.myapplication.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.Model.Image

@Dao
interface ImageDAO {
    @Query("select * from Image")
    fun getAllImage(): LiveData<List<Image>>

//    @Query("select * from Image where isFavorite == 1")
//    fun getImageFavorite(): List<Image>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)

    @Update
    suspend fun updateImage(image: Image)

    @Delete
    suspend fun deleteImage(image: Image)

    @Query("SELECT * FROM Image WHERE uri = :url")
    suspend fun findImage(url:String) : Image?

    @Query("SELECT * FROM Image WHERE  id = (:id - 1) ")
    suspend fun findImagePrevious(id : Int) : Image?

    @Query("SELECT * FROM Image WHERE  id = (:id + 1) ")
    suspend fun findImageNext(id : Int) : Image?

}