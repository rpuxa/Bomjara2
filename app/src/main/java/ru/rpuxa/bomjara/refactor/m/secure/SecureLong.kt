package ru.rpuxa.bomjara.refactor.m.secure

import java.util.*

class SecureLong(value: Long) {

    constructor() : this(0)

    private var _value = value

    var value: Long
        get() {
            checkHash()
            return _value
        }
        set(v) {
            _value = v
            updateHash()
        }


    private var hash = 0L

    init {
        updateHash()
    }

    private fun calculateHash() =
            Random(_value).nextLong()


    private fun updateHash() {
        hash = calculateHash()
    }

    private fun checkHash() {
        if (hash != calculateHash())
            throw HackException("Value = $_value")
    }

    private class HackException(m: String) : Exception(m)
}