package com.example.remotetorrentcontrol

import com.beust.klaxon.Klaxon
import com.google.gson.JsonParser
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
//data class Response(val data : ArrayList<Map<String, String>> = ArrayList())

// represents ONE entry in the list
@Serializable
data class ResponseEntry(val map : Map<String, String>)

class ServerAPI {
    val config = ConfigHandler().getConfig()
    val MOCK_RESPONSE : Boolean = true

    // used by .send() to store response for receive()
    private var response = ArrayList<ResponseEntry>()

    // converts json string to [{}, {}, ..., {}]
    private fun jsonToArray(json : JSONObject) : Response {
        val r = Klaxon().parse<Response>(json.toString())
        println("ServerAPI::jsonToArray() $r")
        if (r == null)
            return ArrayList()

        return r
    }

    // returns response from .send()
    fun receive() : Response {
        return response
    }

    // sends request and returns JsonObject == Map<String, JsonElement>
    fun send(data : JSONObject) {
        if (this.MOCK_RESPONSE) {
            mockResponse(data)
            return
        }

        val data = String(data.toString().toByteArray(), Charsets.UTF_8)
        var jsonResponse = JSONObject()

        var byteResponse = ""
        val conn = Socket(config.SERVER, config.PORT)

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

        // this.jsonResponse = JSONTokener(byteResponse).nextValue() as JSONObject
        jsonResponse = JSONObject(byteResponse)

        this.response = jsonToArray(jsonResponse)
    }

    // used by send() to create a mock response from the server if MOCK_RESPONSE
    private fun mockResponse(data : JSONObject) {
        val request = data["request"]
        val jsonResponse = JSONObject()
        // TODO: create enums for these and switch / case
        if (request == "status_check") { // status
            jsonResponse.put("data", JsonParser.parseString("{\"name\":\"Sagan om Kungen\",\"status\":\"13\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"Bajonettmannen\",\"status\":\"23\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"BurgerKing foot lettuce\",\"status\":\"33\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"Ompalompafabriken\",\"status\":\"43\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"Min hemmafilm 2\",\"status\":\"53\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"Hyss på busfabriken 7\",\"status\":\"63\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"Jönssonligan 1\",\"status\":\"73\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"Vem stal min kexchokla\",\"status\":\"83\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"name\":\"Juice\",\"status\":\"93\"}"))
        }
        else if (request == "search_torrents") { // search
            jsonResponse.put("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Sagan om Kungen\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Bajonettmannen\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"BurgerKing foot lettuce\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Ompalompafabriken\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Min hemmafilm 2\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Hyss på busfabriken 7\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Jönssonligan 1\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Vem stal min kexchokla\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
            jsonResponse.accumulate("data", JsonParser.parseString("{\"type\":\"Movies\",\"name\":\"Juice\",\"uploaded\":\"2021-10-09\",\"size\":\"1.4 Gb\",\"seed\":\"6\",\"leech\":\"5\",\"user\":\"myUsername\"}"))
        }

        println("ServerAPI::mockRequest() jsonResponse == ${jsonResponse}")
        println("ServerAPI::mockRequest() response == ${jsonToArray(jsonResponse)}")

        this.response = jsonToArray(jsonResponse)
    }
}