package com.example.test.network.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.test.network.OnBootBroadCastReceiver
import java.time.ZoneId

class AndroidAlarmScheduler(private val context: Context): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)


    override fun triggerAlarm(item: AlarmItem) {
        val intent= Intent(context, OnBootBroadCastReceiver::class.java).apply {
            putExtra("message", item.message)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond()*1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAlarm(item: AlarmItem) {
        alarmManager.cancel {
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, OnBootBroadCastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}