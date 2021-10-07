package com.example.remotetorrentcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import org.json.JSONObject

class Status : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
    }

    fun statusRequest(view: View) {
        val sr = ServerRequest()

        val data = JSONObject()
        data.put("request", "status_check")
        var responseMap = sr.send(data)

        // TODO: set result in arrayList
        val listView = findViewById<ListView>(R.id.listview)

        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("2")
        arrayList.add("3")
        arrayList.add("4")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)

        listView.adapter = arrayAdapter
    }

    fun back(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }
}
