package com.github.joehaivo.appinfo

import android.app.ActivityManager
import com.blankj.utilcode.util.Utils
import kotlin.math.roundToInt

object MemoryUtils {
    fun getUsedMem(): Int {
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = Utils.getApp().getSystemService(ActivityManager::class.java)
        activityManager.getMemoryInfo(memoryInfo)
        return ((memoryInfo.totalMem - memoryInfo.availMem) / 1024f / 1024f).roundToInt()
    }

    fun getTotalMem(): Int {
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = Utils.getApp().getSystemService(ActivityManager::class.java)
        activityManager.getMemoryInfo(memoryInfo)
        return (memoryInfo.totalMem / 1024f / 1024f).roundToInt()
    }
}