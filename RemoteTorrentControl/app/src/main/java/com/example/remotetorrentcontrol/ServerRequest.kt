package com.example.remotetorrentcontrol

import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

data class ServerConfigs (val HOST : String = "192.168.1.78", val PORT : Int = 9999)

class ServerRequest {
    fun send(data : JSONObject): Map<String, String> {
        var byteResponse = ""

        val t = thread(start = true) {
            // send request
            val conn = Socket(ServerConfigs().HOST, ServerConfigs().PORT)
            OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8).use { out ->
                out.write(data.toString())
            }

            println("[ INFO ] client sent: $data")

            // get result
            val input = BufferedReader(InputStreamReader(conn.inputStream))
            byteResponse = input.readLine()

            println("[ INFO ] client received: $byteResponse")
        }
        t.join()

        val jsonResponse = JSONTokener(byteResponse).nextValue() as JSONObject

        return this.jsonToMap(jsonResponse)
    }

    private fun jsonToMap(json: Json): Map<String, String> {
        val map: MutableMap<String, String> = linkedMapOf()
        for (key in json.keys(json)) {
            map[key] = json[key] as String
        }
        return map
    }
}