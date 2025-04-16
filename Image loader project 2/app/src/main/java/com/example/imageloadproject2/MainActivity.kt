package com.example.imageloadproject2

import android.app.LoaderManager
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import coil.compose.AsyncImage
import com.example.imageloadproject2.ui.theme.ImageLoadProject2Theme

class MainActivity : ComponentActivity(){

    val imageLoader = ImageLoaderHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageLoadProject2Theme {
                App(
                    startLoader = { url, onSuccess, onStatus ->
                        imageLoader.loadImage(url, onSuccess, onStatus)
                    }

                )

                    }


            }
        }

}
@Composable

fun App(
    modifier: Modifier = Modifier,
    startLoader: (String, onSuccess:(String) -> Unit, onStatus:(String) -> Unit) -> Unit
)
{
    var text by remember{ mutableStateOf("") }
    var url by remember { mutableStateOf("") }
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
                            // Start service with notification
                            val serviceIntent = Intent(context, ImageLoaderService::class.java)
//                            context.startService(serviceIntent)
                            ContextCompat.startForegroundService(context, serviceIntent)
                            startLoader(
                                text,
                                { url = it },
                                { notification = it }
                            )
//                            startLoader(
//                                text,
//                                onComplete = { resultUrl -> url = resultUrl },
//                                onStatus = { status -> notification = status }
//                            )


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

            AsyncImage(
                model = url,
                contentDescription = "Ảnh từ internet",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Crop
            )


            Text(
                text = notification,
                color = Color.Green,
                modifier = Modifier.padding(16.dp)
            )



        }
    }

}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

