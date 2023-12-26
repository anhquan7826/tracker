package com.anhquan.tracker_client

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.anhquan.tracker_client.utils.IDUtil

class MainApplication : Application() {
    private lateinit var pf: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        pf = getSharedPreferences("tracker-client", Context.MODE_PRIVATE)
        if (!pf.contains("id")) {
            pf.edit().putString("id", IDUtil.generateId()).apply()
        }
    }
}