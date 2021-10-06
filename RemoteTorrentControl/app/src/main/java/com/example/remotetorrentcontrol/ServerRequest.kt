package com.example.remotetorrentcontrol

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

data class ServerConfigs (val HOST : String = "192.168.1.78", val PORT : Int = 9999)

class ServerRequest {
    fun send(data : JSONObject) {
        thread(start = true) {
            // send request
            val conn = Socket(ServerConfigs().HOST, ServerConfigs().PORT)
            OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8).use { out ->
                out.write(data.toString())
            }

            // get result
            val input = BufferedReader(InputStreamReader(conn.inputStream))

            // check result
            println("Client received: ${input.readLine()}")
        }
    }
}