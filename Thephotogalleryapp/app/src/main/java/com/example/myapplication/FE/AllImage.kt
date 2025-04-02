package com.example.myapplication.FE

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.Model.Image
import com.example.myapplication.ViewModel.ImageViewModel
import coil.compose.rememberAsyncImagePainter

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.myapplication.R
import java.io.InputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AllImage(navController: NavController,modifier: Modifier)
{
    val context = LocalContext.current

    // 🎯 Khởi tạo request permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Cần cấp quyền để hiển thị ảnh", Toast.LENGTH_SHORT).show()
        }
    }

    // 🚀 Yêu cầu quyền khi Composable này được khởi tạo
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val imageViewModel: ImageViewModel = viewModel()
    val _images = imageViewModel.getImage().observeAsState(emptyList())
    val images = _images.value
    Log.d("Ảnh","${images}")

    var selected = remember{ mutableStateOf<String?>(null) }
    var count = remember{ mutableStateOf(0) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("The photo gallery app")
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Gray, // Màu nền
                titleContentColor = Color.White // Màu chữ
            ),
            navigationIcon = {
                IconButton(onClick = {navController.navigate("mainScreen")}) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

        )
    }){ paddingValues->
        Column (modifier = Modifier
            .padding(paddingValues)
            .wrapContentSize(Alignment.Center)){

            if(images.size != 0){
            //Text("Xin chào")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(images.size)
                { index ->
                    Image(
                        painter = rememberAsyncImagePainter(images[index].uri),
                        contentDescription = "Ảnh thứ ${images[index].id}",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(5.dp)
                            .border(3.dp, Color.Red, shape = RoundedCornerShape(10.dp))
                            //.clickable { navController.navigate("ScreenDetail/${images[index].id}") }
                            .clickable {
                                //selected.value = null
                                selected.value = images[index].uri
                                count.value++
                                //ImageZoomEffect(images[index].uri)
                                //Log.d("Ảnh","đường dẫn của ảnh thứ ${images[index].id}: ${images[index].uri}")
                            }
                    )


//                MyCard(images[index], modifier, navController)
                }
            }
        }
            else{
                Text(
                    "Không có bất kỳ ảnh nào trong đây",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )

            }

        }

        selected.value?.let {
            uri-> ImageViewer(uri,navController){selected.value = null}
            //Log.d("ảnh","${uri}")
        }
        //selected.value = null

        }
}














