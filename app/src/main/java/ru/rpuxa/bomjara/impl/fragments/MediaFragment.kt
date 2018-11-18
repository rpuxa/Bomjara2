package ru.rpuxa.bomjara.impl.fragments

import android.view.View
import kotlinx.android.synthetic.main.media_fragment.*
import org.jetbrains.anko.support.v4.startActivity
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.activities.MediaActivity

class MediaFragment : CacheFragment() {
    override val layout = R.layout.media_fragment

    override fun onChange(view: View) {
        enter_media.setOnClickListener {
            startActivity<MediaActivity>()
        }
    }
}