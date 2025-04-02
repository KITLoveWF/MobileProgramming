package com.example.myapplication.ViewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Database.ImageDatabase
import com.example.myapplication.Model.Image
import com.example.myapplication.Repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ImageViewModel (application: Application): AndroidViewModel(application) {
//    private val db = ImageDatabase.getDatabase(application).imageDAO()
//    private val _images = MutableLiveData<List<Image>>()
//    val images : LiveData<List<Image>> get() = _images

    private  val _images : LiveData<List<Image>>
    private  val _imageRepository : ImageRepository
    init {
        //loadImage()
        val imageDao = ImageDatabase.getDatabase(application).imageDAO()
        _imageRepository = ImageRepository(imageDao)
        _images = _imageRepository.images

    }
    fun addImage(image: Image)
    {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Test1","image:${image}")
            _imageRepository.addImage(image)
            //loadImage()
        }
    }
    fun updateImage(image: Image)
    {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Test1","image:${image}")
            _imageRepository.updateImage(image)
        }

    }
    fun getImage(): LiveData<List<Image>>
    {
        return _images;
    }
    suspend fun findImage(uri: String): Image? {

        return _imageRepository.findImage(uri)
    }

    suspend fun getNextImageUri(id : Int): Image? {
        return _imageRepository.findImageNext(id)
    }

    suspend fun getPreviousImageUri(id: Int): Image? {
        return _imageRepository.findImagePrevious(id)
    }

}