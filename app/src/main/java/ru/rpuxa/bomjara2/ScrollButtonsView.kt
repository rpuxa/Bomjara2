package ru.rpuxa.bomjara2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ScrollButtonsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val frameWidth = 5f

    private val paint = Paint()
    private var cursorIcon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.frame)
    private lateinit var icons: Array<Bitmap>
    private var cursor = 0f

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val iconWidth = width - 2 * frameWidth
        val iconWidthInt = iconWidth.toInt()
        val withFrame = iconWidthInt + frameWidth.toInt() * 2

        var y = frameWidth
        icons.forEachIndexed { index, bitmap ->
            if (bitmap.width != iconWidthInt || bitmap.height != iconWidthInt)
                icons[index] = Bitmap.createScaledBitmap(bitmap, iconWidthInt, iconWidthInt, false)
            canvas.drawBitmap(bitmap, frameWidth, y, paint)
            y += frameWidth + iconWidth

        }
        if (cursorIcon.width != withFrame || cursorIcon.height != withFrame)
            cursorIcon = Bitmap.createScaledBitmap(cursorIcon, withFrame, withFrame, false)
        canvas.drawBitmap(cursorIcon, 0f, cursor, paint)
    }

    fun setIcons(vararg icons: Bitmap): ScrollButtonsView {
        this.icons = Array(icons.size) { icons[it] }
        invalidate()
        return this
    }
}