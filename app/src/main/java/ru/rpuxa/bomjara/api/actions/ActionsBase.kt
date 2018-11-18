package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.actions.DefaultCourse
import java.io.File
import java.io.InputStream

/**
 * Actions base which exists all actions, locations,
 * friends, transports, homes, courses and vip goods available in game.
 */
interface ActionsBase {

    val actions: Array<Action>

    val locations: Array<ChainElement>

    val friends: Array<ChainElement>

    val transports: Array<ChainElement>

    val homes: Array<ChainElement>

    val courses: Array<DefaultCourse>

    val vips: Array<Vip>


    /**
     * Get penalty (in rubles) for location of the player
     */
    fun getPenalty(player: Player): Int

    /**
     * Get action list for current [level] and [menu]
     */
    fun getActionsByLevel(level: Int, menu: Int): List<Action>

    /**
     * Load data from file (if it exists) or assets
     */
    fun load(filesDir: File, assetsInputStream: InputStream)

    /**
     * Save data to file
     */
    fun save(filesDir: File)
}