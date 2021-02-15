package com.example.barcelonaTeam;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * This class is for the foreground service as required in guidelines
 */
public class GameForegroundService extends Service {
    public static int NOTIFICATION_ID1 = 12;
    private NotificationManager mNotiMgr;
    private Notification.Builder mNotifyBuilder;
    private Long days;
    private long hours,minutes;
    /*
    This function only run once and it is to initialize the notification channel and start the service
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initForeground();
    }

    /*
    This function is for the functionality of the service
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
                @Override
                public void run() {
                    //The service is always running
                    while (true) {
                    //Check the flag of the system to avoid notification when not wanted
                    if(Utilities.flagForegroundNotification){
                        //Sleep one second between each notification
                        try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                        //Get the date and time of now
                        LocalDate today = LocalDate.now();
                        LocalTime now = LocalTime.now();
                        //Go over all the matches in the system
                        for (Game match : Utilities.matches) {
                            LocalDate dateMatch = match.getDate();
                            LocalTime timeMatch = match.getTime();

                            //Check if the match is today or at a later date
                            if (dateMatch.isAfter(today) || dateMatch.equals(today)) {
                                //Calculate the time until the game
                                days = today.until(dateMatch, ChronoUnit.DAYS);
                                hours=now.until(timeMatch,ChronoUnit.HOURS);
                                minutes=now.until(timeMatch,ChronoUnit.MINUTES);
                                minutes=minutes%60;

                                //Check if the game is at a later date but the time of the match has already pass
                                if (days > 0 && now.isAfter(timeMatch)) {
                                    /*Example today is 1.1.2000 and time is 20:00 and the match is on 3.1.2000 at 15:00
                                    days will be 2 and hours will be -5 so we need to fix it to correct days and time
                                     */
                                    days--;
                                    hours+=23;
                                    minutes+=60;
                                    break;
                                } else
                                    if (!(days == 0 && now.isAfter(timeMatch))) //This is a case which the game is either at a later date and the time is correct or the time of the match is appropriate to NowTime
                                    break;
                            }
                        }
                        //Build the message to display on notification
                        String msg = "You have: " + days + " days and " + hours + " hours " + minutes + " minutes until next game";
                        //Send notification and double check the flag in case the checkbox was changed while the service was sleeping
                        if(Utilities.flagForegroundNotification)
                        updateNotification(msg);
                    }
                }}
            }).start();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /*
    This function initializes the notification channel, start the foreground service and send the first notification which is required and expected at the start of service
     */
    private void initForeground(){
        String CHANNEL_ID = "my_channel_01";
        //create channel that inside him we stream the notifications.
        if (mNotiMgr==null)
            mNotiMgr= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "My main channel",
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        //what to do when we press on the notification.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotifyBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Barca team")
                .setSmallIcon(R.mipmap.barca)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        startForeground(NOTIFICATION_ID1, updateNotification("First Test"));
    }
    /*
    Send notification
     */
    public Notification updateNotification(String details){
        mNotifyBuilder.setContentText(details).setOnlyAlertOnce(true);
        Notification noti = mNotifyBuilder.build();
        noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotiMgr.notify(NOTIFICATION_ID1, noti);
        return noti;
    }

}
