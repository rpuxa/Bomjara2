package ru.rpuxa.bomjara.api.data

import ru.rpuxa.bomjara.api.actions.ActionsBase
import ru.rpuxa.bomjara.api.player.CurrencyExchange
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.saves.SaveLoader
import ru.rpuxa.bomjara.api.server.Server
import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.api.statistic.Statistic

interface Data {
    val server: Server
    val actionsBase: ActionsBase
    val saveLoader: SaveLoader
    val exchange: CurrencyExchange
    val statistic: Statistic
    val player: Player
    val settings: Settings
}