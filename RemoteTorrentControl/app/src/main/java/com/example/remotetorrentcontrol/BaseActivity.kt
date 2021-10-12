package com.example.remotetorrentcontrol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import org.json.JSONObject

// this class is a parent to all activity classes communicating with the server
open class BaseActivity(private val layout : Int) : AppCompatActivity() {
    private val server = ServerAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
    }

    // TODO: move this to where it belongs
    protected fun jsonToArray(json : JsonObject) : ArrayList<Map<String, String>>{
        val jsonStr = json.toString()
        println("json str: ${jsonStr}")

        val array = ArrayList<Map<String, String>>()

        // val mapEntry = Gson().fromJson(jsonStr, Map::class.java) as Map<String, String>

        return array
    }

    // calls Server::send()
    protected fun send(data : JSONObject) {
        server.send(data)
    }

    // calls Server::receive() and returns server response
    protected fun receive() : JsonObject {
        // TODO: make FAIL check on response
        return server.receive()
    }

    // navigate to another activity
    fun goTo(activity : AppCompatActivity) {
        val intent = Intent(this, activity::class.java).apply {}
        startActivity(intent)
    }
}