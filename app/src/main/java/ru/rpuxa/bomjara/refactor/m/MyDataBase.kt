package ru.rpuxa.bomjara.refactor.m

import android.content.Context
import androidx.room.*
import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel

@Database(
        entities = [MyDataBase.Save::class, MyDataBase.Settings::class],
        version = 1
)
abstract class MyDataBase : RoomDatabase() {

    companion object {
        fun create(context: Context): MyDataBase =
                Room.databaseBuilder(context, MyDataBase::class.java, "database.db")
                        .build()

        private const val SAVES_TABLE_NAME = "saves"
        private const val SETTINGS_TABLE_NAME = "settings"
    }

    fun getAllSaves(): List<Save> {
        return savesDao().getAll()
    }

    fun updatePlayer(
            id: Long,
            name: String,
            age: Int,
            bottles: Long,
            rubles: Long,
            diamonds: Long,
            location: Int,
            friend: Int,
            home: Int,
            transport: Int,
            efficiency: Int,
            maxEnergy: Int,
            maxFullness: Int,
            maxHealth: Int,
            energy: Int,
            fullness: Int,
            health: Int,
            courses: IntArray,
            deadByHungry: Boolean,
            deadByZeroHealth: Boolean,
            caughtByPolice: Boolean
    ) {
        savesDao().insert(Save(
                id, name, age, bottles, rubles, diamonds,
                location, friend, home, transport, efficiency, maxEnergy, maxFullness, maxHealth,
                energy, fullness, health, courses, deadByHungry, deadByZeroHealth, caughtByPolice
        ))
    }

    fun updateCondition(id: Long, condition: Condition) {
        savesDao().updateCondition(id, condition.energy, condition.health, condition.fullness)
    }

    fun updateMaxCondition(id: Long, condition: Condition) {
        savesDao().updateMaxCondition(id, condition.energy, condition.health, condition.fullness)
    }

    fun updateMoney(id: Long, money: Money) {
        savesDao().updateMoney(id, money.rubles, money.bottles, money.diamonds)
    }

    fun updateTransport(id: Long, transport: Int) {
        savesDao().updateTransport(id, transport)
    }

    fun updateHome(id: Long, home: Int) {
        savesDao().updateHome(id, home)
    }

    fun updateFriend(id: Long, friend: Int) {
        savesDao().updateFriend(id, friend)
    }

    fun updateLocation(id: Long, location: Int) {
        savesDao().updateLocation(id, location)
    }

    fun updateCoursesProgress(id: Long, progress: IntArray) {
        savesDao().updateCoursesProgress(id, Save.toStringCourses(progress))
    }

    fun updateAge(id: Long, age: Int) {
        savesDao().updateAge(id, age)
    }

    fun updateEndGame(id: Long, endGame: Int) {
        savesDao().apply {
            when (endGame) {
                PlayerViewModel.ALIVE -> {
                    updateDeadByHealth(id, false)
                    updateDeadByHungry(id, false)
                    updateCaughtByPolice(id, false)
                }
                PlayerViewModel.DEAD_BY_HEALTH -> updateDeadByHealth(id, true)
                PlayerViewModel.DEAD_BY_HUNGRY -> updateDeadByHungry(id, true)
                PlayerViewModel.CAUGHT_BY_POLICE -> updateCaughtByPolice(id, true)
            }
        }
    }

    fun deleteSave(id: Long) {
        savesDao().delete(id)
    }

    fun renameSave(id: Long, newName: String) {
        savesDao().rename(id, newName)
    }

    fun getLastSaveId() = getSettings().lastSave

    fun setLastSaveId(id: Long) {
        settingsDao().setLastSave(id)
    }

    fun getShowTips() = getSettings().showTips

    fun setShowTips(bFlag: Boolean) {
        settingsDao().setShowTips(if (bFlag) 1 else 0)
    }


    private fun getSettings(): Settings {
        return settingsDao().get().firstOrNull() ?: run {
            val settings = Settings(true, 0)
            settingsDao().insert(settings)
            return settings
        }
    }


    protected abstract fun savesDao(): SavesDao

    @Dao
    protected interface SavesDao {

        @Query("SELECT * FROM $SAVES_TABLE_NAME")
        fun getAll(): List<Save>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(save: Save)

        @Query("DELETE FROM $SAVES_TABLE_NAME WHERE id = :id")
        fun delete(id: Long)

