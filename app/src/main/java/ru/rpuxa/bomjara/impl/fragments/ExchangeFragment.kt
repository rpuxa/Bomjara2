package ru.rpuxa.bomjara.impl.fragments

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.exchange.*
import kotlinx.android.synthetic.main.open_exchange.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.impl.Data.exchange
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.utils.divider
import ru.rpuxa.bomjara.impl.player.of
import ru.rpuxa.bomjara.impl.toast
import java.lang.Math.ceil

class ExchangeFragment : CacheFragment() {

    override val layout = R.layout.exchange

    private val bottles
        get() = player.money.bottles

    override fun onChange(view: View) {
        with(view) {
            setBottles()

            bottles_all.setOnClickListener {
                handOver(1.0)
            }

            bottles_10.setOnClickListener {
                handOver(0.1)
            }

            bottles_30.setOnClickListener {
                handOver(0.3)
            }

            from_count.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    update(from, to)
                }
            })

            percent_100.setOnClickListener {
                setPercent(1)
            }

            percent_50.setOnClickListener {
                setPercent(2)
            }

            percent_25.setOnClickListener {
                setPercent(4)
            }

            percent_10.setOnClickListener {
                setPercent(10)
            }

            convert.setOnClickListener {
                convert()
            }

            from_group.setOnCheckedChangeListener { _, _ ->
                update(from, to)
            }

            to_group.setOnCheckedChangeListener { _, _ ->
                update(from, to)
            }


            update(from, to)
        }
    }

    private fun setBottles() {
        exchange_bottles_count.text = player.money.bottles.toString()
    }


    private fun View.setPercent(divide: Int) {
        val count = player.money.currencies[from.id] / divide
        from_count.setText(count.toString())
        update(from, to)
    }

    private fun View.update(fromCurrency: Currencies, toCurrency: Currencies) {
        all.text = player.money.currencies[from.id].toString()
        val from = from_count.text.toString().toLongOrNull() ?: 0L
        val to = exchange.convertWithCommission(from, fromCurrency, toCurrency)
        to_count.text = to.divider()
    }

    private fun View.convert() {
        val from = from
        val to = to

        val count = from_count.text.toString().toLongOrNull() ?: 0L
        val convertedCount = exchange.convertWithCommission(count, from, to)
        when {
            from == to -> toast("Выберите разные валюты")
            count <= 0L || convertedCount == 0L -> toast("Введите положительную сумму")
            !player.addMoney(-count of from) -> toast(getString(R.string.money_needed))
            else -> {
                player.addMoney(convertedCount of to)
                toast("Перевод выполнен")
                update(from, to)
            }
        }
    }

    private val View.from
        get() = when {
            from_rubles.isChecked -> Currencies.RUBLES
            from_euros.isChecked -> Currencies.EUROS
            from_bitcoins.isChecked -> Currencies.BITCOINS
            else -> throw IllegalStateException()
        }

    private val View.to
        get() = when {
            to_rubles.isChecked -> Currencies.RUBLES
            to_euros.isChecked -> Currencies.EUROS
            to_bitcoins.isChecked -> Currencies.BITCOINS
            else -> throw IllegalStateException()
        }


    private fun handOver(percent: Double) {
        val count = ceil(bottles * percent).toLong()
        val converted = exchange.convert(count, Currencies.BOTTLES, Currencies.RUBLES)
        if (count == 0L)
            toast("У вас нет бутылок!")
        else
            toast("Вы получили за бутылки $converted рублей")
        setBottles()
    }
}