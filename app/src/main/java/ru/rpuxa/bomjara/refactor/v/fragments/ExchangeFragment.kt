package ru.rpuxa.bomjara.refactor.v.fragments

import android.view.View
import kotlinx.android.synthetic.main.exchange.*
import org.jetbrains.anko.support.v4.toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.refactor.m.player.secure.of
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe
import ru.rpuxa.bomjara.utils.v

class ExchangeFragment : CacheFragment() {

    override val layout = R.layout.exchange

    override fun onChange(view: View) {
        bottles_all.setOnClickListener {
            handOver(1)
        }

        bottles_10.setOnClickListener {
            handOver(10)
        }

        bottles_30.setOnClickListener {
            handOver(3)
        }

        getViewModel<PlayerViewModel>().money.observe(this) {
            exchange_bottles_count.text = it.bottles.toString()
        }
    }

    private fun handOver(share: Long) {
        val player = getViewModel<PlayerViewModel>()
        val bottles = player.money.v.bottles / share
        val rubles = bottles * 2
        player.addMoney((-bottles of Currencies.BOTTLES))
        player.addMoney(rubles of Currencies.RUBLES)
        if (bottles == 0L)
            toast("У вас нет бутылок!")
        else
            toast("Вы получили за бутылки $rubles рублей")
    }
}