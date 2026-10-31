package ro.pub.cs.systems.eim.lab05.startedserviceactivity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StartedServiceActivity : AppCompatActivity() {

    // TODO: exercise 7 - companion object with isVisible, read by the receiver declared in the manifest

    private lateinit var messageTextView: TextView
    private lateinit var startedServiceBroadcastReceiver: StartedServiceBroadcastReceiver
    private lateinit var startedServiceIntentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_service)

        messageTextView = findViewById(R.id.message_text_view)

        // TODO: exercise 4 - start the service

        // TODO: exercise 6a - create an instance of the StartedServiceBroadcastReceiver broadcast receiver

        // TODO: exercise 6b - create an instance of an IntentFilter
        // with all available actions contained within the broadcast intents sent by the service

        // TODO: exercise 7 - request the permission to post notifications (Android 13+)

    }

    override fun onResume() {
        super.onResume()
        // TODO: exercise 7 - isVisible = true

        // TODO: exercise 6c - register the broadcast receiver with the corresponding intent filter
        // Note: RECEIVER_EXPORTED is required, the broadcasts come from another app (StartedService)
        if (::startedServiceBroadcastReceiver.isInitialized && ::startedServiceIntentFilter.isInitialized) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter, Context.RECEIVER_EXPORTED)
            }
        }
    }

    override fun onPause() {
        // TODO: exercise 7 - isVisible = false
        // TODO: exercise 6c - unregister the broadcast receiver
        if (::startedServiceBroadcastReceiver.isInitialized) {
            unregisterReceiver(startedServiceBroadcastReceiver)
        }

        super.onPause()
    }

    override fun onDestroy() {
        // TODO: exercise 6d - stop the service

        super.onDestroy()
    }

    // TODO: exercise 7 - implement the onNewIntent callback method
    // get the message from the extra field of the intent
    // and display it in the messageTextView
}
