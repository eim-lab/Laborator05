package ro.pub.cs.systems.eim.lab05.startedserviceactivity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class StartedServiceActivity : AppCompatActivity() {

    companion object {
        // TODO: exercise 7 - read by the receiver declared in the manifest
        var isVisible = false
    }

    private lateinit var messageTextView: TextView
    private lateinit var startedServiceBroadcastReceiver: StartedServiceBroadcastReceiver
    private lateinit var startedServiceIntentFilter: IntentFilter
    private var serviceStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_service)

        messageTextView = findViewById(R.id.message_text_view)
        messageTextView.movementMethod = ScrollingMovementMethod()

        // TODO: exercise 6a - create an instance of the StartedServiceBroadcastReceiver broadcast receiver
        startedServiceBroadcastReceiver = StartedServiceBroadcastReceiver(messageTextView)

        // TODO: exercise 6b - create an instance of an IntentFilter
        // with all available actions contained within the broadcast intents sent by the service
        startedServiceIntentFilter = IntentFilter().apply {
            addAction(Constants.ACTION_STRING)
            addAction(Constants.ACTION_INTEGER)
            addAction(Constants.ACTION_ARRAY_LIST)
        }

        // TODO: exercise 4 - start the service (moved to onResume to start after registering receiver)

        // TODO: exercise 7 - request the permission to post notifications (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                Constants.PERMISSION_REQUEST_POST_NOTIFICATIONS
            )
        }
    }

    override fun onResume() {
        super.onResume()
        isVisible = true

        // TODO: exercise 6c - register the broadcast receiver with the corresponding intent filter
        // Register with RECEIVER_EXPORTED for cross-app broadcasts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter, Context.RECEIVER_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter)
        }

        // TODO: exercise 4 - start the service AFTER registering the receiver
        if (!serviceStarted) {
            val intent = Intent().apply {
                component = ComponentName(
                    "ro.pub.cs.systems.eim.lab05.startedservice",
                    "ro.pub.cs.systems.eim.lab05.startedservice.StartedService"
                )
            }
            startForegroundService(intent)
            serviceStarted = true
        }
    }

    override fun onPause() {
        // TODO: exercise 6c - unregister the broadcast receiver
        unregisterReceiver(startedServiceBroadcastReceiver)
        isVisible = false

        super.onPause()
    }

    override fun onDestroy() {
        // TODO: exercise 6d - stop the service
        val intent = Intent().apply {
            component = ComponentName(
                "ro.pub.cs.systems.eim.lab05.startedservice",
                "ro.pub.cs.systems.eim.lab05.startedservice.StartedService"
            )
        }
        stopService(intent)

        super.onDestroy()
    }

    // TODO: exercise 7 - implement the onNewIntent callback method
    // get the message from the extra field of the intent
    // and display it in the messageTextView

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val message = intent.getStringExtra(Constants.MESSAGE)
        message?.let {
            val currentText = messageTextView.text?.toString() ?: ""
            messageTextView.text = currentText + (if (currentText.isEmpty()) "" else "\n") + it
        }
    }
}
