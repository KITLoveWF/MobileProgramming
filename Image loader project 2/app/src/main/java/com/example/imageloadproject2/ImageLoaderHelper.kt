package com.example.imageloadproject2

import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import android.os.Bundle
import androidx.activity.ComponentActivity
//import androidx.loader.content.Loader

class ImageLoaderHelper(private val activity: ComponentActivity) :
    LoaderManager.LoaderCallbacks<String> {

    private var urlToLoad: String = ""
    private var onUrlLoaded: ((String) -> Unit)? = null
    private var onResult: ((String) -> Unit)? = null

    fun loadImage(
        url: String,
        onSuccess: (String) -> Unit,
        onStatus: (String) -> Unit
    ) {
        urlToLoad = url
        onUrlLoaded = onSuccess
        onResult = onStatus
        LoaderManager.getInstance(activity)
            .restartLoader(1001, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        onResult?.invoke("Loading...")
        return ImageLoader(activity, urlToLoad)
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        if (data != null && data != "Default value") {
            onUrlLoaded?.invoke(data)
            onResult?.invoke("Image loaded successfully")
        } else {
            onResult?.invoke("Failed to load image")
        }
    }

    override fun onLoaderReset(loader: Loader<String>) {}
}
