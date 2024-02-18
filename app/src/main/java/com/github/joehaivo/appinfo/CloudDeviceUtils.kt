package com.github.joehaivo.appinfo

import com.android.server.dexguard.IDexGuardClient

object CloudDeviceUtils {

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


}