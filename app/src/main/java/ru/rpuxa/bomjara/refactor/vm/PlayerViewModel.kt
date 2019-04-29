package ru.rpuxa.bomjara.refactor.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.rpuxa.bomjara.api.actions.Action
import ru.rpuxa.bomjara.api.actions.ChainElement
import ru.rpuxa.bomjara.api.actions.Course
import ru.rpuxa.bomjara.api.actions.Player
import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.refactor.m.ActionsLoader
import ru.rpuxa.bomjara.refactor.m.MyDataBase
import ru.rpuxa.bomjara.refactor.m.MyDataBase.Save.Companion.DEAD_BY_HEALTH
import ru.rpuxa.bomjara.refactor.m.MyDataBase.Save.Companion.DEAD_BY_HUNGRY
import ru.rpuxa.bomjara.refactor.m.player.secure.SecureCondition
import ru.rpuxa.bomjara.refactor.m.player.secure.SecureMoney
import ru.rpuxa.bomjara.refactor.m.player.secure.SecureMonoCurrency
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.utils.gauss
import ru.rpuxa.bomjara.utils.nnValue
import ru.rpuxa.bomjara.utils.update
import ru.rpuxa.bomjara.utils.v
import javax.inject.Inject

class PlayerViewModel : ViewModel(), Player {

    private val res = Resources()

    override var id: Long = 0L
    override var name: String = ""
    override val condition = MutableLiveData<Condition>()
    override val maxCondition = MutableLiveData<Condition>()
    override val money = MutableLiveData<Money>()
    override val transport = MutableLiveData<Int>()
    override val home = MutableLiveData<Int>()
    override val friend = MutableLiveData<Int>()
    override val location = MutableLiveData<Int>()
    override val coursesProgress = MutableLiveData<IntArray>()
    override val age = MutableLiveData<Int>()
    override var efficiency = MutableLiveData<Int>()
    override var daysWithoutCaught: Int = 2
    override val endGame = MutableLiveData<Int>()
    override var doingAction: Boolean = false

    val currentActions = MutableLiveData<List<Action>>()
    val availableCourses = MutableLiveData<List<Course>>()
    val currentCourses = MutableLiveData<List<Course>>()
    val completedCourses = MutableLiveData<List<Course>>()


    init {
        runBlocking {
            val id = res.myDataBase.getLastSaveId()
            val save = res.myDataBase.getAllSaves()
                .find { it.id == id }!!
            this@PlayerViewModel.id = save.id
            name = save.name
            condition.value = SecureCondition(save.energy, save.fullness, save.health)
            maxCondition.value = SecureCondition(save.maxEnergy, save.maxFullness, save.maxHealth)
            money.value = SecureMoney().apply {
                rubles = save.rubles
                bottles = save.bottles
                diamonds = save.diamonds
            }
            transport.value = save.transport
            home.value = save.home
            friend.value = save.friend
            location.value = save.location
            coursesProgress.value = save.courses
            age.value = save.age
            efficiency.value = save.efficiency
            endGame.value = save.endGame



            fun updateAvailableCourses() {
                availableCourses.value = courses.filter {
                    it.id < location.v + 2 &&
                        coursesProgress.v[it.id] == 0
                }
            }

            fun updateAction() {
                currentActions.value = res.actions.getActionsByLevel(location.v, friend.v)
            }

            location.observeForever {
                updateAction()
                updateAvailableCourses()
            }

            coursesProgress.observeForever { progress ->
                currentCourses.value = courses.filter { progress[it.id] in 1 until it.length }
                completedCourses.value = courses.filter { progress[it.id] == it.length }
                updateAvailableCourses()
            }

            condition.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateCondition(id, it)
                }
            }

            maxCondition.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateMaxCondition(id, it)
                }
            }

            money.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateMoney(id, it)
                }
            }

            transport.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateTransport(id, it)
                }
            }

            home.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateHome(id, it)
                }
            }

            friend.observeForever {
                updateAction()
                GlobalScope.launch {
                    res.myDataBase.updateFriend(id, it)
                }
            }

            location.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateLocation(id, it)
                }
            }

            coursesProgress.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateCoursesProgress(id, it)
                }
            }

            age.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateAge(id, it)
                }
            }

            endGame.observeForever {
                GlobalScope.launch {
                    res.myDataBase.updateEndGame(id, it)
                }
            }
        }
    }

    override fun startStudyCourse(id: Int): Boolean {
        if (addMoney(getCourse(id).cost)) {
            coursesProgress.update {
                this[id] = 1
            }
            return true
        }
        return false
    }

    override fun studyCourse(id: Int): Boolean {
        coursesProgress.update {
            this[id]++
        }
        addCondition(COURSE_CONDITION)
        return coursesProgress.v[id] == courses[id].length
    }

    override fun buyCourse(id: Int): Boolean {
        val course = getCourse(id)
        if (addMoney(
                SecureMonoCurrency(
                    course.skipCost.count - course.skipCost.count * coursesProgress.v[id] / course.length,
                    course.skipCost.currency
                )
            )
        ) {
            coursesProgress.update { this[id] = getCourse(id).length }
            return true
        }
        return false
    }

    override fun addCondition(add: Condition) {
        condition.update {
            addAssign(add.multiply(gauss))
            truncateAssign(maxCondition.nnValue)
            if (health == 0)
                endGame.value = DEAD_BY_HEALTH
            else if (fullness == 0)
                endGame.value = DEAD_BY_HUNGRY
        }
    }

    override fun addMoney(add: MonoCurrency): Boolean {
        money.update {
            if (!addAssign(add)) {
                return false
            }
        }
        return true
    }

    override fun addSalary(add: MonoCurrency) {
        addMoney(add.multiply(gauss).multiply(efficiency.v.toDouble() / 100))
    }

    fun getLocation(id: Int): ChainElement = res.actions.locations[id]
    fun getTransport(id: Int): ChainElement = res.actions.transports[id]
    fun getFriend(id: Int): ChainElement = res.actions.friends[id]
    fun getHome(id: Int): ChainElement = res.actions.homes[id]
    fun getCourse(id: Int) = res.actions.courses[id]
    val penalty get() = res.actions.getPenalty(this)


    val locations get() = res.actions.locations
    val friends get() = res.actions.friends
    val homes get() = res.actions.homes
    val transports get() = res.actions.transports
    val courses get() = res.actions.courses
    val vips get() = res.actions.vips

    class Resources {
        @Inject
        lateinit var myDataBase: MyDataBase

        @Inject
        lateinit var actions: ActionsLoader

        init {
            Bomjara.component.inject(this)
        }
    }

    companion object {
        private val COURSE_CONDITION = SecureCondition(-5, -5, -5)
    }

}