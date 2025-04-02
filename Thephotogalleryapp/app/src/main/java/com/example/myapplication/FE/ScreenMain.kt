package com.example.myapplication.FE

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.Image
import com.example.myapplication.ViewModel.ImageViewModel
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@androidx.media3.common.util.UnstableApi
fun MainScreen(navController : NavController)
{
    var isChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val imageViewModel: ImageViewModel = viewModel()

    // Launcher mở thư viện ảnh
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        selectedImages = uris
    }
    LaunchedEffect(selectedImages) {
        selectedImages.forEach{
                select->
            val image = Image(0, uri = select.toString())
            Log.d("DatabaseTest", "Adding image: $image")
            imageViewModel.addImage(image)
        }
    }



    Scaffold(
            topBar = {
            TopAppBar(
                title = {
                    Text("The photo gallery app")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray, // Màu nền
                    titleContentColor = Color.White // Màu chữ
                )
            )
        },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        isChecked = !isChecked
                        imagePickerLauncher.launch("image/*")
                              },
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    shape = CircleShape

                ) {
                    if(!isChecked)
                    {
                        Icon(Icons.Default.Add, contentDescription = "Thêm Ảnh")

                    }
                    else{

                        Icon(Icons.Default.Check, contentDescription = "Đánh dấu đã thêm")
//                        navController.navigate("addImage")

                    }
                }

            }
        ) {
                paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {navController.navigate("allImage")},
                    //onClick = { imagePickerLauncher.launch("image/*")},
                    colors = ButtonDefaults.buttonColors(contentColor =  Color.Black, containerColor = Color.Cyan)
                ) {
                    Text("Xem tất cả ảnh")
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Hiển thị ảnh đã chọn
                LazyRow {
                    items(selectedImages.size) { index ->
                        Image(
                            painter = rememberAsyncImagePainter(selectedImages[index]),
                            contentDescription = "Ảnh đã chọn",
                            modifier = Modifier.size(100.dp).padding(4.dp)
                        )
                    }
                }
                //Text("Nội dung ứng dụng ở đây")
            }
        }
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text("Màn hình chính")
//
//    }
}

//@Composable
//fun InsertData()

