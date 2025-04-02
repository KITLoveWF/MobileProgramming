package com.example.myapplication.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myapplication.DAO.ImageDAO
import com.example.myapplication.Model.Image

class ImageRepository(private val imageDAO: ImageDAO) {
    val images : LiveData<List<Image>> = imageDAO.getAllImage()
    suspend fun addImage(image: Image)
    {
        Log.d("Test2","image:${image}")
        imageDAO.insertImage(image)
    }
    suspend fun  updateImage(image: Image)
    {
        imageDAO.updateImage(image)
    }
    suspend fun  findImage(uri : String): Image?
    {
        return imageDAO.findImage(uri)
    }
    suspend fun findImageNext(id : Int): Image?
    {
        return  imageDAO.findImageNext(id)
    }
    suspend fun findImagePrevious(id : Int): Image?
    {
        return  imageDAO.findImagePrevious(id)
    }
}