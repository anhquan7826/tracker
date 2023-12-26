package com.anhquan.tracker_client.utils

import java.util.UUID

object IDUtil {
    fun generateId(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}