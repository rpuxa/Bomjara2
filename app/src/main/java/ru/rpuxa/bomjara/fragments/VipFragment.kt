package ru.rpuxa.bomjara.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.actions.Vip
import ru.rpuxa.bomjara.activities.App
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.toast

class VipFragment : Fragment() {

    private val ad get() = (activity.application as App).videoAd

    private val closed get() = Player.CURRENT.possessions.location < 2

    private var closedView: View? = null
    private var openedView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (closed) {
            if (closedView == null)
                closedView = inflater.inflate(R.layout.vip_closed, container, false)
        } else {
            if (openedView == null)
                openedView = inflater.inflate(R.layout.vip_opened, container, false)
        }

        return if (closed) closedView!! else openedView!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (closed)
            return
        view.updateMoney()
        view.vip_list.layoutManager = LinearLayoutManager(context)
        view.vip_list.adapter = VipAdapter(Actions.VIPS)
        view.add_vip_diamonds.setOnClickListener {
            val res = ad.show {
                Player.CURRENT.money.diamonds += 3
                toast("Получайте награду")
                view.updateMoney()
            }
            if (!res) {
                toast("Загрузка.. Пожалуйста подождите")
            }
        }
    }

    private fun View.updateMoney() {
        vip_diamonds.text = Player.CURRENT.money.diamonds.toString()
    }

    inner class VipAdapter(private val list: Array<Vip>) : RecyclerView.Adapter<VipAdapter.VipHolder>() {
        inner class VipHolder(val view: View) : RecyclerView.ViewHolder(view) {
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
                holder.view.updateMoney()
            }
        }
    }
}