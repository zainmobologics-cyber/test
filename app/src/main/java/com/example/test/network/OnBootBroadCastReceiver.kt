package com.example.test.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OnBootBroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Intent(context, AudioService::class.java).also {
                it.action= AudioService.Actions.START.toString()
                context?.startService(it)
            }

        }
    }
}