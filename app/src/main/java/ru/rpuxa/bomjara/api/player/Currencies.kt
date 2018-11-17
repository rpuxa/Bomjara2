package ru.rpuxa.bomjara.api.player

import android.support.annotation.DrawableRes
import ru.rpuxa.bomjara.R.drawable.*
import ru.rpuxa.bomjara.api.HasIcon

enum class Currencies(val id: Int, val cost: Double, @DrawableRes override val iconId: Int) : HasIcon {
    NONE(-1488, Double.NaN, 0),
    RUBLES(0, 1.0, ruble),
    EUROS(1, 70.0, euro),
    BITCOINS(2, 2000.0, bitcoin),
    BOTTLES(3, 1.5, bottle),
    DIAMONDS(4, Double.NaN, diamond)
    ;

    companion object {
        fun getById(id: Int) = values().find { it.id == id }!!
    }
}