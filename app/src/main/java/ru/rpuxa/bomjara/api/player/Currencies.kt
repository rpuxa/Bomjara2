package ru.rpuxa.bomjara.api.player

import androidx.annotation.DrawableRes
import ru.rpuxa.bomjara.R.drawable.*
import ru.rpuxa.bomjara.api.actions.HasIcon

enum class Currencies(val id: Int, val cost: Double, @DrawableRes override val iconId: Int) : HasIcon {
    RUBLES(0, 1.0, ruble),
    BOTTLES(1, 1.5, bottle),
    DIAMONDS(2, Double.NaN, diamond)
    ;
}

inline val rubles get() = Currencies.RUBLES
inline val bottles get() = Currencies.BOTTLES