package ru.rpuxa.bomjara.refactor.v.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.action.view.*
import kotlinx.android.synthetic.main.actions_list.*
import org.jetbrains.anko.support.v4.toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.Action
import ru.rpuxa.bomjara.api.actions.ActionsMenus
import ru.rpuxa.bomjara.refactor.v.NoScrollManager
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.divider
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe
import ru.rpuxa.bomjara.utils.setVisibility

class ActionsListFragment : CacheFragment() {

    private val menu by lazy { arguments!![MENU] as Int }

    override val layout = R.layout.actions_list

    override fun onChange(view: View) {
        val viewModel = getViewModel<PlayerViewModel>()
        val adapter = ActionsAdapter()
        actions_list.layoutManager = NoScrollManager(context)
        actions_list.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        actions_list.adapter = adapter
        viewModel.currentActions.observe(this) { all ->
            val actions = all.filter { it.menu == menu }
            adapter.submitList(actions)
        }
        icon.setImageBitmap(ActionsMenus.getById(menu).getIcon(context))
        title.text = ActionsMenus.getById(menu).menuName
        TipFragment.bind(this, R.id.tip_action, menu)
    }


    inner class ActionsAdapter : ListAdapter<Action, ActionsViewHolder>(Diff) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.action, parent, false)
            return ActionsViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ActionsViewHolder, position: Int) {
            with(holder) {
                val action = getItem(position)
                name.text = action.name
                illegal.visibility = if (action.illegal) View.VISIBLE else View.GONE
                itemView.setOnClickListener {
                    action(action)
                }

                val actionsMenus = ActionsMenus.getById(menu)
                setVisibility(if (actionsMenus == ActionsMenus.JOBS) View.GONE else View.VISIBLE, remove, removeIcon)
                if (actionsMenus != ActionsMenus.JOBS) {
                    val count = when (actionsMenus) {
                        ActionsMenus.ENERGY -> action.addCondition.energy
                        ActionsMenus.FOOD -> action.addCondition.fullness
                        ActionsMenus.HEALTH -> action.addCondition.health
                        else -> throw IllegalStateException()
                    }
                    add.text = count.toString()
                    addIcon.setImageResource(actionsMenus.iconId)
                    remove.text = action.addMoney.count.divider()
                    removeIcon.setImageBitmap(action.addMoney.currency.getIcon(context))
                } else {
                    add.text = "+" + action.addMoney.count.divider()
                    addIcon.setImageBitmap(action.addMoney.currency.getIcon(context))
                }
            }
        }

        private fun ActionsViewHolder.action(action: Action) {
            val viewModel = getViewModel<PlayerViewModel>()
            if (!viewModel.doingAction) {
                when (action.canPerform(viewModel)) {
                    Action.NOTHING_NEEDED -> {
                        viewModel.doingAction = true
                        bar.visibility = View.VISIBLE
                        bar.start(500) {
                            viewModel.doingAction = false
                            bar.visibility = View.INVISIBLE
                            action.perform(viewModel)
                        }
                    }
                    Action.MONEY_NEEDED -> toast(getString(R.string.money_needed))
                    Action.ENERGY_NEEDED -> toast(getString(R.string.cant_work))
                }
            }
        }
    }

    class ActionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val illegal = view.action_illegal!!
        val bar = view.actions_progress_bar!!
        val add = view.actions_add!!
        val addIcon = view.actions_add_icon!!
        val remove = view.actions_remove!!
        val removeIcon = view.actions_remove_icon!!
        val name = view.action_name!!
    }

    object Diff : DiffUtil.ItemCallback<Action>() {
        override fun areItemsTheSame(oldItem: Action, newItem: Action) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Action, newItem: Action) =
                true
    }

    companion object {
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