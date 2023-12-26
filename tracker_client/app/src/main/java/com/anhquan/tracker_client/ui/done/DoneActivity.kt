package com.anhquan.tracker_client.ui.done

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.anhquan.tracker_client.TrackerService
import com.anhquan.tracker_client.common.composables.setView
import com.anhquan.tracker_client.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class DoneActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "Ứng dụng đang chạy ngầm phía dưới.",
                    textAlign = TextAlign.Center,
                )
                FilledTonalButton(onClick = {
                    logout()
                }) {
                    Text("Đăng xuất")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startForegroundService(Intent(this, TrackerService::class.java))
    }

    private fun logout() {
        stopService(Intent(this, TrackerService::class.java))
        Firebase.auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}