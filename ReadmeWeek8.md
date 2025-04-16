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
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

<application
    ... >
    
    <!-- Register BroadcastReceiver -->
    <receiver android:name=".MyBroadcastReceiver" />

    <!-- Register Service -->
    <service android:name=".MyService" android:exported="false" />
</application>
