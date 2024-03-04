package com.example.kktext.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.kktext.ApiHelper.ApiController
import com.example.kktext.ApiHelper.ApiResponseListner
import com.example.kktext.DefaultLocationClient
import com.example.kktext.LocationClient
import com.example.kktext.Model.ProductDeleteBean
import com.example.kktext.R
import com.example.kktext.Utills.SalesApp
import com.example.kktext.Utills.Utility
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LocationService: Service(){
    private var lat: String?=null
    private var long: String?=null

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: ")
            .setSmallIcon(R.drawable.app_logo)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
       // val interval = 100 * 1000 // 10 seconds in milliseconds
        val interval = 3 * 60 * 1000 // 3 minutes
        locationClient
            .getLocationUpdates(interval.toLong())
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                 lat = location.latitude.toString().takeLast(3)
                 long = location.longitude.toString().takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )

                notificationManager.notify(1, updatedNotification.build())



              //  mDoThisJob()
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}