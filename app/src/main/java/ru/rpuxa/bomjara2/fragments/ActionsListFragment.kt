package ru.rpuxa.bomjara2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Action

class ActionsListFragment : Fragment() {


    lateinit var actions: List<Action>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.actions_list, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {
        private val ACTIONS = "actions"

        fun create(actions: List<Action>): Fragment {
            val bundle = Bundle()
            bundle.put
        }
    }
}