package ro.pub.cs.systems.eim.lab05.startedserviceactivity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView

class StartedServiceBroadcastReceiver(private val messageTextView: TextView?) : BroadcastReceiver() {

    // TODO: exercise 7 - default constructor
    constructor() : this(null)

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: exercise 5 - get the action and the extra information from the intent
        // and set the text on the messageTextView
        val action = intent.action
        Log.d(Constants.TAG, "onReceive called with action: $action")
        
        val data: String? = when (action) {
            Constants.ACTION_STRING -> intent.getStringExtra(Constants.DATA)
            Constants.ACTION_INTEGER -> intent.getIntExtra(Constants.DATA, -1).toString()
            Constants.ACTION_ARRAY_LIST -> {
                val arrayList = intent.getStringArrayListExtra(Constants.DATA)
                arrayList?.toString()
            }
            else -> null
        }
        
        if (messageTextView != null && data != null) {
            val currentText = messageTextView.text?.toString() ?: ""
            messageTextView.text = currentText + (if (currentText.isEmpty()) "" else "\n") + data
            Log.d(Constants.TAG, "Updated TextView with data: $data")
        } else {
            Log.d(Constants.TAG, "messageTextView is null or data is null. TextView: ${messageTextView != null}, data: $data")
        }

        // TODO: exercise 7 - if the activity is not visible,
        // post a notification that opens the activity with the message
        if (messageTextView == null && data != null && !StartedServiceActivity.isVisible) {
            showNotification(context, data)
        }
    }

    private fun showNotification(context: Context, message: String) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(Constants.CHANNEL_ID, "Started Service", NotificationManager.IMPORTANCE_DEFAULT)
        )

        val startedServiceActivityIntent = Intent(context, StartedServiceActivity::class.java).apply {
            putExtra(Constants.MESSAGE, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            startedServiceActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Started Service")
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()
        notificationManager.notify(Constants.NOTIFICATION_ID, notification)
    }
}
