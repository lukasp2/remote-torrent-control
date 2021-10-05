package com.example.remotetorrentcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun searchTorrents(view: View) {
        val intent = Intent(this, SearchTorrent::class.java).apply {
        }
        startActivity(intent)
    }

    fun checkStatus(view: View) {
        val intent = Intent(this, Status::class.java).apply {
        }
        startActivity(intent)
    }
}
