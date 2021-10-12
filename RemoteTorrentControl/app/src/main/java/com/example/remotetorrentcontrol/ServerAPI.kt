package com.example.remotetorrentcontrol

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

data class ServerConfigs (val HOST : String = "192.168.1.78", val PORT : Int = 9999)

class ServerAPI {
    val MOCK_RESPONSE : Boolean = true

    // used by .send() to store response for receive()
    private var response = JsonObject()

    // returns response from .send()
    fun receive() : JsonObject {
        return this.response
    }

    // sends request and returns JsonObject == Map<String, JsonElement>
    fun send(data : JSONObject) {
        if (this.MOCK_RESPONSE) {
            mockResponse(data)
            return
        }

        val data = String(data.toString().toByteArray() , Charsets.UTF_8)
        this.response = JsonObject()

        var byteResponse = ""
        val conn = Socket(ServerConfigs().HOST, ServerConfigs().PORT)

        val t = thread(start = true) {
            // send header
            OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8).use { out ->
                out.write(data.length)
            }
            println("[ INFO ] client sent header: ${data.length}")

            // send request
            OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8).use { out ->
                out.write(data)
            }
            println("[ INFO ] client sent content: $data")

            // get result
            byteResponse = BufferedReader(InputStreamReader(conn.inputStream)).readLine()
            println("[ INFO ] client received header: $byteResponse")
            // TODO: can we skip header from server?

            byteResponse = BufferedReader(InputStreamReader(conn.inputStream)).readLine()
            println("[ INFO ] client received data: $byteResponse")
        }
        t.join()

        this.response = JSONTokener(byteResponse).nextValue() as JsonObject
    }

    // used by send() to create a mock response from the server if MOCK_RESPONSE
    fun mockResponse(data : JSONObject) {
        val request = data["request"]

        // TODO: create enums for these and switch / case
        if (request == "status_check") { // status
            this.response.add("0", JsonParser.parseString("{\"name\":\"Sagan om Kungen\",\"status\":\"13\"}"))
            this.response.add("1", JsonParser.parseString("{\"name\":\"Bajonettmannen\",\"status\":\"23\"}"))
            this.response.add("2", JsonParser.parseString("{\"name\":\"BurgerKing foot lettuce\",\"status\":\"33\"}"))
            this.response.add("3", JsonParser.parseString("{\"name\":\"Ompalompafabriken\",\"status\":\"43\"}"))
            this.response.add("4", JsonParser.parseString("{\"name\":\"Min hemmafilm 2\",\"status\":\"53\"}"))
            this.response.add("5", JsonParser.parseString("{\"name\":\"Hyss på busfabriken 7\",\"status\":\"63\"}"))
            this.response.add("6", JsonParser.parseString("{\"name\":\"Jönssonligan 1\",\"status\":\"73\"}"))
            this.response.add("7", JsonParser.parseString("{\"name\":\"Vem stal min kexchokla\",\"status\":\"83\"}"))
            this.response.add("8", JsonParser.parseString("{\"name\":\"Juice\",\"status\":\"93\"}"))
        }
        else if (request == "search_torrents") { // search
            this.response.add("0", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Sagan om Kungen\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("1", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Bajonettmannen\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("2", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"BurgerKing foot lettuce\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("3", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Ompalompafabriken\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("4", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Min hemmafilm 2\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("5", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Hyss på busfabriken 7\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("6", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Jönssonligan 1\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("7", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Vem stal min kexchokla\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            this.response.add("8", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Juice\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
        }
    }
}