package com.example.kktext.Activity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class UploadWorker  extends Service {

    Handler handler = new Handler();

    public UploadWorker() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler.postDelayed(new Runnable() {
            public void run() {

                /*
                 * code will run every 15 minutes
                 */

                handler.postDelayed(this, 15 * 60 * 1000); //now is every 15 minutes
            }

        }, 0);

        return START_STICKY;
    }
}

