package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.*
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Condition
import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.game.player.Possessions


object Actions {
    const val ENERGY = 0
    const val FOOD = 1
    const val HEALTH = 2
    const val JOBS = 3

    private val actions = ArrayList<LocatedAction>()
    val LOCATIONS: Array<Element>
    val FRIENDS: Array<Element>
    val TRANSPORTS: Array<Element>
    val HOMES: Array<Element>
    val COURSES = arrayOf(
            Course(0, "Езда на самокате", FREE, 10),
            Course(1, "Езда на велосипеде", (-200).rub, 30),
            Course(2, "ПДД", (-1000).rub, 100),
            Course(3, "Строение авто", (-100).euro, 200),
            Course(4, "Строительство", (-100).euro, 200),
            Course(5, "Программирование", (-300).euro, 200),
            Course(6, "Торговле валютами", (-600).euro, 200),
            Course(7, "Управление персоналом", (-40).bitcoin, 200)
    )
    val VIPS = arrayOf(
            Vip("+10 к макс. запасу сытости", 9) {
                it.maxCondition.fullness += 10
                Player.CURRENT.listener?.onMaxConditionChanged()
            },
            Vip("+10 к макс. запасу здоровья", 9) {
                it.maxCondition.health += 10
                Player.CURRENT.listener?.onMaxConditionChanged()

            },
            Vip("+10 к макс. запасу бодрости", 9) {
                it.maxCondition.energy += 10
                Player.CURRENT.listener?.onMaxConditionChanged()
            },
            Vip("+10% к эффективности работы", 15) {
                it.efficiency += 10
            }
    )
    private val PENALTIES = arrayOf(
            500, 3000, 6000, 25_000, 50_000,
            150_000, 350_000
    )

    operator fun get(menu: Int): List<Action> {
        val loc = Player.CURRENT.possessions.location
        val friend = Player.CURRENT.possessions.friend

        return actions.asSequence()
                .filter { it.location == (if (menu == JOBS) friend else loc) && it.menu == menu }
                .map { it.action }
                .toList()

    }


    val penalty get() = PENALTIES[Player.CURRENT.possessions.location]

    fun getMenuName(menu: Int) = when (menu) {
        ENERGY -> "Бодрость"
        FOOD -> "Еда"
        HEALTH -> "Здоровье"
        else -> "Работа"
    }

