package ro.pub.cs.systems.eim.lab05.startedserviceactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView

class StartedServiceBroadcastReceiver(private val messageTextView: TextView?) : BroadcastReceiver() {

    // TODO: exercise 7 - default constructor

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: exercise 5 - get the action and the extra information from the intent and set the text on the messageTextView
        // TODO: exercise 7 - if the activity is not visible,
        // post a notification that opens the activity with the message

    }
}
