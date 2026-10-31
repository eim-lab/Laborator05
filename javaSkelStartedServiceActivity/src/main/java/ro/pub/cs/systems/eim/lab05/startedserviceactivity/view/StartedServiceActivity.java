package ro.pub.cs.systems.eim.lab05.startedserviceactivity.view;

import android.content.Context;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab05.startedserviceactivity.R;

public class StartedServiceActivity extends AppCompatActivity {

    // TODO: exercise 7 - public static boolean isVisible, read by the receiver declared in the manifest

    private TextView messageTextView;
    private StartedServiceBroadcastReceiver startedServiceBroadcastReceiver;
    private IntentFilter startedServiceIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started_service);

        messageTextView = (TextView)findViewById(R.id.message_text_view);

        // TODO: exercise 7 - request the permission to post notifications (Android 13+)
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: exercise 7 - isVisible = true

        // TODO: exercise 6c - register the broadcast receiver with the corresponding intent filter
        // Note: RECEIVER_EXPORTED is required, the broadcasts come from another app (StartedService)
        if (startedServiceBroadcastReceiver != null && startedServiceIntentFilter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter, Context.RECEIVER_EXPORTED);
            } else {
                registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter);
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO: exercise 7 - isVisible = false
        // TODO: exercise 6c - unregister the broadcast receiver
        if (startedServiceBroadcastReceiver != null) {
            unregisterReceiver(startedServiceBroadcastReceiver);
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // TODO: exercise 7 - implement the onNewIntent callback method
    // get the message from the extra field of the intent
    // and display it in the messageTextView

}
