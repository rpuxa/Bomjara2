package ru.rpuxa.bomjara.impl.save

import ru.rpuxa.bomjara.CurrentData.actionsBase
import ru.rpuxa.bomjara.cache.SuperSerializable

class Save21() : SuperSerializable {
    var id = 0L
    var name = "name"
    var age = 0
    var bottles = 0L
    var rubles = 0L
    var euros = 0L
    var bitcoins = 0L
    var diamonds = 0L
    var location = 0
    var friend = 0
    var home = 0
    var transport = 0
    var efficiency = 100
    var maxEnergy = 100
    var maxFullness = 100
    var maxHealth = 100
    var energy = 100
    var fullness = 100
    var health = 100
    var courses = IntArray(actionsBase.courses.size)
    var deadByHungry = false
    var deadByZeroHealth = false
    var caughtByPolice = false

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
}