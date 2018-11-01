package ru.rpuxa.bomjara.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.action.view.*
import kotlinx.android.synthetic.main.actions_list.*
import kotlinx.android.synthetic.main.actions_list.view.*
import ru.rpuxa.bomjara.*
import ru.rpuxa.bomjara.actions.Action
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.game.Player

class ActionsListFragment : CacheFragment() {

    init {
        updateActions()
    }

    override val layout = R.layout.actions_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menu = arguments[MENU] as Int
        if (updateActions[menu]) {
            onChange(view)
            updateActions[menu] = false
        }
    }


    override fun onChange(view: View) {
        val menu = arguments[MENU] as Int
        val actions = Actions[menu]
        actions_list.setHasFixedSize(true)
        actions_list.layoutManager = LinearLayoutManager(context)
        actions_list.adapter = ActionsAdapter(actions)
        actions_list.adapter.notifyDataSetChanged()
        icon.setImageBitmap(context.getMenuIcon(menu))
        title.text = Actions.getMenuName(menu)
        TipFragment.setTip(view.tip_action, menu)
    }


    inner class ActionsAdapter(private val list: List<Action>) : RecyclerView.Adapter<ActionsAdapter.ActionsViewHolder>() {

        inner class ActionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val button = view.action_button!!
            val illegal = view.illegal!!
            val bar = view.progress_bar!!
            val cost = view.cost!!
            val currency = view.currency!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.action, parent, false)
            return ActionsViewHolder(view)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: ActionsViewHolder, position: Int) {
            with(holder) {
                val action = list[position]
                button.text = action.name
                cost.text = action.addMoney.toString()
                currency.setImageBitmap(holder.currency.context.getCurrencyIcon(action.addMoney))
                illegal.visibility = if (action.illegal) View.VISIBLE else View.INVISIBLE
                val context = button.context
                button.setOnClickListener {
                    action(action, context)
                }
            }
        }

        private fun ActionsViewHolder.action(action: Action, context: Context) {
            if (!Player.current.doingAction) {

                when (action.canPerform(Player.current)) {
                    Action.NOTHING_NEEDED -> {
                        Player.current.doingAction = true
                        bar.visibility = View.VISIBLE
                        illegal.visibility = View.INVISIBLE
                        bar.start(500) {
                            Player.current.doingAction = false
                            if (action.illegal)
                                illegal.visibility = View.VISIBLE
                            bar.visibility = View.INVISIBLE
                            action.perform(Player.current)
                            context.save()
                        }
                    }
                    Action.MONEY_NEEDED -> toast(context.getString(R.string.money_needed))
                    Action.ENERGY_NEEDED -> toast(context.getString(R.string.cant_work))
                }
            }
        }


    }

    companion object {
        val updateActions = Array(4) { true }

        fun updateActions() {
            for (i in updateActions.indices)
                updateActions[i] = true
        }

        private const val MENU = "menu"

        fun create(menu: Int): ActionsListFragment {
            val bundle = Bundle()
            bundle.putInt(MENU, menu)
            val fragment = ActionsListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}