package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.Action
import ru.rpuxa.bomjara.api.actions.ActionsBase
import ru.rpuxa.bomjara.api.actions.ChainElement
import ru.rpuxa.bomjara.api.actions.Vip
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.server.Server
import ru.rpuxa.bomjara.impl.Data.server
import ru.rpuxa.bomjara.impl.player.DefaultCondition
import ru.rpuxa.bomjara.impl.player.of
import ru.rpuxa.bomjserver.CachedAction
import ru.rpuxa.bomjserver.CachedChainElement
import ru.rpuxa.bomjserver.CachedCourse
import java.io.*
import kotlin.concurrent.thread
import kotlin.properties.Delegates


class Actions : ActionsBase {
    override lateinit var actions: Array<Action>
    override lateinit var locations: Array<ChainElement>
    override lateinit var friends: Array<ChainElement>
    override lateinit var transports: Array<ChainElement>
    override lateinit var homes: Array<ChainElement>
    override lateinit var courses: Array<DefaultCourse>

    override val vips = arrayOf<Vip>(
            DefaultVip(0, "+10 к макс. запасу сытости", 9) {
                it.maxCondition.fullness += 10
                it.listener?.onMaxConditionChanged(it)
            },
            DefaultVip(1, "+10 к макс. запасу здоровья", 9) {
                it.maxCondition.health += 10
                it.listener?.onMaxConditionChanged(it)

            },
            DefaultVip(2, "+10 к макс. запасу бодрости", 9) {
                it.maxCondition.energy += 10
                it.listener?.onMaxConditionChanged(it)
            },
            DefaultVip(3, "+10% к эффективности работы", 15) {
                it.efficiency += 10
            }
    )

    override fun getPenalty(player: Player) = penalties[player.possessions.location]

    override fun getActionsByLevel(level: Int, menu: Int) = actions.filter { it.level == level && it.menu == menu }

    private var hash by Delegates.notNull<Int>()

    private val penalties = arrayOf(
            500, 3_000, 6_000, 25_000, 50_000, 150_000, 350_000
    )

    private fun Array<CachedChainElement>.toElements(): Array<ChainElement> {
        return map {
            DefaultChainElement(
                    it.name, it.transport.toInt(), it.home.toInt(), it.friend.toInt(), it.location.toInt(),
                    it.course.toInt(), it.cost of it.currency.toInt()
            )
        }.toTypedArray()
    }

    private fun Array<CachedAction>.toActions(): Array<Action> {
        return map {
            DefaultAction(
                    it.id.toInt(), it.level.toInt(), it.menu.toInt(), it.name, -it.cost of it.currency.toInt(),
                    DefaultCondition(it.energy.toInt(), it.food.toInt(), it.health.toInt()), it.isIllegal
            )
        }.toTypedArray()
    }

    private fun Array<CachedCourse>.toCourses(): Array<DefaultCourse> {
        return map {
            DefaultCourse(it.id.toInt(), it.name, -it.cost of it.currency.toInt(), it.length.toInt())
        }.toTypedArray()
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadFromStream(inputStream: InputStream): Boolean {
        return try {
            ObjectInputStream(inputStream).use {
                val actions = it.readObject() as Array<CachedAction>
                val locations = it.readObject() as Array<CachedChainElement>
                val friends = it.readObject() as Array<CachedChainElement>
                val transports = it.readObject() as Array<CachedChainElement>
                val homes = it.readObject() as Array<CachedChainElement>
                val courses = it.readObject() as Array<CachedCourse>
                val hash = it.readObject() as Int

                this.actions = actions.toActions()
                this.locations = locations.toElements()
                this.friends = friends.toElements()
                this.transports = transports.toElements()
                this.homes = homes.toElements()
                this.courses = courses.toCourses()
                this.hash = hash
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun loadFromFile(file: File): Boolean {
        try {
            FileInputStream(file).use { return loadFromStream(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override fun load(filesDir: File, assetsInputStream: InputStream) {
        val serverFile = File(filesDir, SERVER_ACTIONS_FILE)
        if (!loadFromFile(serverFile))
            loadFromStream(assetsInputStream)

        loadFromServer()

        save(serverFile)
    }

    override fun save(filesDir: File) {
        try {
            val cachedActions = cachedActions!!
            val cachedLocations = cachedLocations!!
            val cachedFriends = cachedFriends!!
            val cachedTransports = cachedTransports!!
            val cachedHomes = cachedHomes!!
            val cachedCourses = cachedCourses!!
            val cachedHash = cachedHash!!

            ObjectOutputStream(FileOutputStream(File(filesDir, SERVER_ACTIONS_FILE))).use {
                it.writeObject(cachedActions)
                it.writeObject(cachedLocations)
                it.writeObject(cachedFriends)
                it.writeObject(cachedTransports)
                it.writeObject(cachedHomes)
                it.writeObject(cachedCourses)
                it.writeObject(cachedHash)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var cachedActions = null as Array<CachedAction>?
    private var cachedLocations = null as Array<CachedChainElement>?
    private var cachedFriends = null as Array<CachedChainElement>?
    private var cachedTransports = null as Array<CachedChainElement>?
    private var cachedHomes = null as Array<CachedChainElement>?
    private var cachedCourses = null as Array<CachedCourse>?
    private var cachedHash = null as Int?

    @Suppress("UNCHECKED_CAST")
    private fun loadFromServer() {
        thread {
            server.send(Server.GET_CACHED_ACTIONS, Server.HASH)
                    .onCommand { hash ->
                        if (hash as Int != this.hash) {
                            cachedHash = hash
                            server.send(Server.GET_CACHED_ACTIONS, Server.ACTIONS)
                                    .onCommand{ cachedActions = it as? Array<CachedAction>? }
                            server.send(Server.GET_CACHED_ACTIONS, Server.LOCATIONS)
                                    .onCommand{ cachedLocations = it as? Array<CachedChainElement>? }
                            server.send(Server.GET_CACHED_ACTIONS, Server.FRIENDS)
                                    .onCommand{ cachedFriends = it as? Array<CachedChainElement>? }
                            server.send(Server.GET_CACHED_ACTIONS, Server.TRANSPORTS)
                                    .onCommand{ cachedTransports = it as? Array<CachedChainElement>? }
                            server.send(Server.GET_CACHED_ACTIONS, Server.HOMES)
                                    .onCommand{ cachedHomes = it as? Array<CachedChainElement>? }
                            server.send(Server.GET_CACHED_ACTIONS, Server.COURSES)
                                    .onCommand{ cachedCourses = it as? Array<CachedCourse>? }
                        }
                    }
        }
    }

    companion object {
        private const val SERVER_ACTIONS_FILE = "server_actions.bomj"
    }
}