//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ImageViewer(imageUri: String, navController: NavController, onClose: () -> Unit) {
//    val currentImageUri = remember { mutableStateOf(imageUri) }
//    currentImageUri?.let {
//        var isDialogOpen = remember(it) { mutableStateOf(true) }
//        val imageViewModel: ImageViewModel = viewModel()
//        var image by remember { mutableStateOf<Image?>(null) }
//        LaunchedEffect(currentImageUri.value) {
//            image = imageViewModel.findImage(currentImageUri.value) // Gọi suspend fun trong coroutine scope
//            Log.d("Test", "Image: ${image?.uri}")
//        }
//        //var Image = imageViewModel.findImage(imageUri)
//        var isFavorite by remember { mutableStateOf(false) }
//        isFavorite = image?.isFavorite ?: false
//        Log.d("dialog", "Biến ${isDialogOpen.value}")
//
//
//        var imagePrevious by remember { mutableStateOf<Image?>(null) }
//        var imageNext by remember { mutableStateOf<Image?>(null) }
//        LaunchedEffect(image?.id) {
//            imagePrevious = imageViewModel.getPreviousImageUri(image?.id?:-1)
//            imageNext = imageViewModel.getNextImageUri(image?.id?:-1)
//        }
//
//
//
//
//
//
//
//        if (isDialogOpen.value) {
//            Dialog(onDismissRequest = {
//                isDialogOpen.value = false
//                onClose()
//            }) {
//                Scaffold(
//                    topBar = {
//                        TopAppBar(
//                            title = { Text("Photo Detail") },
//                            navigationIcon = {
//                                IconButton(onClick = { navController.navigate("allImage") }) {
//                                    Icon(
//                                        Icons.Default.ArrowBack,
//                                        contentDescription = "Back",
//                                        tint = MaterialTheme.colorScheme.onSurface
//                                    )
//                                }
//                            },
//                            actions = {
//                                IconButton(
//                                    onClick = {
//                                        isFavorite = !isFavorite
//                                        Log.d("Ảnh before","${image}")
//                                        image?.let {
//                                            val updatedImage = it.copy(isFavorite = isFavorite) // Tạo bản sao mới với giá trị isFavorite đã thay đổi
//                                            imageViewModel.updateImage(updatedImage) // Cập nhật vào database
//                                        }
//                                        Log.d("Ảnh after","${image}")
//                                    }
//                                ) {
//                                    Icon(
//                                        imageVector = if (isFavorite) {
//
//                                            Icons.Default.Favorite
//                                        } else {
//                                            Icons.Default.FavoriteBorder
//                                        },
//                                        contentDescription = "Favorite",
//                                        tint = if (isFavorite) {
//                                            MaterialTheme.colorScheme.tertiary
//                                        } else {
//                                            MaterialTheme.colorScheme.onSurfaceVariant
//                                        }
//                                    )
//                                }
//
//                                IconButton(onClick = { /* Share functionality */ }) {
//                                    Icon(
//                                        imageVector = Icons.Default.Share,
//                                        contentDescription = "Share",
//                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
//                                    )
//                                }
//                            },
//                            colors = TopAppBarDefaults.topAppBarColors(
//                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
//                            )
//                        )
//                    }
//                ) { padding ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(padding)
//                            //.background(Color.Black.copy(alpha = 0.8f)) // Làm mờ nền
//                            .clickable {
//                                isDialogOpen.value = false
//                                onClose()
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        ZoomableImage(currentImageUri.value)
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .align(Alignment.BottomCenter)
//                                .padding(16.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            IconButton(
//                                onClick = {
//                                    imagePrevious?.uri?.let { uri ->
//                                        currentImageUri.value = uri
//                                    }
//
//                                },
//                                enabled = imagePrevious != null
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.ArrowBack,
//                                    contentDescription = "Previous"
//                                )
//                            }
//
//                            IconButton(
//                                onClick = {
//                                    imageNext?.uri?.let { uri ->
//                                        currentImageUri.value = uri
//                                    }
//                                },
//                                enabled = imageNext != null
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.ArrowForward,
//                                    contentDescription = "Next",
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
//
//@Composable
//fun ZoomableImage(imageUri: String) {
//    var scale by remember { mutableStateOf(1f) } // Mức độ phóng to
//    var offsetX by remember { mutableStateOf(0f) } // Kéo ngang
//    var offsetY by remember { mutableStateOf(0f) } // Kéo dọc
//
//    val state = rememberTransformableState { zoomChange, panChange, _ ->
//        scale *= zoomChange
//        offsetX += panChange.x
//        offsetY += panChange.y
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black) // Nền tối giúp xem ảnh rõ hơn
//            .pointerInput(Unit) {
//                detectTransformGestures { _, pan, zoom, _ ->
//                    scale *= zoom
//                    offsetX += pan.x
//                    offsetY += pan.y
//                }
//            }
//            .graphicsLayer(
//                scaleX = scale.coerceIn(1f, 5f), // Giới hạn zoom từ 1x đến 5x
//                scaleY = scale.coerceIn(1f, 5f),
//                translationX = offsetX,
//                translationY = offsetY
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = rememberAsyncImagePainter(imageUri),
//            contentDescription = "Zoomable Image",
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewer(imageUri: String, navController: NavController, onClose: () -> Unit) {
    val currentImageUri = remember { mutableStateOf(imageUri) }
    currentImageUri?.let {
        var isDialogOpen = remember(it) { mutableStateOf(true) }
        val imageViewModel: ImageViewModel = viewModel()
        var image by remember { mutableStateOf<Image?>(null) }
        LaunchedEffect(currentImageUri.value) {
            image = imageViewModel.findImage(currentImageUri.value) // Gọi suspend fun trong coroutine scope
            Log.d("Test", "Image: ${image?.uri}")
        }
        //var Image = imageViewModel.findImage(imageUri)
        var isFavorite by remember { mutableStateOf(false) }
        isFavorite = image?.isFavorite ?: false
        Log.d("dialog", "Biến ${isDialogOpen.value}")


        var imagePrevious by remember { mutableStateOf<Image?>(null) }
        var imageNext by remember { mutableStateOf<Image?>(null) }
        LaunchedEffect(image?.id) {
            imagePrevious = imageViewModel.getPreviousImageUri(image?.id?:-1)
            imageNext = imageViewModel.getNextImageUri(image?.id?:-1)
        }


        if (isDialogOpen.value) {
            Dialog(onDismissRequest = {
                isDialogOpen.value = false
                onClose()
            }) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Photo Detail") },
                            navigationIcon = {
                                IconButton(onClick = { navController.navigate("allImage") }) {
                                    Icon(
                                        Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        isFavorite = !isFavorite
                                        Log.d("Ảnh before","${image}")
                                        image?.let {
                                            val updatedImage = it.copy(isFavorite = isFavorite) // Tạo bản sao mới với giá trị isFavorite đã thay đổi
                                            imageViewModel.updateImage(updatedImage) // Cập nhật vào database
                                        }
                                        Log.d("Ảnh after","${image}")
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isFavorite) {

                                            Icons.Default.Favorite
                                        } else {
                                            Icons.Default.FavoriteBorder
                                        },
                                        contentDescription = "Favorite",
                                        tint = if (isFavorite) {
                                            MaterialTheme.colorScheme.tertiary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }

                                IconButton(onClick = { /* Share functionality */ }) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            )
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            //.background(Color.Black.copy(alpha = 0.8f)) // Làm mờ nền
                            .clickable {
                                isDialogOpen.value = false
                                onClose()
                            }, // Đóng khi bấm ra ngoài
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(currentImageUri.value),
                            contentDescription = "Ảnh phóng to",
                            modifier = Modifier
                                .fillMaxSize()
                                //.aspectRatio(1f) // Giữ tỷ lệ ảnh
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                        IconButton(
                            onClick = {
                                imagePrevious?.uri?.let { uri ->
                                    currentImageUri.value = uri
                                }

                            },
                            enabled = imagePrevious != null
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous"
                            )
                        }

                        IconButton(
                            onClick = {
                                imageNext?.uri?.let { uri ->
                                    currentImageUri.value = uri
                                }
                            },
                            enabled = imageNext != null
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                            )
                        }
                    }
                    }
                }
            }
        }
    }
}

