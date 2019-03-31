package ru.rpuxa.bomjara.refactor.m

import android.content.Context
import ru.rpuxa.bomjara.api.actions.*
import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.refactor.m.actions.ActionImpl
import ru.rpuxa.bomjara.refactor.m.actions.ChainElementImpl
import ru.rpuxa.bomjara.refactor.m.actions.CourseImpl
import ru.rpuxa.bomjara.refactor.m.actions.VipImpl
import ru.rpuxa.bomjara.refactor.m.player.ConditionImpl
import ru.rpuxa.bomjara.refactor.m.player.of
import ru.rpuxa.bomjara.refactor.m.player.rub
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.utils.update
import ru.rpuxa.bomjara.utils.v
import java.io.EOFException
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

    fun getActionsByLevel(level: Int) = actions.filter { it.level == level }

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
            1000, 6_000, 12_000, 40_000, 100_000, 300_000, 1_000_000
    )


    /**
     * Scheme
     *
     * [UByte. Actions length] {
     *      [UByte, id]
     *      [UByte. Level],
     *      [UByte. menu],
     *      [UByte, name length] { ...}
     *      [UInt. Cost in rubles],
     *      [UByte. Energy],
     *      [UByte. Fullness],
     *      [UByte. Health],
     *      [UByte. Is legal]
     *      ...
     *      ...
     * },
     * [UByte. transports] {
     *      string name
     *      transport,
     *      home,
     *      friend,
     *      location
     *      course
     *      int cost
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
    private fun read(input: InputStream) {
        val actions = ArrayList<Action>()
        input.repeat {
            val action = ActionImpl(
                    input.readUByte(),
                    input.readUByte(),
                    input.readUByte(),
                    input.readString(),
                    input.readInt().rub,
                    ConditionImpl(
                            input.readUByte(),
                            input.readUByte(),
                            input.readUByte()
                    ),
                    !input.readBoolean()
            )

            actions.add(action)
        }

        val chains = Array(4) { ArrayList<ChainElement>() }
        chains.forEach { list ->
            input.repeat {
                val element = ChainElementImpl(
                        input.readString(),
                        input.readUByte(),
                        input.readUByte(),
                        input.readUByte(),
                        input.readUByte(),
                        input.readUByte(),
                        input.readInt().rub
                )

                list.add(element)
            }
        }

        val courses = ArrayList<Course>()
        input.repeat {
            val course = CourseImpl(
                    input.readUByte(),
                    input.readString(),
                    input.readInt().rub,
                    input.readShort()
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

    private fun InputStream.checkRead(): Int {
        val a = read()
        if (a < 0)
            throw EOFException("Конец файла!")
        return a
    }

    private fun InputStream.readBoolean() = checkRead() == 1

    private fun InputStream.readUByte(): Int = checkRead()

    private fun InputStream.readInt(): Int {
        var int = 0
        repeat(4) {
            int = int or checkRead()
            int = int shl 8
        }
        return int
    }

    private fun InputStream.readChar(): Char = readShort().toChar()

    private fun InputStream.readShort(): Int = (checkRead() shl 8) or checkRead()

    private inline fun InputStream.repeat(block: (Int) -> Unit) {
        kotlin.repeat(readUByte(), block)
    }

    private fun InputStream.readString(): String {
        val builder = StringBuilder()
        repeat { builder.append(readChar()) }
        return builder.toString()
    }

    companion object {
        const val ACTIONS_NAME = "new_actions.bomj"
    }
}