        @Query("UPDATE $SAVES_TABLE_NAME SET name = :newName WHERE id = :id")
        fun rename(id: Long, newName: String)

        @Query("UPDATE $SAVES_TABLE_NAME SET energy = :energy, health = :health, fullness = :fullness WHERE id = :id")
        fun updateCondition(id: Long, energy: Int, health: Int, fullness: Int)

        @Query("UPDATE $SAVES_TABLE_NAME SET maxEnergy = :energy, maxHealth = :health, maxFullness = :fullness WHERE id = :id")
        fun updateMaxCondition(id: Long, energy: Int, health: Int, fullness: Int)

        @Query("UPDATE $SAVES_TABLE_NAME SET rubles = :rubles, bottles = :bottles, diamonds = :diamonds WHERE id = :id")
        fun updateMoney(id: Long, rubles: Long, bottles: Long, diamonds: Long)

        @Query("UPDATE $SAVES_TABLE_NAME SET transport = :transport WHERE id = :id")
        fun updateTransport(id: Long, transport: Int)

        @Query("UPDATE $SAVES_TABLE_NAME SET home = :home WHERE id = :id")
        fun updateHome(id: Long, home: Int)

        @Query("UPDATE $SAVES_TABLE_NAME SET friend = :friend WHERE id = :id")
        fun updateFriend(id: Long, friend: Int)

        @Query("UPDATE $SAVES_TABLE_NAME SET location = :location WHERE id = :id")
        fun updateLocation(id: Long, location: Int)

        @Query("UPDATE $SAVES_TABLE_NAME SET stringCourses = :progress WHERE id = :id")
        fun updateCoursesProgress(id: Long, progress: String)

        @Query("UPDATE $SAVES_TABLE_NAME SET age = :age WHERE id = :id")
        fun updateAge(id: Long, age: Int)

        @Query("UPDATE $SAVES_TABLE_NAME SET deadByHungry = :b WHERE id = :id")
        fun updateDeadByHungry(id: Long, b: Boolean)

        @Query("UPDATE $SAVES_TABLE_NAME SET deadByZeroHealth = :b WHERE id = :id")
        fun updateDeadByHealth(id: Long, b: Boolean)

        @Query("UPDATE $SAVES_TABLE_NAME SET caughtByPolice = :b WHERE id = :id")
        fun updateCaughtByPolice(id: Long, b: Boolean)
    }

    @Entity(tableName = SAVES_TABLE_NAME)
    class Save(
            @PrimaryKey
            var id: Long,
            var name: String,
            var age: Int,
            var bottles: Long,
            var rubles: Long,
            var diamonds: Long,
            var location: Int,
            var friend: Int,
            var home: Int,
            var transport: Int,
            var efficiency: Int,
            var maxEnergy: Int,
            var maxFullness: Int,
            var maxHealth: Int,
            var energy: Int,
            var fullness: Int,
            var health: Int,
            courses: IntArray,
            var deadByHungry: Boolean,
            var deadByZeroHealth: Boolean,
            var caughtByPolice: Boolean
    ) {

        constructor() : this(
                0, "", 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, IntArray(0),
                false, false, false
        )

        lateinit var stringCourses: String

        var courses: IntArray
            get() {
                val split = stringCourses.split(' ')
                return split.map { it.toInt() }.toIntArray()
            }
            set(value) {
                stringCourses = toStringCourses(value)
            }

        init {
            this.courses = courses
        }

        companion object {
            fun toStringCourses(courses: IntArray): String {
                val builder = StringBuilder()
                for ((i, v) in courses.withIndex()) {
                    builder.append(v)
                    if (i != courses.lastIndex)
                        builder.append(' ')
                }
                return builder.toString()
            }
        }
    }

    protected abstract fun settingsDao(): SettingsDao

    @Dao
    protected interface SettingsDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(settings: Settings)

        @Query("UPDATE $SETTINGS_TABLE_NAME SET lastSave = :id")
        fun setLastSave(id: Long)

        @Query("UPDATE $SETTINGS_TABLE_NAME SET showTips = :bFlag")
        fun setShowTips(bFlag: Int)

        @Query("SELECT * FROM $SETTINGS_TABLE_NAME")
        fun get(): List<Settings>
    }

    @Entity(tableName = SETTINGS_TABLE_NAME)
    class Settings(
            var showTips: Boolean,
            var lastSave: Long
    ) {
        @PrimaryKey
        var id: Int = 0
    }
}
















































