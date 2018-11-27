package ru.rpuxa.bomjara.impl.data

import ru.rpuxa.bomjara.api.data.MutableData
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.impl.actions.Actions
import ru.rpuxa.bomjara.impl.player.CurrencyExchangeImpl
import ru.rpuxa.bomjara.impl.save.SaveLoader21
import ru.rpuxa.bomjara.impl.server.SocketServer
import ru.rpuxa.bomjara.impl.statistic.StatisticImpl

object DataImpl : MutableData {
    override val server = SocketServer
    override val actionsBase = Actions
    override val saveLoader = SaveLoader21
    override val exchange = CurrencyExchangeImpl
    override val statistic = StatisticImpl
    override lateinit var player: Player
    override lateinit var settings: Settings
}