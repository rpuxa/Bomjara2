package ru.rpuxa.bomjara.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes

interface HasIcon {
    val iconId: Int

    fun getIcon(context: Context): Bitmap =
            BitmapFactory.decodeResource(context.resources, iconId)
}