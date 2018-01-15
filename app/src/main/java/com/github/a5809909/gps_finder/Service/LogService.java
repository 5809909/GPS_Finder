package com.github.a5809909.gps_finder.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.github.a5809909.gps_finder.Activity.MainActivity;
import com.github.a5809909.gps_finder.R;
import com.github.a5809909.gps_finder.Utilities.Constants;

public class LogService extends Service {

    private static final String LOG_TAG = "LogService";
    private boolean isRun;

    @Override
    public void onCreate() {
        super.onCreate();
        while (true) {
            try {
                Thread.sleep(5000);
                getLocationClicked();

            } catch (InterruptedException ie) {
            }

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            isRun = true;
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_pin_drop);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("GPS Finder")
                    .setTicker("Truiton Music Player")
                    .setContentText("My GPS")
                    .setSmallIcon(R.drawable.map_marker)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true).build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            isRun = false;
            stopForeground(true);
            stopSelf();

        }

        return START_STICKY;
    }

    public void getLocationClicked() {

        TelephonyManager tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (tel != null) {
            CellLocation loc = tel.getCellLocation();

            if ((loc != null) && (loc instanceof GsmCellLocation)) {
                GsmCellLocation gsmLoc = (GsmCellLocation) loc;
                String op = tel.getNetworkOperator();

                String cid = "" + gsmLoc.getCid();
                String lac = "" + gsmLoc.getLac();
                String mcc = op.substring(0, 3);
                String mnc = op.substring(3);
                Log.i(LOG_TAG, "onStartCommand: 5000 ms");

            } else {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}
