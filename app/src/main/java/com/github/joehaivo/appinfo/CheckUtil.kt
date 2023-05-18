package com.github.joehaivo.appinfo

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object CheckUtil {

    const val SHA1 = "SHA1"

    /**
     * 返回一个签名的对应类型的字符串
     *
     * @param context
     * @param packageName
     * @param type
     *
     * @return
     */
    @JvmStatic
    fun getSingInfo(context: Context, packageName: String?, type: String): String? {
        var tmp: String? = null
        val signs = getSignatures(context, packageName)
        for (sig in signs!!) {
            if (SHA1 == type) {
                tmp = getSignatureString(sig, SHA1)
                break
            }
        }
        return tmp
    }

    /**
     * 返回对应包的签名信息
     *
     * @param context
     * @param packageName
     *
     * @return
     */
    private fun getSignatures(context: Context, packageName: String?): Array<Signature>? {
        if (packageName.isNullOrEmpty()){
            return null
        }
        try {
            val packageInfo: PackageInfo? =
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            return packageInfo?.signatures
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取相应的类型的字符串（把签名的byte[]信息转换成16进制）
     *
     * @param sig
     * @param type
     *
     * @return
     */
    fun getSignatureString(sig: Signature, type: String): String? {
        val hexBytes = sig.toByteArray()
        var fingerprint: String? = null
        try {
            val digest = MessageDigest.getInstance(type)
            val digestBytes = digest.digest(hexBytes)
            val sb = StringBuilder()
            for (digestByte in digestBytes) {
                sb.append(Integer.toHexString(digestByte.toInt() and 0xFF or 0x100).substring(1, 3))
            }
            fingerprint = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return fingerprint
    }
}


