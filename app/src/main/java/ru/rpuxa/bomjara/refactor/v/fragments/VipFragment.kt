package ru.rpuxa.bomjara.refactor.v.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vip_item.view.*
import kotlinx.android.synthetic.main.vip_opened.view.*
import org.jetbrains.anko.support.v4.toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.Vip
import ru.rpuxa.bomjara.refactor.m.player.MonoCurrencyImpl
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe
import ru.rpuxa.bomjara.utils.v

class VipFragment : Fragment() {

    private val closed get() = getViewModel<PlayerViewModel>().location.v < 2

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
        val viewModel = getViewModel<PlayerViewModel>()
        viewModel.money.observe(this) {
            view.vip_diamonds.text = it.diamonds.toString()
        }
        view.vip_list.layoutManager = LinearLayoutManager(context)
        view.vip_list.adapter = VipAdapter().apply { submitList(viewModel.vips) }
        view.add_vip_diamonds.setOnClickListener {
            val res = Bomjara.videoAd.show {
                viewModel.addMoney(MonoCurrencyImpl.THREE_DIAMONDS)
                toast("Получайте награду")
            }
            if (!res) {
                toast(R.string.ad_loading)
            }
        }
//        TipFragment.bind(this, R.id.tip_vip)
    }


    private inner class VipAdapter : ListAdapter<Vip, VipHolder>(Diff) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VipHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vip_item, parent, false)
            return VipHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: VipHolder, position: Int) {
            val vip = getItem(position)
            holder.name.text = vip.name
            holder.cost.text = vip.cost.inv().toString()
            val viewModel = getViewModel<PlayerViewModel>()
            holder.name.setOnClickListener {
                toast(if (vip.buy(viewModel)) "Куплено!" else getString(R.string.money_needed))
            }
        }
    }

    private class VipHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.vip_name!!
        val cost = view.vip_cost!!
    }

    private object Diff : DiffUtil.ItemCallback<Vip>() {
        override fun areItemsTheSame(oldItem: Vip, newItem: Vip) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Vip, newItem: Vip) =
                oldItem.id == newItem.id

    }
}