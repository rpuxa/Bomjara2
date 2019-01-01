package ru.rpuxa.bomjara.api.saves

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.cache.Savable
import ru.rpuxa.bomjara.impl.save.Save

interface SaveLoader : Savable {

    val saves: List<Save>

    fun savePlayer(player: Player)

    fun deleteSave(save: Save)
}