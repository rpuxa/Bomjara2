package ru.rpuxa.bomjara.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.rpuxa.bomjara.R
import kotlin.math.abs
import kotlin.math.round

class ScrollButtonsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val frameWidth = 5f

    private var iconWidth = width - 2 * frameWidth
    private val paint = Paint()
    private var cursorIcon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.frame)
    private var icons: Array<Bitmap> = emptyArray()
    private var cursor = 0f
    private var coloredIcons: Array<Bitmap> = emptyArray()


    private var controlViewPager: ViewPager? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        iconWidth = w - 2 * frameWidth
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        var y = frameWidth
        icons.forEachIndexed { index, bitmap ->
            val iconWidthInt = (iconWidth * 0.7).toInt()
            //val withFrame = iconWidthInt + frameWidth.toInt() * 2

            if (bitmap.width != iconWidthInt || bitmap.height != iconWidthInt) {
                icons[index] = Bitmap.createScaledBitmap(bitmap, iconWidthInt, iconWidthInt, false)
                coloredIcons[index] = Bitmap.createScaledBitmap(coloredIcons[index], iconWidthInt, iconWidthInt, false)
            }
            canvas.drawBitmap(
                    if (abs(cursor / (iconWidth + frameWidth) - index) < .01f) coloredIcons[index] else icons[index],
                    frameWidth + iconWidth * 0.15f,
                    y + iconWidth * 0.15f,
                    paint
            )

            y += frameWidth + iconWidth

        }

        val iconWidthInt = iconWidth.toInt()
        val withFrame = iconWidthInt + frameWidth.toInt() * 2

        if (cursorIcon.width != withFrame || cursorIcon.height != withFrame)
            cursorIcon = Bitmap.createScaledBitmap(cursorIcon, withFrame, withFrame, false)
        canvas.drawBitmap(cursorIcon, 0f, cursor, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val iconWithFrame = iconWidth + frameWidth
        var touchY = event.y - iconWidth / 2
        if (touchY < 0)
            touchY = 0f
        val maxY = iconWithFrame * (icons.size - 1)
        if (touchY > maxY)
            touchY = maxY

        if (event.action == MotionEvent.ACTION_UP) {
            val n = round(touchY / iconWithFrame)
            cursor = n * iconWithFrame
            controlViewPager?.currentItem = n.toInt()
        }

        return true
    }

    fun setIcons(vararg icons: Int): ScrollButtonsView {
        this.icons = Array(icons.size) { BitmapFactory.decodeResource(resources, icons[it])!! }
        invalidate()
        return this
    }

    fun setColoredIcons(vararg icons: Int): ScrollButtonsView {
        coloredIcons = Array(icons.size) { BitmapFactory.decodeResource(resources, icons[it])!! }
        invalidate()
        return this
    }

    fun setViewPager(viewPager: ViewPager) {
        controlViewPager = viewPager
        if (viewPager.adapter.count != icons.size)
            throw IllegalStateException("sizes not same!!")
        invalidate()
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val full = iconWidth + frameWidth
                cursor = (position + positionOffset) * full
                invalidate()
            }


            override fun onPageSelected(position: Int) {
            }
        })
    }
}