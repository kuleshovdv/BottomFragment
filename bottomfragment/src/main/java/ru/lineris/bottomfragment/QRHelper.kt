package ru.lineris.bottomfragment

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

object QRHelper {
    fun buildQRcode(value: String,
                    size: Int = 400,
                    mainColor: Int = Color.BLACK,
                    backgroundColor: Int = Color.WHITE): Bitmap? {
        val multiFormatWriter = MultiFormatWriter()
        return try {
            val bitMatrix = multiFormatWriter.encode(
                value,
                BarcodeFormat.QR_CODE,
                size,
                size)
            createBitmap(bitMatrix, mainColor, backgroundColor)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    private fun createBitmap(matrix: BitMatrix, mainColor: Int, backgroundColor: Int): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] =
                    if (matrix[x, y]) mainColor else backgroundColor
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}