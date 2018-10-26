package ru.rpuxa.bomjara.views

import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.browser
import ru.rpuxa.bomjara.changeVisibility
import ru.rpuxa.bomjara.settings.settings
import ru.rpuxa.bomjara.statistic.Statistic
import ru.rpuxa.bomjara.toast

class RateDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.rate_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            changeVisibility(View.GONE, rating_text, rate)

            rating_bar.setOnRatingBarChangeListener { _, rating, _ ->
                if (rating > 3) {
                    context.browser(R.string.google_play_link)
                    settings.wasRated = true
                    dismiss()
                    return@setOnRatingBarChangeListener
                }
                changeVisibility(View.VISIBLE, rating_text, rate)
            }

            later_rating.setOnClickListener {
                dismiss()
            }

            dont_show_rating.setOnClickListener {
                settings.wasRated = true
                dismiss()
            }

            rate.setOnClickListener {
                val review = rating_text.text.toString()
                if (review.isNotBlank())
                    Statistic.sendReview(rating_bar.rating, review)
                dismiss()
                context.toast("Спасибо!")
                settings.wasRated = true
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        shown = true
    }

    companion object {
        var shown = false

        const val RATE_DIALOG = "rdialog"
    }
}