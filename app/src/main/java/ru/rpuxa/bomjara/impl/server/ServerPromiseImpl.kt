package ru.rpuxa.bomjara.impl.server

import ru.rpuxa.bomjara.api.server.MutableServerPromise
import ru.rpuxa.bomjara.api.server.ServerPromise

class ServerPromiseImpl : MutableServerPromise {
    private var onErrorListener: (() -> Unit)? = null
    private var onCommandListener: ((Any?) -> Unit)? = null

    override fun onError(callback: () -> Unit): ServerPromise {
        onErrorListener = callback

        return this
    }

    override fun onCommand(callback: (Any?) -> Unit): ServerPromise {
        onCommandListener = callback

        return this
    }

    override fun invokeError() {
        onErrorListener?.invoke()
    }

    override fun invokeCommand(data: Any?) {
        onCommandListener?.invoke(data)
    }
}