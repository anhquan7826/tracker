package com.anhquan.tracker_client.model

data class Device(
    val name: String,
    val history: List<History>
)