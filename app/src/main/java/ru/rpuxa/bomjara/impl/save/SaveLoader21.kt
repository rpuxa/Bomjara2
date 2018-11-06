package ru.rpuxa.bomjara.impl.save

import Game.Serialization.SerializablePlayer
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.saves.SaveLoader
import ru.rpuxa.bomjara.impl.Data.settings
import ru.rpuxa.bomjara.impl.cache.SuperDeserializator
import ru.rpuxa.bomjara.impl.cache.SuperSerializable
import ru.rpuxa.bomjara.impl.random
import ru.rpuxa.bomjara.impl.readObject
import ru.rpuxa.bomjara.save.Save
import ru.rpuxa.bomjara.save.SaveLoader as OldSaveLoader
import ru.rpuxa.bomjara.impl.writeObject
import java.io.File

class SaveLoader21 : SaveLoader {
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
        val deserialize = SuperDeserializator.deserialize(filesDir, SAVES_FILE_NAME)
        val list = (deserialize as? OldSaveLoader.Saves)?.list?.map { it.toSave21() }?.toList()
        if (list != null) {
            saves21.list.clear()
            saves21.list.addAll(list)
            File(filesDir, SAVES_FILE_NAME).delete()
        } else {
            val saves = SuperDeserializator.deserialize(filesDir, SAVES21_FILE_NAME) as? Saves21
            if (saves != null)
                saves21 = saves
        }
        loadOld(filesDir)
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

    private fun loadOld(file: File) {
        try {
            val oldSave = File(file, "player")
            val player: SerializablePlayer = file.readObject(null) ?: return
            val converted = convertOld(player)
            saves21.list.add(converted)
            oldSave.delete()
            settings.lastSave = converted.id
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    private fun convertOld(player: SerializablePlayer): Save21 {
        val fields = player.fields
        val maxIndicators = fields[15] as DoubleArray

        return Save21(
                random.nextLong(),
                true,
                "Старое сохранение",
                fields[0] as Int,
                fields[2] as Long,
                fields[16] as Long,
                fields[7] as Long,
                fields[1] as Long,
                fields[19] as Long,
                fields[14] as Int,
                fields[9] as Int,
                fields[11] as Int,
                fields[18] as Int,
                Math.round((fields[5] as Double) * 100).toInt(),
                maxIndicators[2].toInt(),
                maxIndicators[0].toInt(),
                maxIndicators[1].toInt(),
                100,
                100,
                100,
                fields[13] as IntArray,
                false,
                false,
                false
        )
    }

    private fun Player.toSave21() = Save21(
            id,
            old,
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
                id, old, name, age, bottles, rubles, euros,
                bitcoins, diamonds, location, friend, home, transport, efficiency,
                maxEnergy, maxFullness, maxHealth, energy, fullness, health, courses,
                deadByHungry, deadByZeroHealth, caughtByPolice
        )
    }

    companion object {
        private const val SAVES21_FILE_NAME = "saves2.1"
        private const val SAVES_FILE_NAME = "saves2.0"
    }
}