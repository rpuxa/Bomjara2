package ru.rpuxa.bomjara.save

import java.io.Serializable

class Save(
        val id: Long,
        val old: Boolean,
        var name: String,
        val age: Int,
        val bottles: Long,
        val rubles: Long,
        val euros: Long,
        val bitcoins: Long,
        val diamonds: Long,
        val location: Int,
        val friend: Int,
        val home: Int,
        val transport: Int,
        val efficiency: Int,
        val maxEnergy: Int,
        val maxFullness: Int,
        val maxHealth: Int,
        val energy: Int,
        val fullness: Int,
        val health: Int,
        val courses: IntArray,
        val deadByHungry: Boolean,
        val deadByZeroHealth: Boolean,
        val caughtByPolice: Boolean
) : Serializable