package ru.rpuxa.bomjara.fragments

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.exchange.view.*
import kotlinx.android.synthetic.main.open_exchange.*
import ru.rpuxa.bomjara.*
import ru.rpuxa.bomjara.game.CurrencyExchange
import ru.rpuxa.bomjara.game.Player
import java.lang.Math.ceil

class ExchangeFragment : CacheFragment() {

    override val layout = R.layout.exchange

    private val bottles
        get() = Player.CURRENT.money.bottles

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

            var block = false

            from_count.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    block = false
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (block)
                        return
                    block = true
                    update(from_count, to_count, from, to, false)

                }
            })

            to_count.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    block = false
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (block)
                        return
                    block = true
                    update(to_count, from_count, to, from, true)
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
                update(from_count, to_count, from, to, false)
            }

            to_group.setOnCheckedChangeListener { _, _ ->
                update(from_count, to_count, from, to, false)
            }


            update(from_count, to_count, from, to, false)
        }
    }


    private fun View.setPercent(divide: Int) {
        val count = Player.CURRENT.money.countFromCurrency(from) / divide
        from_count.setText(count.toString())
        update(from_count, to_count, from, to, false)
    }

    private fun View.update(fromCount: EditText, toCount: EditText, fromCurrency: Int, toCurrency: Int, reverse: Boolean) {
        all.text = Player.CURRENT.money.countFromCurrency(from).toString()
        val from = fromCount.text.toString().toLongOrNull() ?: 0L
        val to = CurrencyExchange.convert(from, fromCurrency, toCurrency, reverse)
        toCount.setText(to.toString())
    }

    private fun View.convert() {
        val from = from
        val to = to

        val count = from_count.text.toString().toLong()

        val convertedCount = CurrencyExchange.convert(count, from, to, false)
        if (!Player.CURRENT.add(-count.currency(from))) {
            toast(getString(R.string.money_needed))
            return
        }
        Player.CURRENT.add(convertedCount.currency(to))
        toast("Перевод выполнен")
        update(from_count, to_count, from, to, false)
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