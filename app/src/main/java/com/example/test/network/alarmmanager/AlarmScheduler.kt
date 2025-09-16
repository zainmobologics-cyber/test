package com.example.test.network.alarmmanager

interface AlarmScheduler {
    fun triggerAlarm(item: AlarmItem)

    fun cancelAlarm(item: AlarmItem)
}