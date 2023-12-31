package com.anhquan.tracker_client

import android.Manifest
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.anhquan.tracker_client.model.History
import com.anhquan.tracker_client.utils.NotificationUtil
import com.anhquan.tracker_client.utils.now
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TrackerService : Service() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val database = Firebase.database.reference
    private val uid = Firebase.auth.currentUser!!.uid
    private lateinit var deviceId: String

    private val minTimeInterval = 300 * 1000L

    private var initiated = false

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let {
                try {
                    if (initiated) onLocationUpdate(it.latitude, it.longitude)
                } catch (_: Exception) {
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LocationManager::class.java)
        notificationManager = getSystemService(NotificationManager::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        NotificationUtil.configure(this)
        startForeground(1, NotificationUtil.getPersistentNotification(applicationContext))
        initField()
        trackLocation()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    private fun initField() {
        database.child("$uid/email").setValue(Firebase.auth.currentUser!!.email!!)
        val sp = getSharedPreferences("tracker-client", Context.MODE_PRIVATE)
        if (sp.contains("id")) {
            deviceId = sp.getString("id", "").toString()
        } else {
            val ref = database.child("$uid/devices").push()
            deviceId = ref.key!!
            sp.edit().putString("id", deviceId).apply()
        }
        database.child("$uid/devices/$deviceId/name").setValue(Build.MODEL)
        initiated = true
    }

    private fun trackLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, minTimeInterval).build(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun onLocationUpdate(latitude: Double, longitude: Double) {
        val time = now()
        database.child("$uid/devices/$deviceId/history").get().addOnSuccessListener {
            it.ref.child(it.childrenCount.toString()).setValue(
                History(
                    time = time,
                    lat = latitude,
                    lon = longitude
                )
            )
        }
    }

    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }
}