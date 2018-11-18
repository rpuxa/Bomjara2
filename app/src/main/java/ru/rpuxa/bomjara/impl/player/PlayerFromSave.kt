package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.save.Save21

object PlayerFromSave {
        operator fun invoke(save: Save21): Player {
            var player: Player? = null
            save.apply {
                player = DefaultPlayer(
                        id,
                        name,
                        DefaultCondition(energy, fullness, health),
                        DefaultCondition(maxEnergy, maxFullness, maxHealth),
                        DefaultMoney().apply {
                            rubles = save.rubles
                            euros = save.euros
                            bitcoins = save.bitcoins
                            bottles = save.bottles
                            diamonds = save.diamonds
                        },
                        DefaultPossessions(transport, home, friend, location),
                        courses,
                        age,
                        efficiency,
                        caughtByPolice,
                        deadByZeroHealth,
                        deadByHungry
                )
            }

            return player!!
        }
}