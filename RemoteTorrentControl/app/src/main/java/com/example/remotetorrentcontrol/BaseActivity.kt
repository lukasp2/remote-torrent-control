package com.example.remotetorrentcontrol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon
import com.google.gson.JsonObject
import org.json.JSONObject

// this class is a parent to all activity classes communicating with the server
open class BaseActivity(private val layout : Int) : AppCompatActivity() {
    private val server = ServerAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
    }

    // calls Server::send()
    protected fun send(data : JSONObject) {
        server.send(data)
    }

    // calls Server::receive() and returns server response
    protected fun receive() : Response {
        // TODO: make FAIL check on response
        return server.receive()
    }

    // navigate to another activity
    fun goTo(activity : AppCompatActivity) {
        val intent = Intent(this, activity::class.java).apply {}
        startActivity(intent)
    }
}