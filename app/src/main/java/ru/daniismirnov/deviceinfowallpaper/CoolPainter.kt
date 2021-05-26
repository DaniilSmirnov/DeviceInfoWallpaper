package ru.daniismirnov.deviceinfowallpaper

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.res.Resources
import android.graphics.*
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter.formatIpAddress
import android.util.DisplayMetrics
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class CoolPainter {
    fun draw(canvas: Canvas, context: Context) {
        val paint = Paint()
        drawBackground(canvas)

        paint.color = Color.WHITE
        paint.textSize = getScreenSize().widthPixels.toFloat() / 20
        var y = (getScreenSize().heightPixels.toFloat() / 2) - paint.textSize * 7
        val x = 5F
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            canvas.drawText("Serial number: " + getSerial(), x, y, paint)
            y += paint.textSize
        }
        canvas.drawText("IP: " + getIP(context), x, y, paint)
        y += paint.textSize
        canvas.drawText("Manufacturer: " + getManufacturer(), x, y, paint)
        y += paint.textSize
        canvas.drawText("Model: " + getModel(), x, y, paint)
        y += paint.textSize
        canvas.drawText("Android " + getAndroidVersion(), x, y, paint)
        y += paint.textSize
        canvas.drawText("RAM: " + getRAMInfo(context), x, y, paint)
        y += paint.textSize
        canvas.drawText("Cores: " + getCores() + " Frequency: " + getCPUFrequency(), x, y, paint)
        y += paint.textSize

    }

    private fun drawBackground(canvas: Canvas) {
        val paint = Paint()

        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.shader = LinearGradient(
            0F,
            0F,
            0F,
            getScreenSize().heightPixels.toFloat(),
            Color.BLUE,
            Color.WHITE,
            Shader.TileMode.MIRROR
        )
        canvas.drawPaint(paint)
    }
}

@SuppressLint("MissingPermission", "HardwareIds")
fun getSerial(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Build.getSerial()
    } else {
        Build.SERIAL
    }
}

fun getModel(): String {
    return Build.MODEL
}

fun getManufacturer(): String {
    return Build.MANUFACTURER
}

fun getScreenSize(): DisplayMetrics {
    return Resources.getSystem().displayMetrics
}

fun getAndroidVersion(): String {
    return Build.VERSION.RELEASE
}

fun getIP(context: Context): String {
    val wm = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?
    return formatIpAddress(wm!!.connectionInfo.ipAddress)
}

fun getRAMInfo(context: Context): String {
    val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memInfo = ActivityManager.MemoryInfo()
    actManager.getMemoryInfo(memInfo)
    return (memInfo.totalMem / 1048576).toString() + "gb"
}

fun getCores(): String {
    return Runtime.getRuntime().availableProcessors().toString()
}

fun getCPUFrequency(): String {
    val proc = Runtime.getRuntime().exec("cat sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")
    val `is`: InputStream = proc.inputStream
    return getStringFromInputStream(`is`)
}

private fun getStringFromInputStream(`is`: InputStream): String {
    val sb = StringBuilder()
    val br = BufferedReader(InputStreamReader(`is`))
    var line: String? = null

    while (br.readLine().also { line = it } != null) {
        sb.append(line)
        sb.append("\n")
    }
    return sb.toString()
}