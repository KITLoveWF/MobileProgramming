package com.example.myapplication

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.FE.AllImage
import com.example.myapplication.FE.MainScreen
import com.example.myapplication.Factory.ImageViewModelFactory
import com.example.myapplication.ViewModel.ImageViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

@androidx.media3.common.util.UnstableApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                App()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
@androidx.media3.common.util.UnstableApi
fun App(modifier: Modifier = Modifier )
{
    val navController = rememberNavController()

//    val context = LocalContext.current.applicationContext as Application
//    val imageViewModel: ImageViewModel = viewModel(factory = ImageViewModelFactory(context))
//    println(imageViewModel)

    NavHost(navController = navController, startDestination = "mainScreen")
    {
        composable("mainScreen") {
            MainScreen(navController)
        }
        composable("allImage") {
            AllImage(navController, modifier)
        }
    }
//    Log.d("NavController", "Current Destination: ${navController.currentDestination?.route}")
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text("hello")
//
//    }

}
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyApplicationTheme {
//        Greeting("Android")
//    }
//}