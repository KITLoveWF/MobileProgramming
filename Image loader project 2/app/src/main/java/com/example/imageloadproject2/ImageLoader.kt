package com.example.imageloadproject2

import androidx.loader.content.AsyncTaskLoader
import android.content.Context
import android.graphics.BitmapFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader(
    context: Context,
    private val url: String
): AsyncTaskLoader<String>(context)
{
    private var cachedResult: String? = null

    override fun onStartLoading() {
        if (cachedResult != null) {
            deliverResult(cachedResult)
        } else {
            forceLoad()
        }
        super.onStartLoading()
    }
    override fun loadInBackground(): String {
        return try {
            val imageUrl = URL(url)
            val connection = imageUrl.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            if (bitmap != null) url else "Default value"
        } catch (e: Exception) {
            e.printStackTrace()
            "Default value"
        }
    }

    override fun deliverResult(data: String?) {
        cachedResult = data
        super.deliverResult(data)
    }
}