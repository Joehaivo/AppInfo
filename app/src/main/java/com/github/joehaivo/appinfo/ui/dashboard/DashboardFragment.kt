package com.github.joehaivo.appinfo.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ShellUtils
import com.github.joehaivo.appinfo.PodUtils
import com.github.joehaivo.appinfo.MemoryUtils
import com.github.joehaivo.appinfo.databinding.FragmentDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.root.setOnTouchListener { v, event ->
//            binding.tvTouch.text = event.toString()

            return@setOnTouchListener true
        }
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            binding.tvTouch.text = """
                ro.product.cpu.abi=${PodUtils.getSystemProperties("ro.product.cpu.abi")}
                ro.product.cpu.abi2=${PodUtils.getSystemProperties("ro.product.cpu.abi2")}
                ro.product.cpu.abilist=${PodUtils.getSystemProperties("ro.product.cpu.abilist")}
                ro.product.cpu.abilist32=${PodUtils.getSystemProperties("ro.product.cpu.abilist32")}
                ro.product.cpu.abilist64=${PodUtils.getSystemProperties("ro.product.cpu.abilist64")}
                ro.product.cpu.abi=${PodUtils.getSystemProperties("ro.product.cpu.abi")}
                ro.product.cpu.abilist=${PodUtils.getSystemProperties("ro.product.cpu.abilist")}
                ro.product.cpu.abilist32=${PodUtils.getSystemProperties("ro.product.cpu.abilist32")}
                ro.product.cpu.abilist64=${PodUtils.getSystemProperties("ro.product.cpu.abilist64")}
                ro.vendor.product.cpu.abilist=${PodUtils.getSystemProperties("ro.vendor.product.cpu.abilist")}
                ro.vendor.product.cpu.abilist32=${PodUtils.getSystemProperties("ro.vendor.product.cpu.abilist32")}
                ro.vendor.product.cpu.abilist64=${PodUtils.getSystemProperties("ro.vendor.product.cpu.abilist64")}
                ro.dalvik.vm.isa.arm=${PodUtils.getSystemProperties("ro.dalvik.vm.isa.arm")}
                ro.dalvik.vm.isa.arm64=${PodUtils.getSystemProperties("ro.dalvik.vm.isa.arm64")}
            """.trimIndent()

            binding.tvPadCode.text = "PadCode: ${PodUtils.getPodCode()}"

            binding.tvBrand.text = "型号：${DeviceUtils.getModel()}\n制造商：${DeviceUtils.getManufacturer()}"

            val commandResult = withContext(Dispatchers.IO) {
                ShellUtils.execCmd("ls", true)
            }
            binding.tvRoot.text = if (commandResult.result == 0) "ROOT: Yes" else "ROOT: No"

            binding.tvDisplay.text = "App分辨率：${ScreenUtils.getAppScreenWidth()} x ${ScreenUtils.getAppScreenHeight()} \n" +
                    "屏幕分辨率：${ScreenUtils.getScreenWidth()} x ${ScreenUtils.getScreenHeight()} \n" +
                    "屏幕密度: ${ScreenUtils.getScreenDensityDpi()}dpi, 比率：${ScreenUtils.getScreenDensity()}" +
                    ""

            val cpuR = withContext(Dispatchers.IO) {
                ShellUtils.execCmd("cat /proc/cpuinfo", false)
            }
//            binding.tvCpuinfo.text = "/proc/cpuinfo"
            binding.tvCpuinfo.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("/proc/cpuinfo")
                    .setMessage(cpuR.successMsg)
                    .show()
            }

            val memR = withContext(Dispatchers.IO) {
                ShellUtils.execCmd("cat /proc/meminfo", false)
            }
            binding.tvMeminfo.text = "/proc/meminfo ${MemoryUtils.getUsedMem()}MB/${MemoryUtils.getTotalMem()}MB"
            binding.tvMeminfo.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("/proc/meminfo")
                    .setMessage(memR.successMsg)
                    .show()
            }

            val gpuR = withContext(Dispatchers.IO) {
                ShellUtils.execCmd("dumpsys SurfaceFlinger | grep \"GLES:\"", true)
            }
            binding.tvGpu.text = "GPU: ${gpuR.successMsg}"

            binding.tvUa.text = "UA: ${System.getProperty("http.agent")}"

            try {
                val manager = requireContext().getSystemService(Context.TELEPHONY_SERVICE)
                val method = manager.javaClass.getMethod("getImei", Int::class.javaPrimitiveType)
                val imei = method.invoke(manager, 0) as String


                binding.tvImei.text = "imei: $imei, getSerial: ${Build.getSerial()}, ID: ${Build.ID}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}