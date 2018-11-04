package ru.rpuxa.bomjara.save

import Game.Serialization.SerializablePlayer
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.random
import ru.rpuxa.bomjara.readObject
import ru.rpuxa.bomjara.settings.settings
import ru.rpuxa.bomjara.writeObject
import java.io.File

object SaveLoader {

    class Saves : SuperSerializable {
        var list = ArrayList<Save>()
    }

    class Saves21 : SuperSerializable {
        var list: MutableList<Save21> = ArrayList()
    }

    private const val SAVES_FILE_NAME = "saves2.0"
    private const val SAVES21_FILE_NAME = "saves2.1"

    var saves21 = Saves21()

    fun save(file: File) {
        file.writeObject(saves21.serialize(), SAVES21_FILE_NAME)
    }

    fun load(file: File) {
        val list = (SuperDeserializator.deserialize(file, SAVES_FILE_NAME) as? Saves)?.list?.map { it.toSave21() }
        if (list != null) {
            saves21.list = list as MutableList<Save21>
            File(file, SAVES_FILE_NAME).delete()
        } else {
            val saves = SuperDeserializator.deserialize(file, SAVES21_FILE_NAME) as? Saves21
            if (saves != null)
                saves21 = saves
        }
        loadOld(file)
    }

    fun savePlayer(player: Player) {
        saves21.list.removeAll { it.name == player.name }
        saves21.list.add(player.toSave21())
    }

    fun findSaveById(id: Long) =
            saves21.list.find { it.id == id }

    fun delete(save: Save21) {
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

        return Save(
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
        ).toSave21()
    }

    private fun Save.toSave21(): Save21 {
        return Save21(id, old, name, age, bottles, rubles, euros,
                bitcoins, diamonds, location, friend, home, transport, efficiency,
                maxEnergy, maxFullness, maxHealth, energy, fullness, health,
                courses.mapIndexed { index, i -> index to i }.toMap() as MutableMap<Int, Int>,
                deadByHungry, deadByZeroHealth, caughtByPolice
        )
    }
}