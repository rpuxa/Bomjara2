package ru.rpuxa.bomjara.refactor.m

import android.content.Context
import ru.rpuxa.bomjara.api.actions.*
import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.refactor.m.actions.ActionImpl
import ru.rpuxa.bomjara.refactor.m.actions.ChainElementImpl
import ru.rpuxa.bomjara.refactor.m.actions.CourseImpl
import ru.rpuxa.bomjara.refactor.m.actions.VipImpl
import ru.rpuxa.bomjara.refactor.m.player.secure.SecureCondition
import ru.rpuxa.bomjara.refactor.m.player.secure.of
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.utils.update
import ru.rpuxa.bomjara.utils.v
import java.io.DataInputStream
import java.io.InputStream
import javax.inject.Inject

class ActionsLoader {

    lateinit var actions: List<Action>
        private set
    lateinit var locations: List<ChainElement>
        private set
    lateinit var friends: List<ChainElement>
        private set
    lateinit var transports: List<ChainElement>
        private set
    lateinit var homes: List<ChainElement>
        private set
    lateinit var courses: List<Course>
        private set

    @Inject
    lateinit var context: Context

    init {
        Bomjara.component.inject(this)
    }

    fun load() = read(context.assets.open(ACTIONS_NAME))


    val vips = vips {
        add(0, "+10 к макс. запасу сытости", 8) {
            it.maxCondition.update { fullness += 10 }
        }
        add(1, "+10 к макс. запасу здоровья", 8) {
            it.maxCondition.update { health += 10 }
        }
        add(2, "+10 к макс. запасу бодрости", 9) {
            it.maxCondition.update { energy += 10 }

        }
        add(3, "+10% к эффективности работы", 17) {
            it.efficiency.value = it.efficiency.v + 10
        }
    }

    fun getPenalty(player: Player) = penalties[player.location.v]

    fun getActionsByLevel(locationLevel: Int, friendLevel: Int): List<Action> {
        return actions.filter {
            it.level == (if (it.menu == ActionsMenus.JOBS.id) friendLevel else locationLevel)
        }
    }

    private inline fun vips(block: CreateVip.() -> Unit): List<Vip> {
        val create = CreateVip()
        create.block()
        return create.list
    }

    private class CreateVip {

        val list = ArrayList<Vip>()

        fun add(id: Int, name: String, cost: Int, onBuy: (Player) -> Unit) {
            list.add(VipImpl(id, name, cost of Currencies.DIAMONDS, onBuy))
        }
    }

    private val penalties = arrayOf(
            1_000, 6_000, 12_000, 40_000, 100_000, 300_000, 1_000_000
    )


    /**
     * Scheme
     *
     * [short Actions length] {
     *      [short, id]
     *      [UByte. Level],
     *      [UByte. menu],
     *      string name
     *      [int. Cost],
     *      [byte. currency],
     *      [byte. Energy],
     *      [byte. Fullness],
     *      [byte. Health],
     *      [boolean. Is legal]
     *      ...
     *      ...
     * },
     * [byte transports] {
     *      string name
     *      byte transport,
     *      byte home,
     *      byte friend,
     *      byte location
     *      byte course
     *      int cost
     *      byte currency
     * }
     * ...
     * friends
     * ...
     * ...
     * ...
     * [UByte courses] {
     *      byte id
     *       string name,
     *      int cost
     *      short length
     * }
     */
    private fun read(i: InputStream) {
        val input = DataInputStream(i)
        val actions = ArrayList<Action>()
        repeat(input.readShort().toInt()) {
            val action = ActionImpl(
                    input.readShort().toInt(),
                    input.readByte().toInt(),
                    input.readByte().toInt(),
                    input.readUTF(),
                    (-input.readInt()) of Currencies.getById(input.readByte().toInt()),
                    SecureCondition(
                            input.readByte().toInt(),
                            input.readByte().toInt(),
                            input.readByte().toInt()
                    ),
                    input.readBoolean()
            )

            actions.add(action)
        }

        val chains = Array(4) { ArrayList<ChainElement>() }
        chains.forEach { list ->
            repeat(input.readByte().toInt()) {
                val element = ChainElementImpl(
                        input.readUTF(),
                        input.readByte().toInt(),
                        input.readByte().toInt(),
                        input.readByte().toInt(),
                        input.readByte().toInt(),
                        input.readByte().toInt(),
                        input.readInt() of Currencies.getById(input.readByte().toInt())
                )

                list.add(element)
            }
        }

        val courses = ArrayList<Course>()
        repeat(input.readByte().toInt()) {
            val course = CourseImpl(
                    input.readByte().toInt(),
                    input.readUTF(),
                    (-input.readInt()) of Currencies.RUBLES,
                    input.readShort().toInt()
            )
            courses.add(course)
        }

        this.actions = actions
        transports = chains[0]
        homes = chains[1]
        friends = chains[2]
        locations = chains[3]
        this.courses = courses
    }


    companion object {
        const val ACTIONS_NAME = "new_actions.bomj"
    }
}























