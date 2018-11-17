package ru.rpuxa.bomjara.api.actions

import android.support.annotation.IdRes
import ru.rpuxa.bomjara.R.drawable.*
import ru.rpuxa.bomjara.api.HasIcon

enum class ActionsMenus(val id: Int, val menuName: String, @IdRes override val iconId: Int) : HasIcon {
    ENERGY(0, "Бодрость", colored_energy),
    FOOD(1, "Еда", colored_food),
    HEALTH(2, "Здоровье", colored_health),
    JOBS(3, "Работа", colored_job)
    ;

    companion object {
        fun getById(id: Int) = values().find { it.id == id }!!
    }
}