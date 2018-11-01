package ru.rpuxa.bomjara.views

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView

class ScaledImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, 9 * widthMeasureSpec / 16)
    }
}