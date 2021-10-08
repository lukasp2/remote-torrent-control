package com.example.remotetorrentcontrol

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

data class ServerConfigs (val HOST : String = "192.168.1.78", val PORT : Int = 9999)

data class Response (val requestType : String, val data : ArrayList<Pair<String, String>>)

class ServerRequest {
    fun send(data : JSONObject): Response {
        val data = String(data.toString().toByteArray() , Charsets.UTF_8)
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
                out.write(data.toString())
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

        // MOCK
        /*
        val data_mock = ArrayList<Pair<String, String>>()
        data_mock.add(Pair("Sagan om Kungen","12"))
        data_mock.add(Pair("Bajonettmannen","23"))
        data_mock.add(Pair("BurgerKing foot lettuce","34"))
        data_mock.add(Pair("Haha","45"))

        val response_mock = Response("status_request", data_mock)
        */
        // MOCK

        val jsonResponse = JSONTokener(byteResponse).nextValue() as JsonElement
        val response = Gson().fromJson(jsonResponse, Response::class.java)

        return response
    }
}