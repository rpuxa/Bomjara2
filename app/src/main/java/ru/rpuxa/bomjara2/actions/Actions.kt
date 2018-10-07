package ru.rpuxa.bomjara2.actions

import ru.rpuxa.bomjara2.*
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money
import ru.rpuxa.bomjara2.game.player.Possessions


object Actions {
    private val actions = ArrayList<LocatedAction>()
    private val locations: Array<Chain>
    private val friends: Array<Chain>
    private val transports: Array<Chain>
    private val houses: Array<Chain>
    private val penalties = arrayOf(
            500

    )

    init {
        locations = arrayOf(
                Chain("Помойка на окраине", 0, 0, 0, 0, 0, FREE),
                Chain("Подъезд", 1, 1, 0, 0, -1, FREE),
                Chain("Жилой район", 2, 2, 1, 1, -1, FREE),
                Chain("Дача", 3, 3, 3, 0, -1, FREE),
                Chain("Квартира в микрорайоне", 4, 4, 4, 0, -1, FREE),
                Chain("Центр города", 5, 5, 5, 0, -1, FREE),
                Chain("Берег моря", 6, 6, 5, 0, -1, FREE)
        )

        friends = arrayOf(
                Chain("Без кореша", 0, 0, 0, 0, 0, FREE),
                Chain("Сосед по подъезду Василий", 0, 0, 0, 1, -1, 60.bottle),
                Chain("Гопник Валера", 2, 0, 0, 1, -1, 300.bottle),
                Chain("Бомбила Семён", 3, 0, 0, 2, -1, 1000.rub),
                Chain("Прораб Михалыч", 4, 0, 0, 2, 4, 50.euro),
                Chain("Программист Слава", 5, 4, 0, 4, 5, 200.euro),
                Chain("Трейдер Юля", 6, 5, 0, 5, 6, 500.euro),
                Chain("Директор IT компании Эдвард", 6, 5, 0, 5, 7, 30.bitcoin)
        )

        transports = arrayOf(
                Chain("Без транспорта", 0, 0, 0, 0, -1, FREE),
                Chain("Самокат", 0, 0, 0, 0, 0, 500.rub),
                Chain("Велосипед", 0, 1, 0, 1, 1, 3000.rub),
                Chain("Старая копейка", 0, 1, 0, 1, 2, 10000.rub),
                Chain("Девятка", 0, 2, 3, 2, 3, 50000.rub),
                Chain("Старая Иномарка", 0, 2, 3, 2, 4, 3300.euro),
                Chain("Иномарка среднего класса", 0, 2, 3, 2, -1, 8000.euro),
                Chain("Спорткар", 0, 0, 0, 0, -1, 500.bitcoin)
        )

        houses = arrayOf(
                Chain("Помойка", 0, 0, 0, 0, 0, FREE),
                Chain("Палатка б/у", 0, 0, 0, 0, 0, 1000.rub),
                Chain("Гараж улитка", 2, 1, 0, 1, 0, 9000.rub),
                Chain("Сарай", 2, 1, 0, 1, 0, 40000.rub),
                Chain("Однушка", 2, 1, 0, 1, 0, 4000.euro),
                Chain("Двухкомнатная в центре города", 2, 1, 0, 1, 0, 9000.euro),
                Chain("Дом на берегу моря", 7, 1, 0, 1, 0, 600.bitcoin)
        )

        location(0) {
            energy {
                add("Жрать объедки с помойки", FREE, -10, -15, 25)
                add("Купить бутер", (-40).rub, -5, -5, 30)
                add("Купить шаурму", (-100).rub, -5, -5, 70)
                add("Отжать семки у голубей", FREE, -5, -5, 50, false)
            }

            health {
                add("Пособирать травы", FREE, 10, -10, -5)
                add("Сходить к бабке", (-70).rub, 60, -10, -5)
                add("Спереть лекарства с аптеки", FREE, 50, -15, -5, false)
            }

            food {
                add("Поспать", FREE, -5, 15, -5)
                add("Выпить палёнки", (-45).rub, -5, 30, -5)
                add("Купить пивас", (-60).rub, 0, 30, -5)
                add("Украсть Редбулл", FREE, -10, 60, -5, false)
            }
        }
    }

    class LocatedAction(val location: Int, val menu: Int, val action: Action)

    const val ENERGY = 0
    const val FOOD = 1
    const val HEALTH = 2
    const val JOBS = 3

    open class Chain(val name: String, transport: Int, house: Int, friend: Int, location: Int, val study: Int, val moneyRemove: Money) {
        val possessions = Possessions(transport, house, friend, location)
    }

    class Chains(val list: Array<Chain>)

    operator fun get(menu: Int): List<Action> {
        val loc = Player.CURRENT.possessions.location
        return actions
                .asSequence()
                .filter { it.location == loc && it.menu == menu }
                .map { it.action }
                .toList()
    }

    val penalty get() = penalties[Player.CURRENT.possessions.location]


    fun getMenuName(menu: Int) = when (menu) {
        ENERGY -> "Бодрость"
        FOOD -> "Еда"
        HEALTH -> "Здоровье"
        else -> "Работа"
    }

    private inline fun location(i: Int, block: Location.() -> Unit) = Location(i).block()


    private class Location(val i: Int) {
        inline fun energy(block: Menu.() -> Unit) = Menu(ENERGY).block()
        inline fun food(block: Menu.() -> Unit) = Menu(FOOD).block()
        inline fun health(block: Menu.() -> Unit) = Menu(HEALTH).block()

        internal inner class Menu(val type: Int) {
            fun add(name: String, addMoney: Money, health: Int, energy: Int, food: Int, legal: Boolean = true) {
                val action = Action(name, addMoney, Condition(energy, food, health), !legal)
                actions.add(LocatedAction(i, type, action))
            }
        }
    }


}