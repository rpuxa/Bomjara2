package ru.rpuxa.bomjara.api.player

enum class Currencies(val id: Int, val cost: Double) {
    RUBLES(0, 1.0),
    EUROS(1, 70.0),
    BITCOINS(2, 2000.0),
    BOTTLES(3, 1.5),
    DIAMONDS(4, Double.NaN)
    ;


}