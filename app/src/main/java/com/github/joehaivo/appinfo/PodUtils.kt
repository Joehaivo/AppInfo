package com.github.joehaivo.appinfo

import android.annotation.SuppressLint
import android.util.Log
import com.android.server.dexguard.IDexGuardClient

object PodUtils {

    const val TAG = "CloudDeviceUtils"

    fun isByteDanceVm(): Boolean {
        return kotlin.runCatching {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            get.invoke(c, "ro.bd.product.model") as? String
        }.getOrNull() == "gemini"
    }

    fun getPodCode(): String {
        return if (isByteDanceVm()) getByteDancePodCode() else getBaiduPodCode()
    }

    private fun getByteDancePodCode(): String {
        return kotlin.runCatching {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            get.invoke(c, "ro.serialno") as String
        }.getOrDefault("")
    }

    private fun getBaiduPodCode(): String {
        val ip = IDexGuardClient.getDeviceIp()
        val ips = ip.split('.').joinToString("") { String.format("%03d", it.toIntOrNull()) }
        return "VM$ips"
    }

    @SuppressLint("PrivateApi")
    fun getSystemProperties(properties: String): String {
        var systemProperties: String = ""
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            systemProperties = get.invoke(c, properties) as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return systemProperties
    }
}