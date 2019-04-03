package ru.rpuxa.bomjara.api.actions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.api.player.MonoCurrency

interface Player {
    val id: Long
    val name: String
    val condition: LiveData<Condition>
    val maxCondition: MutableLiveData<Condition>
    val money: LiveData<Money>
    val transport: LiveData<Int>
    val home: LiveData<Int>
    val friend: LiveData<Int>
    val location: LiveData<Int>
    val coursesProgress: LiveData<IntArray>
    val age: MutableLiveData<Int>
    var efficiency: MutableLiveData<Int>
    var daysWithoutCaught: Int
    val endGame: MutableLiveData<Int>
    val doingAction: Boolean

    fun startStudyCourse(id: Int): Boolean

    fun studyCourse(id: Int): Boolean

    fun buyCourse(id: Int): Boolean

    fun addCondition(add: Condition)

    fun addMoney(add: MonoCurrency): Boolean

    fun addSalary(add: MonoCurrency)
}