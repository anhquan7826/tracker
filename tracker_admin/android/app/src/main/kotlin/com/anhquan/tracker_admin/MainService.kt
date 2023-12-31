package com.anhquan.tracker_admin

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainService : Service() {
    abstract class MyChildEventListener : ChildEventListener {
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private val listeners: MutableMap<String, ChildEventListener> = mutableMapOf()

    private val deviceDistances: MutableMap<String, Pair<Double, Double>> = mutableMapOf()

    private var loaded = false

    private lateinit var spf: SharedPreferences

    private var notificationState: String = "all"

    private var distance: Int = 0

    override fun onCreate() {
        super.onCreate()
        spf = getSharedPreferences("com.anhquan.tracker_admin", Context.MODE_PRIVATE)
        NotificationUtil.configure(this)
        startForeground(1, NotificationUtil.getPersistentNotification(this))
        delay(timeMillis = 1000) {
            loaded = true
        }
        notificationState = spf.getString("notification", "all")!!
        distance = spf.getInt("distance", 0)
        listen()
        Log.i("MainService", "Service created!")
    }

    private fun delay(timeMillis: Long = 500, callback: () -> Unit): Disposable {
        return Observable.timer(timeMillis, TimeUnit.MILLISECONDS).subscribe {
            callback.invoke()
        }
    }

    private fun listen() {
        FirebaseDatabase.getInstance().reference.addChildEventListener(object :
            MyChildEventListener() {
            init {
                listeners["/"] = this
            }

            override fun onChildAdded(user: DataSnapshot, previousChildName: String?) {
                user.ref.child("/devices").addChildEventListener(object : MyChildEventListener() {
                    init {
                        listeners["/${user.key}/devices"] = this
                    }

                    override fun onChildAdded(device: DataSnapshot, previousChildName: String?) {
                        val deviceName = device.child("name").value.toString()
                        val deviceId = device.key!!
                        device.ref.child("history")
                            .addChildEventListener(object : MyChildEventListener() {
                                init {
                                    listeners["/${user.key}/devices/${device.key}/history"] = this
                                }

                                override fun onChildAdded(
                                    history: DataSnapshot, previousChildName: String?
                                ) {
                                    if (loaded) {

                                        val lat = history.child("lat").value.toString().toDouble()
                                        val lon = history.child("lon").value.toString().toDouble()
                                        sendNotification(deviceId, deviceName, lat, lon)
                                        deviceDistances[deviceId] = Pair(lat, lon)
                                    }
                                }
                            })
                    }
                })
            }
        })
    }


    fun sendNotification(deviceId: String, deviceName: String, lat: Double, lon: Double) {
        if (notificationState == "none") {
            return
        }
        if (notificationState == "partial") {
            deviceDistances[deviceId]?.let {
                if (CoordinateUtil.distance(it, Pair(lat, lon)) > distance) {
                    NotificationUtil.showDeviceLocationNotification(
                        this@MainService,
                        deviceName = deviceName,
                        lat = lat,
                        lon = lon
                    )
                }
            }
        } else {
            NotificationUtil.showDeviceLocationNotification(
                this@MainService,
                deviceName = deviceName,
                lat = lat,
                lon = lon
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listeners.forEach {
            FirebaseDatabase.getInstance().getReference(it.key).removeEventListener(it.value)
        }
        Log.i("MainService", "Service destroyed!")
    }
}