package ru.rpuxa.bomjara.api.actions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

interface HasIcon {
    val iconId: Int

    fun getIcon(context: Context?): Bitmap =
            BitmapFactory.decodeResource(context!!.resources, iconId)
}