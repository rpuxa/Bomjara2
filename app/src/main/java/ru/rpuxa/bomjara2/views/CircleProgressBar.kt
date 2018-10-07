package ru.rpuxa.bomjara2.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CircleProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val circleWidth = 15f
    private val paint = Paint().apply {
        color = Color.CYAN
    }
    private var progress = .5f

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val w = width / 2f
        val h = height / 2f
        val r = Math.min(w, h)
        val path = Path()
        path.moveTo(w, 0f)
        val max = 50
        var current = 0.0
        for (i in 0..max) {
            val percent = i / max.toFloat()
            if (percent > progress)
                break
            val angle = percent * 2 * PI
            path.lineTo(w + r * sin(angle).toFloat(), h + r * -cos(angle).toFloat())
            current = angle
        }
        while (current > 0) {
            path.lineTo(w + (r - circleWidth) * sin(current).toFloat(), h + (r - circleWidth) * -cos(current).toFloat())
            current -= PI / 25
        }
        path.lineTo(w, circleWidth)
        path.lineTo(w, 0f)
        path.close()

        canvas.drawPath(path, paint)
    }

    var started = false

    fun start(duration: Int, callback: () -> Unit) {
        if (started)
            return
        started = true
        val anim = ValueAnimator.ofFloat(0f, 1.1f)
        anim.duration = duration.toLong()
        anim.addUpdateListener {
            progress = it.animatedValue as Float
            invalidate()
        }

        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                started = false
                callback()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        anim.start()
    }
}