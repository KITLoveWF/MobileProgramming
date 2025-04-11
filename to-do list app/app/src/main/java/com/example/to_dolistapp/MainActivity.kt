package com.example.to_dolistapp

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_dolistapp.ui.theme.TodoListAppTheme
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListAppTheme {
                App()

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun App(modifier: Modifier = Modifier ){
    var isChecked by remember { mutableStateOf(false) }
    var isSortDate by remember { mutableStateOf(false) }
    var isSortName by remember { mutableStateOf(false) }
    var selectIcon by remember { mutableStateOf(1) }
//    var listName = remember { mutableStateListOf<String>() }
//    var listDate = remember { mutableStateListOf<String>() }
    val taskList = mutableListOf<Pair<String, String>>()
    var nameTask by remember { mutableStateOf("") }
    var dateTask by remember { mutableStateOf("") }
    var selectdate by remember { mutableStateOf("") }
    var selectname by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("To do task list")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray, // Màu nền
                    titleContentColor = Color.White // Màu chữ
                ),
                actions = {
                    IconButton(onClick = {
                        selectIcon = 1
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Danh sách nhiệm vụ", tint = Color.White)
                    }
                    IconButton(onClick = {
                        isSortDate = !isSortDate
                        selectIcon = 2
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Tìm kiếm theo ngày", tint = Color.White)
                    }
                    IconButton(onClick = {
                        isSortName = !isSortName
                        selectIcon = 3
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Tìm kiếm theo tên nhiệm vụ", tint = Color.White)
                    }
                }

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isChecked = !isChecked

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
        InputValue(
            showDialog = isChecked,
            onConfirm = {
                name,date ->
                nameTask = name
                dateTask = date
//                listName.add(nameTask)
//                listDate.add(dateTask)
                taskList.add(Pair(nameTask,dateTask))
            },
            onDismiss = {
                isChecked = false

            }
        )

        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
               horizontalAlignment = Alignment.Start) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Black)
            )
            {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
//                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                )
                {
                    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                        Text("Danh sách",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 20.sp)
                            )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    if(selectIcon == 2)
                    {
                        //listName.forEach(name ->)
                        for ((name,date) in taskList)
                        {
                            if(date == selectdate) {
                                Row(modifier = Modifier.fillMaxWidth()
                                    .shadow(1.dp)
                                    .border(1.dp, Color.LightGray)
                                    .background(Color.White),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(name,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            color = Color.Red
                                            )
                                        )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                    }
                    else if(selectIcon == 1)
                    {
                        for ((name,date) in taskList)
                        {
                                Row(modifier = Modifier.fillMaxWidth()
                                    .shadow(1.dp)
                                    .border(1.dp, Color.LightGray)
                                    .background(Color.White),
                                    horizontalArrangement = Arrangement.Center

                                ) {
                                    Text(name,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            color = Color.Red
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    else if(selectIcon == 3)
                    {
                        for ((name,date) in taskList)
                        {
                            if(check(selectname,name)) {

                                Row(modifier = Modifier.fillMaxWidth()
                                    .shadow(1.dp)
                                    .border(1.dp, Color.LightGray)
                                    .background(Color.White),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(name,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            color = Color.Red
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }

                }
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
//                    .border(2.dp, Color.Black)
                    .padding(16.dp)
                ) {

//                    listDate.forEach { date ->
//                        Row(modifier = Modifier.fillMaxWidth()) {
//                            Text(date)
//                        }
//                        Spacer(modifier = Modifier.height(20.dp))
//                    }
                    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                        Text("Thời gian",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    if(selectIcon == 2)
                    {
                        //listName.forEach(name ->)
                        for ((name,date) in taskList)
                        {
                            if(date == selectdate) {
                                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                                    Text(date,style = TextStyle(fontSize = 20.sp))
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                    }
                    else if(selectIcon == 1)
                    {
                        for ((name,date) in taskList)
                        {
                            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                                Text(date,style = TextStyle(fontSize = 20.sp))
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    else if(selectIcon == 3)
                    {
                        for ((name,date) in taskList)
                        {
                            if(check(selectname,name)) {
                                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                                    Text(date,style = TextStyle(fontSize = 20.sp))
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }
                }

            }

        }

        SortDate(
            showDialog = isSortDate,
            onDismiss = {
                isSortDate = false
            },
            onConfirm = {
                    date -> selectdate = date
            }
        )
        SortName(
            showDialog = isSortName,
            onDismiss = {
                isSortName = false
            },
            onConfirm = {
                    name -> selectname = name
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputValue(
           showDialog: Boolean,
            onDismiss: () -> Unit,
            onConfirm: (String,String) -> Unit
           )
{
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }


    val day by remember { mutableStateOf(null) }
    val month by remember { mutableStateOf(null) }
    val year by remember { mutableStateOf(null) }
    val dtp = rememberDatePickerState(
        day,month, yearRange = IntRange(1900,2300),
        initialDisplayMode = DisplayMode.Input,
    )
    val select = dtp.selectedDateMillis
    date = select?.let{
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(Date(it))
    }.toString()
    if(showDialog)
    {
    AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Nhập task cần làm") },
            text ={
                Column {
                    Text("Tên task")
                    OutlinedTextField(
                        value = name,
                        onValueChange = {name = it},
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ngày thực hiện")
                    DatePicker(state = dtp)

                }

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(name,date)
                        onDismiss()
                    }
                ) {
                    Text("Xác nhận")
                }

            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Huỷ")
                }

            },

            )
        }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDate(showDialog: Boolean,
             onDismiss: () -> Unit,
             onConfirm: (String) -> Unit
             )
{
    var date by remember { mutableStateOf("") }
    val day by remember { mutableStateOf(null) }
    val month by remember { mutableStateOf(null) }
    val dtp = rememberDatePickerState(
        day,month, yearRange = IntRange(1900,2300),
        initialDisplayMode = DisplayMode.Input,
    )
    val select = dtp.selectedDateMillis
    date = select?.let{
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(Date(it))
    }.toString()
    if(showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Sắp xếp theo ngày") },
            text = {
                Column {
                    Text("Ngày thực hiện")
                    DatePicker(state = dtp)

                }

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(date)
                        onDismiss()
                    }
                ) {
                    Text("Xác nhận")
                }

            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Huỷ")
                }

            },

            )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortName(showDialog: Boolean,
             onDismiss: () -> Unit,
             onConfirm: (String) -> Unit
)
{
    var name by remember { mutableStateOf("") }

    if(showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Sắp xếp theo tên") },
            text = {
                Column {
                    Text("Tên nhiệm vụ")
                    OutlinedTextField(
                        value = name,
                        onValueChange = {name = it},
                        modifier = Modifier.fillMaxWidth()
                    )


                }

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(name)
                        onDismiss()
                    }
                ) {
                    Text("Xác nhận")
                }

            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Huỷ")
                }

            },

            )
    }
}
fun check(sub: String, pa: String): Boolean {
    for (i in sub.indices) {
        for (j in i + 1..sub.length) {
            val result = sub.substring(i, j)
            Log.d("result",result)
            if (pa.contains(result, ignoreCase = true)) {
                return true
            }
        }
    }
    return false
}

