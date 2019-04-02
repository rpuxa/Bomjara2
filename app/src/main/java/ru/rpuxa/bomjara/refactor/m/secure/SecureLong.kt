package ru.rpuxa.bomjara.refactor.m.secure

import java.util.*

class SecureLong(value: Long) {

    constructor() : this(0)

    var value: Long = value
        get() {
            checkHash()
            return field
        }
        set(v) {
            field = v
            updateHash()
        }


    private var hash = 0L

    init {
        updateHash()
    }

    private fun calculateHash() =
            Random(value).nextLong()


    private fun updateHash() {
        hash = calculateHash()
    }

    private fun checkHash() {
        if (hash != calculateHash())
            throw HackException("Value = $value")
    }

    private class HackException(m: String) : Exception(m)
}