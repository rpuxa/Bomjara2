package ru.rpuxa.bomjara.fragments

import android.view.View
import kotlinx.android.synthetic.main.media_fragment.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.activities.MediaActivity
import ru.rpuxa.bomjara.startActivity

class MediaFragment : CacheFragment() {
    override val layout = R.layout.media_fragment

    override fun onChange(view: View) {
        enter_media.setOnClickListener {
            activity.startActivity<MediaActivity>()
        }
    }
}