    init {
        LOCATIONS = arrayOf(
                Element("Помойка на окраине", 0, 0, 0, 0, 0, FREE),
                Element("Подъезд", 1, 1, 0, 0, -1, 10.rub),
                Element("Жилой район", 2, 2, 1, 1, -1, FREE),
                Element("Дача", 3, 3, 3, 0, -1, FREE),
                Element("Квартира в микрорайоне", 4, 4, 4, 0, -1, FREE),
                Element("Центр города", 5, 5, 5, 0, -1, FREE),
                Element("Берег моря", 6, 6, 5, 0, -1, FREE)
        )

        FRIENDS = arrayOf(
                Element("Без кореша", 0, 0, 0, 0, 0, FREE),
                Element("Сосед по подъезду Василий", 0, 0, 0, 1, -1, 60.bottle),
                Element("Гопник Валера", 2, 0, 0, 1, -1, 300.bottle),
                Element("Бомбила Семён", 3, 0, 0, 2, -1, 1000.rub),
                Element("Прораб Михалыч", 4, 0, 0, 2, 4, 50.euro),
                Element("Программист Слава", 5, 4, 0, 4, 5, 200.euro),
                Element("Трейдер Юля", 6, 5, 0, 5, 6, 500.euro),
                Element("Директор IT компании Эдвард", 6, 5, 0, 5, 7, 30.bitcoin)
        )

        TRANSPORTS = arrayOf(
                Element("Без транспорта", 0, 0, 0, 0, -1, FREE),
                Element("Самокат", 0, 0, 0, 0, 0, 500.rub),
                Element("Велосипед", 0, 1, 0, 1, 1, 3000.rub),
                Element("Старая копейка", 0, 1, 0, 1, 2, 10000.rub),
                Element("Девятка", 0, 2, 3, 2, 3, 50000.rub),
                Element("Старая Иномарка", 0, 2, 3, 2, 4, 3300.euro),
                Element("Иномарка среднего класса", 0, 2, 3, 2, -1, 8000.euro),
                Element("Спорткар", 0, 0, 0, 0, -1, 500.bitcoin)
        )

        HOMES = arrayOf(
                Element("Помойка", 0, 0, 0, 0, 0, FREE),
                Element("Палатка б/у", 0, 0, 0, 0, -1, 1000.rub),
                Element("Гараж улитка", 2, 1, 0, 1, -1, 9000.rub),
                Element("Сарай", 2, 1, 0, 1, -1, 40000.rub),
                Element("Однушка", 2, 1, 0, 1, 0, 4000.euro),
                Element("Двухкомнатная в центре города", 2, 1, 0, 1, 0, 9000.euro),
                Element("Дом на берегу моря", 7, 1, 0, 1, 0, 600.bitcoin)
        )

        job(0) {
            add("Пособирать бутылки", 25.bottle, -10, -20, -10)
            add("Пособирать монеты", 60.rub, -10, -20, -10)
            add("Украсть бабки у уличных музыкантов", 100.rub, -10, -10, -10, false)
        }

        job(1) {
            add("Пойти с Василием за бутылками", 45.bottle, -5, -20, -10)
            add("Пособирать монеты из фонтана", 120.rub, -10, -20, -10)
            add("Собирать макулатуру на свалках", 190.rub, -5, -40, -20)
            add("Сдать люк на металл", 200.rub, -5, -20, -5, false)
        }

        job(2) {
            add("Пособирать монеты из фонтана", 120.rub, -5, -20, -10)
            add("Расклеивать листовки с Валерой", 250.rub, -10, -30, -20)
            add("Стырить магнитолу", 300.rub, -10, -35, -20, false)
            add("Отжать мобилку", 350.rub, -10, -45, -15, false)
        }

        job(3) {
            add("Мести дворы", 150.rub, -5, -10, -5)
            add("Подработать грузчиком", 300.rub, -5, -25, -5)
            add("Работать бомбилой", 470.rub, -10, -30, -15)
            add("Украсть сумочку", 500.rub, -10, -10, -10, false)
        }

        job(4) {
            add("Месить цемент", 300.rub, -5, -10, -5)
            add("Клеить обои", 550.rub, -5, -25, -5)
            add("Класть плитку", 650.rub, -5, -30, -10)
            add("Тырить вещи со стройки", 10.euro, -10, -10, -10, false)
        }

        job(5) {
            add("Работать в колл-центре", 600.rub, -5, -10, -5)
            add("Работать в службе поддержки", 10.euro, -5, -15, -5)
            add("Кодить сайты", 15.euro, -10, -20, -15)
            add("Написать вирус", 2.bitcoin, -15, -30, -30, false)
            add("Написать Симулятор бомжа", 60.euro, -15, -30, -30, false)
        }

        job(6) {
            add("Зарабатывать на бинарных опционах", 1000.rub, -5, -10, -5)
            add("Управление капиталом", 17.euro, -5, -15, -5)
            add("Продажа акций", 35.euro, -10, -200, -15)
            add("Махинации с курсами валют", 3.bitcoin, -15, -30, -30, false)
        }

        job(7) {
            add("Руководить в отделе безопасности", 25.euro, -5, -10, -5)
            add("Управление компанией", 30.euro, -5, -15, -5)
            add("Разработка ПО для иностранных коллег", 70.euro, -10, -20, -15)
            add("Взлом базы данных конкурентов", 10.bitcoin, -15, -30, -30, false)
        }




        location(0) {
            food {
                add("Жрать объедки с помойки", FREE, -10, -15, 25)
                add("Купить бутер", 40.rub, -5, -5, 30)
                add("Купить шаурму", 100.rub, -5, -5, 70)
                add("Отжать семки у голубей", FREE, -5, -5, 50, false)
            }

            health {
                add("Пособирать травы", FREE, 20, -10, -5)
                add("Сходить к бабке", 70.rub, 60, -10, -5)
                add("Спереть лекарства с аптеки", FREE, 50, -15, -5, false)
            }

            energy {
                add("Поспать", FREE, -5, 20, -5)
                add("Выпить палёнки", 45.rub, -5, 50, -5)
                add("Купить пивас", 60.rub, 0, 70, -5)
                add("Украсть Редбулл", FREE, -10, 60, -5, false)
            }
        }

        location(1) {
            food {
                add("Жрать объедки с помойки", FREE, -10, -15, 25)
                add("Купить шаурму", 100.rub, -5, -5, 30)
                add("Купить шашлык", 250.rub, -5, -5, 80)
                add("Отнять еду у доставщика пиццы", FREE, -5, -5, 70, false)
            }
            health {
                add("Пособирать травы", FREE, 10, -15, -5)
                add("Купить пилюли", 50.rub, 35, -5, -5)
                add("Найти в подъезде бывшего врача", 190.rub, 70, -10, -5)
                add("Украсть у деда лекарства", FREE, 60, -15, -5, false)
            }
            energy {
                add("Поспать в палатке", FREE, -5, 15, -5)
                add("Бухнуть с гопниками", 50.rub, -5, 35, -5)
                add("Купить палёный абсент", 150.rub, -15, 70, -5)
                add("Украсть Редбулл", FREE, -10, 50, -5, false)
            }
        }
        location(2) {
            food {
                add("Сварить гречку", 80.rub, -5, -5, 25)
                add("Сделать похлебку", 270.rub, -5, -5, 50)
                add("Купить птицу гриль", 450.rub, -5, -5, 80)
                add("Отнять еду у доставщика пиццы", FREE, -5, -5, 70, false)
            }
            health {
                add("Делать физ. упражнения", FREE, 10, -15, -5)
                add("Купить пилюли", 100.rub, 30, -5, -5)
                add("Приготовить лечебный отвар", 250.rub, 70, -10, -5)
                add("Ограбить аптеку", FREE, 80, -20, -5, false)
            }
            energy {
                add("Поспать в гараже", FREE, -5, 15, -5)
                add("Найти собутыльников за гаражами", 100.rub, -5, 35, -5)
                add("Купить коньяк", 280.rub, -5, 70, -5)
                add("Отжать бухло у собутыльников", FREE, -10, 80, -5, false)
            }
        }

        location(3) {
            food {
                add("Сварить мясной бульон", 170.rub, -5, -5, 25)
                add("Пойти в магаз", 350.rub, -5, -5, 50)
                add("Заказать пиццу", 600.rub, -5, -5, 80)
                add("Ограбить ларек", FREE, -5, -5, 70, false)
            }
            health {
                add("Купить лекарства", 180.rub, 30, -5, -5)
                add("Сходить в больницу", 470.rub, 80, -10, -5)
                add("Знакомый врач", 7.euro, 100, -10, -5)
            }
            energy {
                add("Купить водяры", 150.rub, -5, 35, -5)
                add("Купить коньяк", 400.rub, -5, 50, -5)
                add("Купить хорошего вина", 9.euro, -5, 100, -5)
            }
        }


        location(4) {
            food {
                add("Пойти в Ашан", 350.rub, -5, -5, 30)
                add("Заказать пиццу", 600.rub, -5, -5, 50)
                add("Пойти в ресторан", 15.euro, -5, -5, 80)
            }
            health {
                add("Гос больница", 500.rub, 40, -10, -5)
                add("Знакомый врач", 7.euro, 40, -10, -5)
                add("Частная больница", 15.euro, 80, -5, -5)
            }
            energy {
                add("Купить абсент", 400.rub, -5, 35, -5)
                add("Купить коньяк", 10.euro, -5, 50, -5)
                add("Купить дорогой виски", 15.euro, -5, 100, -5)
            }
        }


        location(5) {
            food {
                add("Сходить в ТЦ", 600.rub, -5, -5, 30)
                add("Сходить в шашлычную", 13.euro, -5, -5, 50)
                add("Пойти в ресторан", 20.euro, -5, -5, 80)
            }
            health {
                add("Вызвать врача", 800.rub, 25, -10, -5)
                add("Купить иностранные таблетки", 12.euro, 40, -10, -5)
                add("Частная клиника", 25.euro, 80, -5, -5)
            }
            energy {
                add("Сходить в спортзал", 700.rub, -5, 35, -5)
                add("Сходить в фитнес клуб", 17.euro, -5, 50, -5)
                add("Нанять тренера", 25.euro, -5, 100, -5)
            }
        }

        location(6) {
            food {
                add("Сходить на рыбалку", 13.euro, -5, -5, 30)
                add("Приготовить черепаху", 30.euro, -5, -5, 50)
                add("Посетить ресторан на плаву", 40.euro, -5, -5, 80)
            }
            health {
                add("Лечение пиявками", 15.euro, 20, -10, -5)
                add("Частная клиника", 28.euro, 40, -10, -5)
                add("Иностранные врачи", 50.euro, 80, -5, -5)
            }
            energy {
                add("Грязевые ванны", 15.euro, -5, 35, -5)
                add("Сходить в фитнес клуб", 30.euro, -5, 50, -5)
                add("Нырять с аквалангом", 50.euro, -5, 100, -5)
            }
        }

    }


