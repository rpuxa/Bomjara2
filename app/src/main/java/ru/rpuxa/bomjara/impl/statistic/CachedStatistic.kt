package ru.rpuxa.bomjara.impl.statistic

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.Data
import ru.rpuxa.bomjara.impl.cache.SuperSerializable

class CachedStatistic(save: CachedStatistic? = null, player: Player? = null, val version: Int) : SuperSerializable {

    var saveId = -14882342368765
    var actionsUsingCount: ArrayList<Int> = zeroList(Data.actionsBase.actions.size).apply {
        if (save != null)
            for (i in indices)
                set(i, save.actionsUsingCount[i])
    }
    var boughtVips: ArrayList<Int> = zeroList(Data.actionsBase.vips.size).apply {
        if (save != null)
            for (i in indices)
                set(i, save.boughtVips[i])
    }

    init {
        if (save != null)
            saveId = save.saveId
        else if (player != null)
            saveId = player.id
    }

    companion object {
        private fun zeroList(size: Int) = ArrayList<Int>(size).apply { repeat(size) { add(0) } }
    }
}