package com.example.simplealarmclockapp

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplealarmclockapp.ui.theme.SimpleAlarmClockAppTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.min
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import android.provider.AlarmClock
import android.widget.Toast
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleAlarmClockAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        //name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
fun setTime(context: Context,hour: Int, minute:Int, message: String)
{
    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_HOUR,hour)
        putExtra(AlarmClock.EXTRA_MINUTES,minute)
        putExtra(AlarmClock.EXTRA_MESSAGE,message)
        //putExtra(AlarmClock.EXTRA_SKIP_UI, true)
    }
    //intent.resolveActivity(context.packageManager)
//    context.startActivity(intent)
//    Toast.makeText(context, "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()
//    Log.d("AlarmDebug", "Hour: $hour, Minute: $minute")
//    Log.d("IntentDebug",intent.toString())
//    val alarmApp = intent.resolveActivity(context.packageManager)
//    Log.d("AlarmDebug", "Resolved Activity: $alarmApp")
    if (intent.resolveActivity(context.packageManager) == null) {
        context.startActivity(intent)
        Toast.makeText(context, "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "No alarm app available", Toast.LENGTH_SHORT).show()
    }
//    val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://google.com"))
//    context.startActivity(intent)
}
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var time by remember { mutableStateOf("No time") }
    val context = LocalContext.current
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val minute = Calendar.getInstance().get(Calendar.MINUTE)
    val timePickerState = rememberTimePickerState(
        hour, minute, is24Hour = true
    )
    Column(
        modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePicker(
            state = timePickerState
        )
        Spacer(modifier= Modifier.height(40.dp))
        Row(

        ){
            Text("Selected time: ", fontSize = 35.sp)
            Text(text = time, color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 35.sp)

        }

        Spacer(modifier= Modifier.height(40.dp))
        Button(
            onClick={
                time = String.format("%02d:%02d",timePickerState.hour,timePickerState.minute)
                setTime(context,timePickerState.hour,timePickerState.minute,"Wake up for class!")
            },
            colors = ButtonDefaults.buttonColors(
//            backgroundColor = Color.Black,
                contentColor = Color.White,
                containerColor = Color.Black
            ),
            modifier = Modifier.size(150.dp,80.dp)

        )
        {
            Text("Select", fontSize = 26.sp, fontWeight = FontWeight.Bold)

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleAlarmClockAppTheme {
        Greeting()
    }
}