package com.anhquan.tracker_client.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.anhquan.tracker_client.R
import com.anhquan.tracker_client.common.composables.setView
import com.anhquan.tracker_client.ui.done.DoneActivity
import com.anhquan.tracker_client.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                goToNextActivity()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView {
            MainView()
        }
    }

    override fun onStart() {
        super.onStart()
        if (isPermissionGranted()) {
            goToNextActivity()
        }
    }

    @Composable
    fun MainView(modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    stringResource(R.string.grant_location_permission), modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
            FilledTonalButton(onClick = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }) {
                Text(stringResource(R.string.grant_permission))
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private fun goToNextActivity() {
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this, DoneActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}