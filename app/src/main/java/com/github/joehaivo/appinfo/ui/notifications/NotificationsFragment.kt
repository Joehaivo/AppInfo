package com.github.joehaivo.appinfo.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.joehaivo.appinfo.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        // 5.1以上默认禁止了https和http混用，以下方式是开启
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.loadWithOverviewMode = true
        webSettings.saveFormData = true
        webSettings.domStorageEnabled = true
        webSettings.setSupportZoom(false)
        webSettings.displayZoomControls = false
        webSettings.javaScriptCanOpenWindowsAutomatically = false
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = Charsets.UTF_8.name()
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true

        binding.webview.loadUrl("file:///android_asset/preview.html")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}