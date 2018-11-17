package ru.rpuxa.bomjara.impl

import ru.rpuxa.bomjara.api.actions.ActionsBase
import ru.rpuxa.bomjara.api.player.CurrencyExchange
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.saves.SaveLoader
import ru.rpuxa.bomjara.api.server.Server
import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.api.statistic.Statistic
import ru.rpuxa.bomjara.impl.actions.Actions
import ru.rpuxa.bomjara.impl.player.DefaultCurrencyExchange
import ru.rpuxa.bomjara.impl.save.SaveLoader21
import ru.rpuxa.bomjara.impl.server.SocketServer
import ru.rpuxa.bomjara.impl.statistic.DefaultStatistic
import kotlin.properties.Delegates

object Data {
    val server: Server = SocketServer
    val actionsBase: ActionsBase = Actions
    val saveLoader: SaveLoader = SaveLoader21
    val exchange: CurrencyExchange = DefaultCurrencyExchange
    val statistic: Statistic = DefaultStatistic
    lateinit var player: Player
    lateinit var settings: Settings
}