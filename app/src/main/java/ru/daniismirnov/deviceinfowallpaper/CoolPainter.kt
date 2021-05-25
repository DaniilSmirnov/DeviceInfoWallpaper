package ru.daniismirnov.deviceinfowallpaper

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.util.DisplayMetrics


class CoolPainter {
    fun draw(canvas: Canvas) {
        val paint = Paint()
        drawBackground(canvas)

        paint.color = Color.WHITE
        paint.textSize = getScreenSize().widthPixels.toFloat() / 20
        var y = getScreenSize().heightPixels.toFloat() / 10
        val x = 5F
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            canvas.drawText("Serial number: " + getSerial(), x, y, paint)
            y += paint.textSize
        }
        canvas.drawText("Manufacturer: " + getManufacturer(), x, y, paint)
        y += paint.textSize
        canvas.drawText("Model: " + getModel(), x, y, paint)
        y += paint.textSize
        canvas.drawText("Android " + getAndroidVersion(), x, y, paint)


    }

    private fun drawBackground(canvas: Canvas){
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