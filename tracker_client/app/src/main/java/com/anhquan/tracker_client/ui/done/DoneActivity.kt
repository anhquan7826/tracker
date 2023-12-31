package com.anhquan.tracker_client.ui.done

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anhquan.tracker_client.R
import com.anhquan.tracker_client.TrackerService
import com.anhquan.tracker_client.common.composables.setView
import com.anhquan.tracker_client.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class DoneActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(stringResource(id = R.string.app_name))
                        },
                    )
                },
                contentWindowInsets = ScaffoldDefaults.contentWindowInsets.add(
                    WindowInsets(
                        left = 16.dp, right = 16.dp
                    )
                ),
            ) { p ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(p)
                        .fillMaxSize()
                ) {
                    Text(
                        "Ứng dụng đang chạy ngầm phía dưới.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    Button(onClick = {
                        logout()
                    }) {
                        Text("Đăng xuất")
                    }
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