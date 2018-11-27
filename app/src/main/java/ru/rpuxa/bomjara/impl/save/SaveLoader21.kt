package ru.rpuxa.bomjara.impl.save

import ru.rpuxa.bomjara.CurrentData.settings
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.saves.SaveLoader
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.save.Save
import ru.rpuxa.bomjara.utils.writeObject
import java.io.File
import ru.rpuxa.bomjara.save.SaveLoader as OldSaveLoader

object SaveLoader21 : SaveLoader {
    private class Saves21 : SuperSerializable {
        var list = ArrayList<Save21>()
    }

    private var saves21 = Saves21()

    override val saves get() = saves21.list

    override fun saveToFile(filesDir: File) {
        val obj = saves21.serialize()
        filesDir.writeObject(obj, SAVES21_FILE_NAME)
    }

    override fun loadFromFile(filesDir: File) {
        val deserialize = SuperDeserializator.deserialize(filesDir, OLD_SAVES_FILE_NAME)
        val list = (deserialize as? OldSaveLoader.Saves)?.list?.map { it.toSave21() }?.toList()
        if (list != null) {
            saves.clear()
            saves.addAll(list)
            File(filesDir, OLD_SAVES_FILE_NAME).delete()
            if (saves.isNotEmpty())
                settings.lastSave = saves.last().id
        } else {
            val saves = SuperDeserializator.deserialize(filesDir, SAVES21_FILE_NAME) as? Saves21
            if (saves != null)
                saves21 = saves
        }
    }

    override fun savePlayer(player: Player) {
        saves21.list.removeAll { it.name == player.name }
        saves21.list.add(player.toSave21())
    }

    override fun findSaveById(id: Long) =
            saves21.list.find { it.id == id }

    override fun deleteSave(save: Save21) {
        saves21.list.remove(save)
    }

    private fun Player.toSave21() = Save21(
            id,
            name,
            age,
            money.bottles,
            money.rubles,
            money.euros,
            money.bitcoins,
            money.diamonds,
            possessions.location,
            possessions.friend,
            possessions.home,
            possessions.transport,
            efficiency,
            maxCondition.energy,
            maxCondition.fullness,
            maxCondition.health,
            condition.energy,
            condition.fullness,
            condition.health,
            courses,
            deadByHungry,
            deadByZeroHealth,
            caughtByPolice
    )

    private fun Save.toSave21(): Save21 {
        return Save21(
                id, name, age, bottles, rubles, euros,
                bitcoins, diamonds,
                if (location >= 3) location + 1 else location,
                friend,
                if (home >= 3) home + 1 else home,
                transport, efficiency,
                maxEnergy, maxFullness, maxHealth, energy, fullness, health, courses,
                deadByHungry, deadByZeroHealth, caughtByPolice
        )
    }

    private const val SAVES21_FILE_NAME = "saves2.1"
    private const val OLD_SAVES_FILE_NAME = "saves2.0"
}