    private class LocatedAction(val location: Int, val menu: Int, val action: Action)

    open class Element(val name: String, transport: Int, house: Int, friend: Int, location: Int, val course: Int, val moneyRemove: Money) {


        val possessions = Possessions(transport, house, friend, location)

    }

    private inline fun job(friend: Int, block: Job.() -> Unit) = Job(friend).block()

    private inline fun location(i: Int, block: Location.() -> Unit) = Location(i).block()


    private class Location(val i: Int) {
        inline fun energy(block: Menu.() -> Unit) = Menu(ENERGY).block()
        inline fun food(block: Menu.() -> Unit) = Menu(FOOD).block()
        inline fun health(block: Menu.() -> Unit) = Menu(HEALTH).block()

        internal inner class Menu(val type: Int) {
            fun add(name: String, removeMoney: Money, health: Int, energy: Int, food: Int, legal: Boolean = true) {
                val action = Action(name, -removeMoney, Condition(energy, food, health), !legal)
                actions.add(LocatedAction(i, type, action))
            }
        }
    }

    private class Job(val i: Int) {
        fun add(name: String, addMoney: Money, health: Int, energy: Int, food: Int, legal: Boolean = true) {
            val action = Action(name, addMoney, Condition(energy, food, health), !legal)
            actions.add(LocatedAction(i, JOBS, action))
        }
    }


}