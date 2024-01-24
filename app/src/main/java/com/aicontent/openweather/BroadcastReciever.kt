package com.aicontent.openweather

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

class BroadcastReceiverAirPlane : BroadcastReceiver() {

    //    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
//        val initialCode = resultCode
//        val initialData = resultData
//
//        // Do something with the received data or modify it
//        val newData = "Modified Data: $initialData"
//
//        // Set the new result code and data
//        resultCode = Activity.RESULT_OK
//        resultData = newData
//
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isTurnOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON
            ) != 0
            Toast.makeText(context, "air plan mode is $isTurnOn", Toast.LENGTH_LONG).show()

        }

    }
}