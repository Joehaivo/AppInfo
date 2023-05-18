package com.github.joehaivo.appinfo.ui.home

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.Utils
import com.github.joehaivo.appinfo.CheckUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    suspend fun getAllAppTrait(): List<AppTrait> {
        val traits = withContext(Dispatchers.IO) {
            var flags = (PackageManager.GET_META_DATA
                    or PackageManager.GET_ACTIVITIES
                    or PackageManager.GET_RECEIVERS
                    or PackageManager.GET_SERVICES
                    or PackageManager.GET_PROVIDERS
                    )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                flags = flags or PackageManager.GET_SIGNING_CERTIFICATES
            }
            val packageInfos = Utils.getApp().packageManager.getInstalledPackages(flags)
            packageInfos.map { info ->
                AppTrait().apply {
                    appName = info?.applicationInfo?.loadLabel(Utils.getApp().packageManager)?.toString()
                    packageName = info?.packageName
                    serviceNames = info?.services?.map { it.name }
                    activityNames = info?.activities?.map { it.name }
                    receiverNames = info?.receivers?.map { it.name }
                    providerNames = info?.providers?.map { it.name }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        this.signatureMD5s = info?.signingInfo?.apkContentsSigners?.map {
                            CheckUtil.getSignatureString(it, "MD5") ?: ""
                        }
                    }
                }
            }
        }
        return traits
    }
}

@Keep
class AppTrait {
    var appName: String? = null
    var packageName: String? = null
    var serviceNames: List<String>? = null
    var activityNames: List<String>? = null
    var receiverNames: List<String>? = null
    var providerNames: List<String>? = null
    var signatureMD5s: List<String>? = null
}