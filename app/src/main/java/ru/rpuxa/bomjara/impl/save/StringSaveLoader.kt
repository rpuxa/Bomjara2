package ru.rpuxa.bomjara.impl.save

import android.content.Context
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.saves.SaveLoader
import ru.rpuxa.bomjara.cache.StringSerializable
import java.io.File

object StringSaveLoader : SaveLoader {

    private lateinit var stringSaves: Saves

    override val saves: List<Save> get() = stringSaves.list

    override fun savePlayer(player: Player) {
        stringSaves.list.removeAll { it.name == player.name }
        stringSaves.list.add(player.toStringSave())
    }

    override fun deleteSave(save: Save) {
        stringSaves.list.remove(save)
    }

    override fun saveToFile(filesDir: File, context: Context) {
        stringSaves.save(context, SAVES_FILE_NAME)
    }

    override fun loadFromFile(filesDir: File, context: Context) {
        stringSaves = StringSerializable.load(SAVES_FILE_NAME, context) as? Saves ?: Saves()
        SaveLoader21.loadFromFile(filesDir, context)
        SaveLoader21.saves.forEach { stringSaves.list.add(it.toStringSave()) }
        File(SaveLoader21.SAVES21_FILE_NAME).delete()
    }

    private fun Player.toStringSave() = StringSave(
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

    private fun Save.toStringSave() = StringSave(
            id,
            name,
            age,
            bottles,
            rubles,
            euros,
            bitcoins,
            diamonds,
            location,
            friend,
            home,
            transport,
            efficiency,
            maxEnergy,
            maxFullness,
            maxHealth,
            energy, fullness,
            health,
            courses,
            deadByHungry,
            deadByZeroHealth,
            caughtByPolice
    )

    private class Saves : StringSerializable {
        var list = ArrayList<StringSave>()

        companion object {
            private const val serialVersionUID = 17694592376523897L
        }
    }

    private const val SAVES_FILE_NAME = "saves2.1.1"
}