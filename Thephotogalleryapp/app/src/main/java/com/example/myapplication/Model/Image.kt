package com.example.myapplication.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

//import androidx.room.PrimaryKey
//import androidx.room.vo.PrimaryKey

@Entity(tableName = "Image")
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uri: String,
    var isFavorite : Boolean = false

)
