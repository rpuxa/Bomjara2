package ru.rpuxa.bomjara.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.Data


fun Activity.toast(msg: String, isShort: Boolean = true) {
    runOnUiThread {
        Toast.makeText(this, msg, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }
}

fun Fragment.toast(msg: String, isShort: Boolean = true) =
        activity.toast(msg, isShort)

fun Activity.toast(@StringRes id: Int, isShort: Boolean = true) = toast(getString(id), isShort)

fun Fragment.toast(@StringRes id: Int, isShort: Boolean = true) = toast(getString(id), isShort)


fun changeVisibility(visibility: Int, vararg views: View) {
    views.forEach { it.visibility = visibility }
}

inline fun <reified T : Activity> Activity.startActivity() =
        startActivity(Intent(this, T::class.java))

inline fun <reified T : Activity> Activity.startActivity(enterAnim: Int, exitAnim: Int) {
    startActivity<T>()
    overridePendingTransition(enterAnim, exitAnim)
}

inline fun <reified T : Activity> Activity.startActivityFromRight() {
    startActivity<T>(R.anim.right_in, R.anim.left_out)
}

fun Context.browser(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

fun Context.browser(@StringRes res: Int) = browser(getString(res))

