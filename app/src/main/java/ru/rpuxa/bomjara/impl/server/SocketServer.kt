package ru.rpuxa.bomjara.impl.server

import ru.rpuxa.bomjara.api.server.Server
import ru.rpuxa.bomjara.api.server.ServerToken
import ru.rpuxa.bomjserver.ServerCommand
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class SocketServer : Server {

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
                while (commands.isNotEmpty()) {
                    val cmd = commands.peekFirst()
                    oos.writeObject(cmd.serverCommand)
                    oos.flush()
                    val ans = ois.readObject() as ServerCommand
                    try {
                        cmd.token.command(ans.data)
                    } catch (e: Throwable) {
                        throw e
                    }

                    commands.pollFirst()
                }
                val startTime = System.currentTimeMillis()
                while (commands.isEmpty() && System.currentTimeMillis() - startTime < 3000)
                    Thread.sleep(50)
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
            commands.forEach { it.token.error() }
            commands.clear()
            e.printStackTrace()
        } finally {
            running.set(false)
        }
    }


    override fun send(id: Int, data: Any?): ServerToken {
        val token = ServerToken()
        commands.addLast(Cmd(ServerCommand(id, data), token))
        if (!running.get())
            Thread(::connect).start()

        return token
    }

    private data class Cmd(val serverCommand: ServerCommand, val token: ServerToken)


    companion object {
        private const val IP_SERVER = "89.223.31.120"
    }
}