package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.CurrentData.actionsBase
import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.save.Save

object PlayerFactory {

    fun fromSave(save: Save): Player =
            PlayerImpl(
                    save.id,
                    save.name,
                    ConditionImpl(save.energy, save.fullness, save.health),
                    ConditionImpl(save.maxEnergy, save.maxFullness, save.maxHealth),
                    MoneyImpl().apply {
                        rubles = save.rubles
                        euros = save.euros
                        bitcoins = save.bitcoins
                        bottles = save.bottles
                        diamonds = save.diamonds
                    },
                    PossessionsImpl(save.transport, save.home, save.friend, save.location),
                    save.courses,
                    save.age,
                    save.efficiency,
                    save.caughtByPolice,
                    save.deadByZeroHealth,
                    save.deadByHungry
            )

    fun newPlayer(id: Long, name: String): Player =
            PlayerImpl(
                    id,
                    name,
                    ConditionImpl(75, 75, 100),
                    ConditionImpl(100, 100, 100),
                    50 of Currencies.RUBLES,
                    PossessionsImpl(),
                    IntArray(actionsBase.courses.size),
                    0,
                    100,
                    false,
                    false,
                    false
            )

}