package com.example.imageloaderproject

import android.content.AsyncTaskLoader
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.imageloaderproject.ui.theme.ImageLoaderProjectTheme
import coil.compose.AsyncImage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setContent {
            ImageLoaderProjectTheme {
                App()
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun  App(modifier: Modifier = Modifier)
{
    var text by remember{ mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    //lateinit var bitmapState: MutableState<Bitmap?>
    var context = LocalContext.current
    //val isConnect = isInternetAvailable(context)
    var notification by remember { mutableStateOf("") }
    val activity = LocalActivity.current as MainActivity
    var isCheckConnection by remember{ mutableStateOf(true) }

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally

        )
        {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Nhập đường link ảnh") },
                )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    if(isInternetAvailable(context))
                    {
                        if(text.isNotEmpty())
                        {
                            ImageLoaderTask(
                                context,
                                onResult = {
                                        message->notification = message
                                },
                                onUrlLoaded = {
                                        link->url=link
                                }
                            ).execute(text)


                        }
                        else
                        {
                            Toast.makeText(context, "Please enter a valid URL", Toast.LENGTH_SHORT).show()
                        }

                    }
                    else {
                        isCheckConnection = false
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(16.dp),
                enabled = isCheckConnection
            ) {
                Text("Load ảnh")
            }

            //Log.d("link",text)
            AsyncImage(
                    model = url,
                    contentDescription = "Ảnh từ internet",
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Crop,
            )


            Text(
                text = notification,
                color = Color.Green,
                modifier = Modifier.padding(16.dp)
            )


        }
    }

}

class ImageLoaderTask(
    private val context: Context,
    private val onResult: (String) ->Unit,
    private val onUrlLoaded: (String) -> Unit
) : AsyncTask<String,Void, String>(){
    override fun onPreExecute() {
        onResult("Loading...")

        super.onPreExecute()
    }

    override fun doInBackground(vararg params: String?): String {

        return try {
            val urlString = params.getOrNull(0) ?: return "Default value"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            if (bitmap != null) urlString else "Default value"
        } catch (e: Exception) {
            e.printStackTrace()
            "Default value"
        }
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        if(result != "Default value")
        {
            onUrlLoaded(result)
            onResult("Image loaded successfully")
        }
        else{
            onResult("Failed to load image")
        }
    }

}



fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
//fun loadImageFromUrl(url: String, onLoaded: (String) -> Unit) {
//    val loaderId = 101
//
//    val bundle = Bundle().apply {
//        putString("url", url)
//    }
//
//    LoaderManager.getInstance(this).restartLoader(loaderId, bundle, object : LoaderManager.LoaderCallbacks<String> {
//        override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
//            val imageUrl = args?.getString("url") ?: ""
//            return ImageLoader(this, imageUrl)
//        }
//
//        override fun onLoadFinished(loader: Loader<String>, data: String?) {
//            if (data != null && data != "Default value") {
//                onLoaded(data)
//            } else {
//                onLoaded("Failed to load image")
//            }
//        }
//
//        override fun onLoaderReset(loader: Loader<String>) {
//            // nothing to reset
//        }
//    })
//}

