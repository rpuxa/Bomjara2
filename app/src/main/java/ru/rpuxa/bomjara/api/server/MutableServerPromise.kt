package ru.rpuxa.bomjara.api.server

interface MutableServerPromise : ServerPromise {

    fun invokeError()

    fun invokeCommand(data: Any?)
}