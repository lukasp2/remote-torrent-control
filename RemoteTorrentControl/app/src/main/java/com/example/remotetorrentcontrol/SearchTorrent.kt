package com.example.remotetorrentcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import org.json.JSONObject

class SearchTorrent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_torrent)
    }

    fun search(view: View) {
        val sr = ServerRequest()

        val query = findViewById<EditText>(R.id.search_torrents).text
        val data = JSONObject()
        data.put("request", "search_torrents")
        data.put("query", query)

        sr.send(data)
    }

    fun back(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }
}