package ru.rpuxa.bomjara.impl.save

interface Save {
    var id: Long
    var name: String
    var age: Int
    var bottles: Long
    var rubles: Long
    var euros: Long
    var bitcoins: Long
    var diamonds: Long
    var location: Int
    var friend: Int
    var home: Int
    var transport: Int
    var efficiency: Int
    var maxEnergy: Int
    var maxFullness: Int
    var maxHealth: Int
    var energy: Int
    var fullness: Int
    var health: Int
    var courses: IntArray
    var deadByHungry: Boolean
    var deadByZeroHealth: Boolean
    var caughtByPolice: Boolean
}