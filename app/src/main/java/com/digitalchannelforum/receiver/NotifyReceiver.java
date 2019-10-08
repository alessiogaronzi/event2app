package com.digitalchannelforum.receiver;

import android.app.NotificationChannel;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import com.digitalchannelforum.drupalcon.R;
import com.digitalchannelforum.ui.activity.EventDetailsActivity;
import com.digitalchannelforum.ui.activity.HomeActivity;
import com.digitalchannelforum.utils.AlarmTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long eventId = intent.getLongExtra(AlarmTask.EXTRA_ID, -1);
        long day = intent.getLongExtra(AlarmTask.EXTRA_DAY, -1);
        String text = intent.getStringExtra(AlarmTask.EXTRA_TEXT);
        showNotification(context, eventId, day, text);
    }



    private void showNotification(Context context, long id, long day, String text) {
        String title = context.getString(R.string.dont_miss_it);
        int icon = android.R.drawable.ic_dialog_info;


        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
        intent.putExtra(EventDetailsActivity.EXTRA_DAY, day);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(icon);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setAutoCancel(true);
        builder.setContentIntent(contentIntent);
        builder.setChannelId(context.getString(R.string.channel));
        builder.setDefaults(Notification.DEFAULT_ALL);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setUpNotificationChannel(context,manager);
        manager.notify(0, builder.build());
    }
    public void setUpNotificationChannel(Context context,NotificationManager notificationManager){
        String id = context.getString(R.string.channel);
        // The user-visible name of the channel.
        CharSequence name = context.getString(R.string.channel);
        // The user-visible description of the channel.
        String description = "Notifiche push "+context.getString(R.string.app_name);
        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;

            mChannel = new NotificationChannel(id, name,importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);

            mChannel.setVibrationPattern(new long[]{100,50,100,50,100});

            notificationManager.createNotificationChannel(mChannel);

        }

    }



}
