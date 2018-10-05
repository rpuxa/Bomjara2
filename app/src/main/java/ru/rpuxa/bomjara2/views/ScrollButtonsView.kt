package ru.rpuxa.bomjara2.views

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
import ru.rpuxa.bomjara2.R
import kotlin.math.*

class ScrollButtonsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val frameWidth = 5f
    private val cursorSpeed = 10f

    private var iconWidth = width - 2 * frameWidth
    private val paint = Paint()
    private var cursorIcon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.frame)
    private var icons: Array<Bitmap> = emptyArray()
    private var drugging = false
    private var cursorStartDrugging = 0f
    private var _cursor = 0f
    fun setCursor(value: Float, fromPager: Boolean) {
        _cursor = value
        if (!fromPager) {
            val pager = controlViewPager
            if (pager != null) {
                pager.beginFakeDrag()
                val current = (cursorStartDrugging - value) * pager.width / (iconWidth + frameWidth)
                pager.fakeDragBy(current)
                cursorStartDrugging = value
            }
        } else {
            cursorTarget = value
        }
    }

    private var cursorTarget = 0f

    private var controlViewPager: ViewPager? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        iconWidth = w - 2 * frameWidth
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
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
        canvas.drawBitmap(cursorIcon, 0f, _cursor, paint)

        if (_cursor != cursorTarget) {
            val delta = cursorTarget - _cursor
            val pos = _cursor + sign(delta) * max(abs(delta) * cursorSpeed / iconWidth, cursorSpeed / 4)
            setCursor(if (delta > 0) min(cursorTarget, pos) else max(cursorTarget, pos), false)
            invalidate()
        } else if (!drugging) {
            controlViewPager.endDrag()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val iconWithFrame = iconWidth + frameWidth
        var touchY = event.y - iconWidth / 2
        if (touchY < 0)
            touchY = 0f
        val maxY = iconWithFrame * (icons.size - 1)
        if (touchY > maxY)
            touchY = maxY

        cursorTarget = when (event.action) {
            MotionEvent.ACTION_UP -> {
                drugging = false
                round(touchY / iconWithFrame) * iconWithFrame
            }
            else -> {
                drugging = true
                touchY
            }
        }
        if (cursorTarget != _cursor)
            invalidate()
        return true
    }

    fun setIcons(vararg icons: Int): ScrollButtonsView {
        this.icons = Array(icons.size) { BitmapFactory.decodeResource(resources, icons[it])!! }
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
                if (!viewPager.isFakeDragging) {
                    val full = iconWidth + frameWidth
                    setCursor((position + positionOffset) * full, true)
                    invalidate()
                }
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    private fun ViewPager?.endDrag() {
        if (this != null && isFakeDragging)
            endFakeDrag()
        cursorStartDrugging = _cursor
    }
}