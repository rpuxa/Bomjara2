package ru.rpuxa.bomjara2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.vip_item.view.*
import kotlinx.android.synthetic.main.vip_opened.*
import kotlinx.android.synthetic.main.vip_opened.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.actions.Vip
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.toast

class VipFragment : CacheFragment() {

    private val closed get() = Player.CURRENT.possessions.location < 2

    override val layout get() = if (closed) R.layout.vip_closed else R.layout.vip_opened

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        changed = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onChange(view: View) {
        if (closed)
            return
        updateMoney()
        view.vip_list.layoutManager = LinearLayoutManager(context)
        view.vip_list.adapter = VipAdapter(Actions.VIPS)
    }

    private fun updateMoney() {
        diamonds.text = Player.CURRENT.money.diamonds.toString()
    }

    inner class VipAdapter(private val list: Array<Vip>) : RecyclerView.Adapter<VipAdapter.VipHolder>() {
        inner class VipHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name = view.vip_name!!
            val cost = view.vip_cost!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VipHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vip_item, parent, false)
            return VipHolder(view)
        }

        override fun getItemCount() = list.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: VipHolder, position: Int) {
            val vip = list[position]
            holder.name.text = vip.name
            holder.cost.text = "-" + vip.cost.toString()
            holder.name.setOnClickListener {
                toast(if (vip.buy()) "Куплено!" else context.getString(R.string.money_needed))
                updateMoney()
            }
        }
    }
}