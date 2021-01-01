package com.example.viewpager2

import android.util.Log


object Logger {
    fun log(tag: String?, content: String) {
        if (content.length > 4000) {
            Log.i(tag, content.substring(0, 4000))
            log(tag, content.substring(4000))
        } else {
            Log.i(tag, content)
        }
    }
}