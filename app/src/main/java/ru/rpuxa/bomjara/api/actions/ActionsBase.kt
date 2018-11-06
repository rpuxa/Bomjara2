package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.actions.DefaultCourse
import java.io.File
import java.io.InputStream

interface ActionsBase {

    val actions: Array<Action>

    val locations: Array<ChainElement>

    val friends: Array<ChainElement>

    val transports: Array<ChainElement>

    val homes: Array<ChainElement>

    val courses: Array<DefaultCourse>

    val vips: Array<Vip>

    fun getPenalty(player: Player): Int

    fun getActionsByLevel(level: Int, menu: Int): List<Action>

    fun load(filesDir: File, assetsInputStream: InputStream)

    fun save(filesDir: File)
}