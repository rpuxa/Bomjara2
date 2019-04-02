package ru.rpuxa.bomjara.refactor.m.secure

class SecureInt(value: Int) {
    constructor() : this(0)

    private val secureLong = SecureLong(value.toLong())

    var value: Int
        get() = secureLong.value.toInt()
        set(value) {
            secureLong.value = value.toLong()
        }

}