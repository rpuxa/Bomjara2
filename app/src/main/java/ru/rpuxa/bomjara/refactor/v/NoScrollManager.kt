package ru.rpuxa.bomjara.refactor.v

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NoScrollManager(context: Context?) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}
