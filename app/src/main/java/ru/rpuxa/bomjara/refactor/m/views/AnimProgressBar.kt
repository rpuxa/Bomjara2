package ru.rpuxa.bomjara.refactor.m.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ProgressBar
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

class AnimProgressBar(context: Context, attributeSet: AttributeSet) : ProgressBar(context, attributeSet) {

    var animatedProgress = 0
        set(value) {
            field = value
            invalidate()
        }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (progress != animatedProgress) {
            val delta = max(abs(animatedProgress - progress) / 5, 1)
            val sgn = sign((animatedProgress - progress).toDouble()).toInt()
            var progress0 = progress + sgn * delta
            if (sgn > 0 && progress0 > animatedProgress)
                progress0 = animatedProgress
            else if (progress0 < 0)
                progress0 = 0

            progress = progress0
            invalidate()
        }
    }
}