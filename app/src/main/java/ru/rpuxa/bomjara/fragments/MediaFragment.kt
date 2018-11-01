package ru.rpuxa.bomjara.fragments

import android.view.View
import kotlinx.android.synthetic.main.media_fragment.*
import ru.rpuxa.bomjara.R

class MediaFragment : CacheFragment() {
    override val layout = R.layout.media_fragment

    override fun onChange(view: View) {
        enter_media.setOnClickListener {

        }
    }
}