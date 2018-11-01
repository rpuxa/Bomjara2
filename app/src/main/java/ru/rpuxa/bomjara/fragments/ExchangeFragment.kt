package ru.rpuxa.bomjara.fragments

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.exchange.*
import kotlinx.android.synthetic.main.open_exchange.*
import ru.rpuxa.bomjara.*
import ru.rpuxa.bomjara.game.CurrencyExchange
import ru.rpuxa.bomjara.game.Player
import java.lang.Math.ceil

class ExchangeFragment : CacheFragment() {

    override val layout = R.layout.exchange

    private val bottles
        get() = Player.current.money.bottles

    override fun onChange(view: View) {

        with(view) {
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


    private fun View.setPercent(divide: Int) {
        val count = Player.current.money.countFromCurrency(from) / divide
        from_count.setText(count.toString())
        update(from, to)
    }

    private fun View.update(fromCurrency: Int, toCurrency: Int) {
        all.text = Player.current.money.countFromCurrency(from).toString()
        val from = from_count.text.toString().toLongOrNull() ?: 0L
        val to = CurrencyExchange.convert(from, fromCurrency, toCurrency)
        to_count.text = to.divider()
    }

    private fun View.convert() {
        val from = from
        val to = to

        val count = from_count.text.toString().toLongOrNull() ?: 0L
        val convertedCount = CurrencyExchange.convert(count, from, to)
        when {
            from == to -> toast("Выберите разные валюты")
            count <= 0L || convertedCount == 0L -> toast("Введите положительную сумму")
            !Player.current.add(-count.currency(from)) -> toast(getString(R.string.money_needed))
            else -> {
                Player.current.add(convertedCount.currency(to))
                toast("Перевод выполнен")
                update(from, to)
            }
        }
    }

    private val View.from
        get() = when {
            from_rubles.isChecked -> RUB
            from_euros.isChecked -> EURO
            from_bitcoins.isChecked -> BITCOIN
            else -> throw IllegalStateException()
        }

    private val View.to
        get() = when {
            to_rubles.isChecked -> RUB
            to_euros.isChecked -> EURO
            to_bitcoins.isChecked -> BITCOIN
            else -> throw IllegalStateException()
        }


    private fun handOver(percent: Double) {
        val count = ceil(bottles * percent).toLong()
        if (count == 0L)
            toast("У вас нет бутылок!")
        else
            toast("Вы получили за бутылки ${CurrencyExchange.handOverBottles(count)} рублей")
    }
}