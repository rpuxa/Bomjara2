package ru.rpuxa.bomjara.impl.save

import android.content.Context
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.saves.SaveLoader
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.utils.writeObject
import java.io.File

@Deprecated("")
object SaveLoader21 : SaveLoader {
    class Saves21 : SuperSerializable {
        var list = ArrayList<Save21>()
    }

    private var saves21 = Saves21()

    override val saves: List<Save> get() = saves21.list

    override fun saveToFile(filesDir: File, context: Context) {
        val obj = saves21.serialize()
        filesDir.writeObject(obj, SAVES21_FILE_NAME)
    }

    override fun loadFromFile(filesDir: File, context: Context) {
        val saves = SuperDeserializator.deserialize(filesDir, SAVES21_FILE_NAME) as? Saves21
        if (saves != null)
            saves21 = saves
    }

    override fun savePlayer(player: Player) {
        saves21.list.removeAll { it.name == player.name }
        saves21.list.add(player.toSave21())
    }

    override fun deleteSave(save: Save) {
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

    const val SAVES21_FILE_NAME = "saves2.1"
}