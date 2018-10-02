package com.kputsoftware.mileagecalculator.firebase;

/**
 * Created by duraiswa on 12/14/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kputsoftware.mileagecalculator.MainActivity;
import com.kputsoftware.mileagecalculator.R;
import com.kputsoftware.mileagecalculator.common.Affiliate;
import com.kputsoftware.mileagecalculator.library.DatabaseHandler;
import com.kputsoftware.mileagecalculator.Tipspage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static com.kputsoftware.mileagecalculator.library.variables.*;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    DatabaseHandler db = new DatabaseHandler(this);
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
      //  Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String notif;
          /*  if  (db.appsettingcheck() != 0) {
                notif = db.getnotification();
            }else {
                notif = "true";
            }*/

            notif = getSharedPreferences(MC,0).getString(NOTIFICATION,TRUE);
            if (notif.equals("true")) {

                String reason =remoteMessage.getData().get("reason");
                String title = remoteMessage.getData().get("title");
                String message =remoteMessage.getData().get("message");
                String ads = remoteMessage.getData().get("ads");
                String imageUrl =remoteMessage.getData().get("image");


                if (reason.equals("appupdate")) {

                    openplaystore(title,message);

                }else if (reason.equals("tips")) {

                    if(imageUrl != null && !imageUrl.isEmpty()){

                        Bitmap image = getBitmapfromUrl(imageUrl);
                        opentipspageimage(title, message, ads, image);
                    }else
                        {
                            opentipspage(title, message, ads);
                        }

                }else if (reason.equals("accessories")) {

                    if(imageUrl != null && !imageUrl.isEmpty()){

                        Bitmap image = getBitmapfromUrl(imageUrl);
                        openaccessoriespageimage(title, message, ads, image);
                    }else
                    {
                        openaccessoriespage(title, message, ads);
                    }

                }else if (reason.equals("mainpage")) {
                    mainpage(title,message);

                } else {

                }

            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
          //  Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void opentipspage(String title, String message, String ads) {
        Random random = new Random();
        int m = random.nextInt(1006 -1000 );
        Intent intent = new Intent(this, Tipspage.class);
        intent.putExtra("ads",ads);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.noti2);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
               // .setSmallIcon(R.drawable.noti)
                .setSmallIcon(geticon())
            //    .setLargeIcon(bitmap)
              //  .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.fornoti))
                //  .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
             // .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
           //     .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).setSummaryText(message).setBigContentTitle(title))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }
    private void opentipspageimage(String title, String message, String ads, Bitmap image) {
        Random random = new Random();
        int m = random.nextInt(1006 -1000 );

        Intent intent = new Intent(this, Tipspage.class);
        intent.putExtra("ads",ads);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.noti2);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                // .setSmallIcon(R.drawable.noti)
                .setSmallIcon(geticon())
                //    .setLargeIcon(bitmap)
                //  .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.fornoti))
                //  .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                // .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).setSummaryText(message).setBigContentTitle(title))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }
    private void openaccessoriespage(String title, String message, String ads) {
        Random random = new Random();
        int m = random.nextInt(1006 -1000 );
        Intent intent = new Intent(this, Affiliate.class);
        intent.putExtra("ads",ads);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.noti2);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                // .setSmallIcon(R.drawable.noti)
                .setSmallIcon(geticon())
                //    .setLargeIcon(bitmap)
                //  .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.fornoti))
                //  .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                // .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                //     .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).setSummaryText(message).setBigContentTitle(title))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }
    private void openaccessoriespageimage(String title, String message, String ads, Bitmap image) {
        Random random = new Random();
        int m = random.nextInt(1006 -1000 );

        Intent intent = new Intent(this, Affiliate.class);
        intent.putExtra("ads",ads);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.noti2);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                // .setSmallIcon(R.drawable.noti)
                .setSmallIcon(geticon())
                //    .setLargeIcon(bitmap)
                //  .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.fornoti))
                //  .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                // .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).setSummaryText(message).setBigContentTitle(title))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }


    private void openplaystore(String title, String message) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appmarket));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
              //  .setSmallIcon(R.drawable.noti)
                .setSmallIcon(geticon())
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }




    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message FCM message body received.
     */
    private void mainpage(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.noti2);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            // .setSmallIcon(R.drawable.noti)


             // .setLargeIcon(bitmap)
                .setSmallIcon(geticon())
               //  .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(2 /* ID of notification */, notificationBuilder.build());





        /*
         to limit number of notification
        Random random = new Random();
    int m = random.nextInt(9999 - 1000) + 1000;
       notificationManager.notify(m , notificationBuilder.build());
        */

        // one notification for on that particular time
       // int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        // notificationManager.notify(m /* ID of notification */, notificationBuilder.build());


        //   to keep only one notification
        // notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private int geticon() {
        int ic ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ic = R.drawable.newnoti;
        } else {
            ic = R.mipmap.noti2;
        }
        return ic;
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.newnoti : R.drawable.newappicon;
    }




    private Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}

