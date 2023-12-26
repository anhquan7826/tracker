package com.anhquan.tracker_client.common.composables

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.anhquan.tracker_client.common.theme.TrackerClientTheme

fun ComponentActivity.setView(content: @Composable () -> Unit) {
    setContent {
        TrackerClientTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                content()
            }
        }
    }
}