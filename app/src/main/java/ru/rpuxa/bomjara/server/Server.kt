package ru.rpuxa.bomjara.server

import ru.rpuxa.bomjserver.ServerCommand
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicBoolean

object Server {

    private const val IP_SERVER = "89.223.31.120"

    private val commands = ArrayDeque<Cmd>()
    private val running = AtomicBoolean(false)

    private fun connect() {
        running.set(true)
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress(IP_SERVER, 7158), 10_000)
            val oos = ObjectOutputStream(socket.getOutputStream())
            val ois = ObjectInputStream(socket.getInputStream())
            while (commands.isNotEmpty()) {
                val cmd = commands.peekFirst()
                oos.writeObject(cmd.serverCommand)
                oos.flush()
                val ans = ois.readObject() as ServerCommand
                try {
                    cmd.listener(ans)
                } catch (e: Throwable) {
                    throw e
                }

                commands.pollFirst()
            }
            try {
                oos.close()
            } catch (e: Exception) {
            }
            try {
                ois.close()
            } catch (e: Exception) {
            }
            socket.close()
        } catch (e: IOException) {
            commands.forEach { it.listener(null) }
            commands.clear()
            e.printStackTrace()
        } finally {
            running.set(false)
        }
    }

    fun send(id: Int, data: Any? = null, listener: (ServerCommand?) -> Unit = {}) {
        commands.addLast(Cmd(ServerCommand(id, data), listener))
        if (!running.get())
            Thread(::connect).start()
    }

    private data class Cmd(val serverCommand: ServerCommand, val listener: (ServerCommand?) -> Unit)

    const val STATISTIC = 0
    const val REVIEW = 1
    const val NEWS_COUNT = 2
    const val GET_NEWS = 3
}

