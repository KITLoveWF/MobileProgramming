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
```xml

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

