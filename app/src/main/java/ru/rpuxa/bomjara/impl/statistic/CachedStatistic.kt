package ru.rpuxa.bomjara.impl.statistic

import ru.rpuxa.bomjara.BuildConfig
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.impl.data.DataImpl.actionsBase

class CachedStatistic() : SuperSerializable {

    var saveId = -14882342368765
    var version = BuildConfig.VERSION_CODE
    var actionsUsingCount = ArrayList<Int>()
    var boughtVips = ArrayList<Int>()

    constructor(save: CachedStatistic? = null, player: Player? = null) : this() {
        actionsUsingCount = zeroList(actionsBase.actions.size).apply {
            if (save != null) {
                val saved = save.actionsUsingCount
                for (i in indices) {
                    if (i < saved.size)
                        set(i, saved[i])
                }
            }
        }

        boughtVips = zeroList(actionsBase.vips.size).apply {
            if (save != null) {
                val saved = save.boughtVips
                for (i in indices) {
                    if (i < saved.size)
                        set(i, saved[i])
                }
            }
        }

        if (save != null)
            saveId = save.saveId
        else if (player != null)
            saveId = player.id
    }

    companion object {
        private fun zeroList(size: Int) = ArrayList<Int>(size).apply { repeat(size) { add(0) } }
    }
}