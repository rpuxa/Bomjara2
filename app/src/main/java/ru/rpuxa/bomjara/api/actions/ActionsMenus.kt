package ru.rpuxa.bomjara.api.actions

import androidx.annotation.DrawableRes
import ru.rpuxa.bomjara.R.drawable.*

/**
 * All menus with actions existing in game
 */
enum class ActionsMenus(val id: Int, val menuName: String, @DrawableRes override val iconId: Int) : HasIcon {
    ENERGY(0, "Бодрость", colored_energy),
    FOOD(1, "Еда", colored_food),
    HEALTH(2, "Здоровье", colored_health),
    JOBS(3, "Работа", colored_job)
    ;

    companion object {
        fun getById(id: Int) = values().find { it.id == id }!!
    }
}