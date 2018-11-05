package ru.rpuxa.bomjara.api.saves

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.cache.Saveable
import ru.rpuxa.bomjara.impl.save.Save21

interface SaveLoader : Saveable {

    val saves: List<Save21>

    fun savePlayer(player: Player)

    fun findSaveById(id: Long): Save21?

    fun deleteSave(save: Save21)
}