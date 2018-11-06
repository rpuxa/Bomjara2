package ru.rpuxa.bomjara.api.actions

enum class ActionsMenus(val id: Int, val menuName: String) {
    ENERGY(0, "Бодрость"),
    FOOD(1, "Еда"),
    HEALTH(2, "Здоровье"),
    JOBS(3, "Работа")
    ;

    companion object {
        fun getById(id: Int) = values().find { it.id == id }!!
    }
}