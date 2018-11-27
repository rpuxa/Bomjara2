package ru.rpuxa.bomjara.api.data

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.settings.Settings

interface MutableData : Data {

    override var player: Player

    override var settings: Settings
}