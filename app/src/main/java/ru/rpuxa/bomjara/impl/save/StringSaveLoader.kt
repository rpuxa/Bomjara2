package ru.rpuxa.bomjara.impl.save

import android.content.Context
import ru.rpuxa.bomjara.cache.StringSerializable

@Deprecated("Legacy code")
object StringSaveLoader {

    fun loadFromFile(context: Context): List<Save>? {
        val saves = (StringSerializable.load(SAVES_FILE_NAME, context) as? Saves)?.list
        val edit = context.getSharedPreferences(SAVES_FILE_NAME, Context.MODE_PRIVATE).edit()
        edit.remove("savefieldname")
        edit.apply()

        return saves
    }


    private class Saves : StringSerializable {
        var list = ArrayList<StringSave>()

        companion object {
            private const val serialVersionUID = 17694592376523897L
        }
    }

    private const val SAVES_FILE_NAME = "saves2.1.1"
}

