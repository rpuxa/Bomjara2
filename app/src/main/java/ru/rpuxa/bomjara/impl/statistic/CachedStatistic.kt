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
        actionsUsingCount = ArrayList<Int>(actionsBase.actions.size).apply {
            if (save != null)
                for (i in indices)
                    add(i, save.actionsUsingCount[i])
        }

        boughtVips = ArrayList<Int>(actionsBase.vips.size).apply {
            if (save != null)
                for (i in indices)
                    add(i, save.boughtVips[i])
        }

        if (save != null)
            saveId = save.saveId
        else if (player != null)
            saveId = player.id
    }

}