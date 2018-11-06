package ru.rpuxa.bomjara.api.server

class ServerToken {
    private var onErrorListener: (() -> Unit)? = null
    private var onCommandListener: ((Any?) -> Unit)? = null

    fun onError(callback: () -> Unit): ServerToken {
        onErrorListener = callback

        return this
    }

    fun onCommand(callback: (Any?) -> Unit): ServerToken {
        onCommandListener = callback

        return this
    }

    fun error() {
        onErrorListener?.invoke()
    }

    fun command(data: Any?) {
        onCommandListener?.invoke(data)
    }
}