package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.Currencies.RUBLES
import ru.rpuxa.bomjara.impl.Data

class NewPlayer(id: Long, name: String, old: Boolean) : DefaultPlayer(
        id,
        name,
        old,
        DefaultCondition(75, 75, 100),
        DefaultCondition(100, 100, 100),
        50 of RUBLES,
        DefaultPossessions(),
        IntArray(Data.actionsBase.courses.size),
        0,
        100,
        false,
        false,
        false
)