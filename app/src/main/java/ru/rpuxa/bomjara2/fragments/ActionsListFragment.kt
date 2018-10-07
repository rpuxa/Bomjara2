package ru.rpuxa.bomjara2.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.action.view.*
import kotlinx.android.synthetic.main.actions_list.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Action
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.getCurrencyIcon
import ru.rpuxa.bomjara2.getMenuIcon
import ru.rpuxa.bomjara2.toast

class ActionsListFragment : Fragment() {
    var cacheView: View? = null
    var change = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (cacheView == null)
            cacheView = inflater.inflate(R.layout.actions_list, container, false)!!
        return cacheView!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (change)
            return
        val menu = arguments[MENU] as Int
        val actions = Actions[menu]
        actions_list.setHasFixedSize(true)
        actions_list.layoutManager = LinearLayoutManager(context)
        actions_list.adapter = ActionsAdapter(actions)
        actions_list.adapter.notifyDataSetChanged()
        icon.setImageBitmap(context.getMenuIcon(menu))
        title.text = Actions.getMenuName(menu)
        change = true
    }


    class ActionsAdapter(private val list: List<Action>) : RecyclerView.Adapter<ActionsAdapter.ActionsViewHolder>() {

        class ActionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
            if (!Player.CURRENT.doingAction) {

                when (action.canPerform(Player.CURRENT)) {
                    Action.YES -> {
                        Player.CURRENT.doingAction = true
                        bar.visibility = View.VISIBLE
                        illegal.visibility = View.INVISIBLE
                        bar.start(1000) {
                            Player.CURRENT.doingAction = false
                            if (action.illegal)
                                illegal.visibility = View.VISIBLE
                            bar.visibility = View.INVISIBLE
                            action.perform(Player.CURRENT)
                        }
                    }
                    Action.MONEY_NEEDED -> context.toast(context.getString(R.string.money_needed))
                    Action.ENERGY_NEEDED -> context.toast(context.getString(R.string.cant_work))
                }
            }
        }


    }

    companion object {
        private const val MENU = "menu"

        fun create(menu: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(MENU, menu)
            val fragment = ActionsListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}