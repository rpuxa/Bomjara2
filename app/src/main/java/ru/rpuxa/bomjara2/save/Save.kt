package ru.rpuxa.bomjara2.save

import java.io.Serializable

class Save(
        val old: Boolean,
        val name: String,
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
        val courses: IntArray
) : Serializable