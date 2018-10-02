package ru.rpuxa.bomjara2.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_menu.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.activities.SavesActivity

class MainMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.main_menu, container)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.saves.setOnClickListener {
            openSaves()
        }
    }

    private fun openSaves() {
        activity.startActivity(Intent(context, SavesActivity::class.java))
    }
}