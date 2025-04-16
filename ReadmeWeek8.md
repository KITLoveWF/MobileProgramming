# Android App with AsyncTask, AsyncTaskLoader, Broadcasts, Services, and Notifications

## 📱 Overview

This Android application demonstrates the use of asynchronous processing and system components including:

- `AsyncTask` for background operations.
- `AsyncTaskLoader` for lifecycle-aware background loading.
- `BroadcastReceiver` for communication between components.
- `Service` for long-running background tasks.
- `Notifications` for user interaction and alerts.

## ▶️ Running the App

### Prerequisites

- Android Studio (Arctic Fox or higher recommended)
- Android SDK version 21+
- Emulator or physical device (API 21+)

### Steps

1. Clone or download the project.
2. Open it with Android Studio.
3. Connect a device or launch an emulator.
4. Run the app using the green "Run" button or `Shift + F10`.

## ⚙️ Setup Instructions

Ensure the following permissions and dependencies are set in your app:

### **AndroidManifest.xml**
```sh
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />

    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.ImageLoadProject2"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.ImageLoadProject2">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <service
            android:name=".ImageLoaderService"
            android:enabled="true"
            android:exported="false" />
    </application>

</manifest>
```
### **AsyncTask**
```sh
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
```
### **AsyncTaskLoader**
```sh
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
```
### **Service**
```sh

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
```
### **Notifications**
```sh
class ImageLoaderService : Service() {

    private val handler = Handler()
    private lateinit var runnable: Runnable

    companion object {
        const val CHANNEL_ID = "ImageLoaderServiceChannel"
        const val NOTIFICATION_ID = 1
        const val INTERVAL: Long = 60 * 1000 // 1 phút
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Image Loader Service is running")
        Log.d("ImageLoaderService", "Service started with startForeground")
        startForeground(NOTIFICATION_ID, notification)

        runnable = object : Runnable {
            override fun run() {
                val updatedNotification = createNotification("Image Loader Service is still running")
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, updatedNotification)

                handler.postDelayed(this, INTERVAL)
            }
        }

        handler.postDelayed(runnable, INTERVAL)

        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(message: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        Log.d("ImageLoaderService", "🛎️ Notification created: $message")
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Image Loader")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Image Loader Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
```
