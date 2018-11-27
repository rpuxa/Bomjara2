package ru.rpuxa.bomjara.api.server

interface ServerPromise {

    fun onError(callback: () -> Unit): ServerPromise

    fun onCommand(callback: (Any?) -> Unit): ServerPromise

}