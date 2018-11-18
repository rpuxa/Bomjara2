package ru.rpuxa.bomjara.utils

import android.app.Activity
import android.view.View
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjara.R


fun changeVisibility(visibility: Int, vararg views: View) {
    views.forEach { it.visibility = visibility }
}

inline fun <reified T : Activity> Activity.startActivity(enterAnim: Int, exitAnim: Int) {
    startActivity<T>()
    overridePendingTransition(enterAnim, exitAnim)
}

inline fun <reified T : Activity> Activity.startActivityFromRight() {
    startActivity<T>(R.anim.right_in, R.anim.left_out)
}

