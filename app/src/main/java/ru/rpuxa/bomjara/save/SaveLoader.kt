package ru.rpuxa.bomjara.save

import ru.rpuxa.bomjara.cache.SuperSerializable

@Deprecated("")
object SaveLoader {

    @Deprecated("")
    class Saves : SuperSerializable {
        var list = ArrayList<Save>()
    }

}