package ru.rpuxa.bomjara2.save

import Game.Serialization.SerializablePlayer
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.random
import ru.rpuxa.bomjara2.readObject
import ru.rpuxa.bomjara2.writeObject
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

object SaveLoader {

    private const val FILE_NAME = "saves2.0"

    var saves = ArrayList<Save>()

    fun save(file: File) {
        file.writeObject(saves, FILE_NAME)
    }

    fun load(file: File) {
        val saves = file.readObject<ArrayList<Save>>(FILE_NAME)
        if (saves != null)
            this.saves = saves
        loadOld(file)
    }

    fun savePlayer(player: Player) {
        saves.removeAll { it.name == player.name }
        saves.add(player.toSave())
    }

    fun findSaveById(id: Long) =
            saves.find { it.id == id }

    fun delete(save: Save) {
        saves.remove(save)
    }

    private fun loadOld(file: File) {
        val file1 = File(file, "player")
        val player = try {
            ObjectInputStream(FileInputStream(file1)).use {
                it.readObject() as SerializablePlayer
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        saves.add(convertOld(player))
        //  file1.delete()
    }

    fun convertOld(player: SerializablePlayer): Save {
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
                (fields[6] as Double).toInt(),
                (fields[8] as Double).toInt(),
                (fields[10] as Double).toInt(),
                fields[13] as IntArray,
                false,
                fields[4] as Boolean,
                fields[3] as Boolean
        )
    }
}