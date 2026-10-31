package ro.pub.cs.systems.eim.lab05.startedserviceactivity.view;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab05.startedserviceactivity.R;
import ro.pub.cs.systems.eim.lab05.startedserviceactivity.general.Constants;

public class StartedServiceActivity extends AppCompatActivity {

    // TODO: exercise 7 - read by the receiver declared in the manifest
    public static boolean isVisible = false;

    private TextView messageTextView;
    private StartedServiceBroadcastReceiver startedServiceBroadcastReceiver;
    private IntentFilter startedServiceIntentFilter;
    private boolean serviceStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started_service);

        messageTextView = findViewById(R.id.message_text_view);
        messageTextView.setMovementMethod(new ScrollingMovementMethod());

        // TODO: exercise 6a - create an instance of the StartedServiceBroadcastReceiver broadcast receiver
        startedServiceBroadcastReceiver = new StartedServiceBroadcastReceiver(messageTextView);

        // TODO: exercise 6b - create an instance of an IntentFilter
        // with all available actions contained within the broadcast intents sent by the service
        startedServiceIntentFilter = new IntentFilter();
        startedServiceIntentFilter.addAction(Constants.ACTION_STRING);
        startedServiceIntentFilter.addAction(Constants.ACTION_INTEGER);
        startedServiceIntentFilter.addAction(Constants.ACTION_ARRAY_LIST);

        // TODO: exercise 7 - request the permission to post notifications (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    Constants.PERMISSION_REQUEST_POST_NOTIFICATIONS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        
        // TODO: exercise 6c - register the broadcast receiver with the corresponding intent filter
        // Register with RECEIVER_EXPORTED for cross-app broadcasts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter, Context.RECEIVER_EXPORTED);
        }

        // TODO: exercise 4 - start the service AFTER registering the receiver
        if (!serviceStarted) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("ro.pub.cs.systems.eim.lab05.startedservice", "ro.pub.cs.systems.eim.lab05.startedservice.service.StartedService"));
            startForegroundService(intent);
            serviceStarted = true;
        }
    }

    @Override
    protected void onPause() {
        // TODO: exercise 6c - unregister the broadcast receiver
        unregisterReceiver(startedServiceBroadcastReceiver);
        isVisible = false;

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO: exercise 6d - stop the service
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("ro.pub.cs.systems.eim.lab05.startedservice", "ro.pub.cs.systems.eim.lab05.startedservice.service.StartedService"));
        stopService(intent);

        super.onDestroy();
    }

    // TODO: exercise 7 - implement the onNewIntent callback method
    // get the message from the extra field of the intent
    // and display it in the messageTextView
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String message = intent.getStringExtra(Constants.MESSAGE);
        if (message != null) {
            messageTextView.setText(messageTextView.getText().toString() + "\n" + message);
        }
    }

}
