package ro.pub.cs.systems.eim.lab05.startedserviceactivity.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab05.startedserviceactivity.general.Constants;

public class StartedServiceBroadcastReceiver extends BroadcastReceiver {

    private TextView messageTextView;

    // TODO: exercise 7 - default constructor
    public StartedServiceBroadcastReceiver() {
        this.messageTextView = null;
    }

    public StartedServiceBroadcastReceiver(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: exercise 5 - get the action and the extra information from the intent
        // and set the text on the messageTextView

        String action = intent.getAction();
        Log.d(Constants.TAG, "onReceive called with action: " + action);
        String data = null;
        if (Constants.ACTION_STRING.equals(action)) {
            data = intent.getStringExtra(Constants.DATA);
        }
        if (Constants.ACTION_INTEGER.equals(action)) {
            data = String.valueOf(intent.getIntExtra(Constants.DATA, -1));
        }
        if (Constants.ACTION_ARRAY_LIST.equals(action)) {
            java.util.ArrayList<String> arrayList = intent.getStringArrayListExtra(Constants.DATA);
            if (arrayList != null) {
                data = arrayList.toString();
            }
        }
        if (messageTextView != null && data != null) {
            String currentText = messageTextView.getText() != null ? messageTextView.getText().toString() : "";
            messageTextView.setText(currentText + (currentText.isEmpty() ? "" : "\n") + data);
            Log.d(Constants.TAG, "Updated TextView with data: " + data);
        } else {
            Log.d(Constants.TAG, "messageTextView is null or data is null. TextView: " + (messageTextView != null) + ", data: " + data);
        }

        // TODO: exercise 7 - if the activity is not visible,
        // post a notification that opens the activity with the message
        if (messageTextView == null && data != null && !StartedServiceActivity.isVisible) {
            showNotification(context, data);
        }
    }

    private void showNotification(Context context, String message) {
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(
                new NotificationChannel(Constants.CHANNEL_ID, "Started Service", NotificationManager.IMPORTANCE_DEFAULT));

        Intent startedServiceActivityIntent = new Intent(context, StartedServiceActivity.class);
        startedServiceActivityIntent.putExtra(Constants.MESSAGE, message);
        startedServiceActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                startedServiceActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new Notification.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Started Service")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }
}
