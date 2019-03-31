package ru.rpuxa.bomjara.impl.save

import ru.rpuxa.bomjara.cache.StringSerializable

@Deprecated("")
class StringSave() : Save, StringSerializable {

    override var id = 0L
    override var name = "name"
    override var age = 0
    override var bottles = 0L
    override var rubles = 0L
    override var euros = 0L
    override var bitcoins = 0L
    override var diamonds = 0L
    override var location = 0
    override var friend = 0
    override var home = 0
    override var transport = 0
    override var efficiency = 100
    override var maxEnergy = 100
    override var maxFullness = 100
    override var maxHealth = 100
    override var energy = 100
    override var fullness = 100
    override var health = 100
    override var courses = IntArray(0)
    override var deadByHungry = false
    override var deadByZeroHealth = false
    override var caughtByPolice = false

    constructor(id: Long, name: String, age: Int, bottles: Long,
                rubles: Long, euros: Long, bitcoins: Long, diamonds: Long, location: Int,
                friend: Int, home: Int, transport: Int, efficiency: Int, maxEnergy: Int,
                maxFullness: Int, maxHealth: Int, energy: Int, fullness: Int, health: Int,
                courses: IntArray, deadByHungry: Boolean, deadByZeroHealth: Boolean,
                caughtByPolice: Boolean) : this() {
        this.id = id
        this.name = name
        this.age = age
        this.bottles = bottles
        this.rubles = rubles
        this.euros = euros
        this.bitcoins = bitcoins
        this.diamonds = diamonds
        this.location = location
        this.friend = friend
        this.home = home
        this.transport = transport
        this.efficiency = efficiency
        this.maxEnergy = maxEnergy
        this.maxFullness = maxFullness
        this.maxHealth = maxHealth
        this.energy = energy
        this.fullness = fullness
        this.health = health
        this.courses = courses
        this.deadByHungry = deadByHungry
        this.deadByZeroHealth = deadByZeroHealth
        this.caughtByPolice = caughtByPolice
    }

    companion object {
        private const val serialVersionUID = 2347354171311523176L
    }
}