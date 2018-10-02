package ru.rpuxa.bomjara2

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View

class ScrollButtonsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var icons: Array<out Bitmap>

    fun setIcons(vararg icons: Bitmap): ScrollButtonsView {
        this.icons = icons
        return this
    }
}