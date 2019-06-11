package com.jidogoon.rnhostandroidapp.rn

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class RNCrossInterface(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {
    override fun getName(): String = "HostAppInterface"

    @ReactMethod
    fun logError(tag: String?, message: String?) {
        Log.e(tag, message)
    }
}