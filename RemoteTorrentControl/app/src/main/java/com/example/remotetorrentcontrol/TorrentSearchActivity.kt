package com.example.remotetorrentcontrol

import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.ListView
import org.json.JSONObject

class SearchActivity : BaseActivity(R.layout.activity_search_torrent) {
    fun search(view: View) {
        val query = findViewById<EditText>(R.id.search_torrents).text
        val data = JSONObject()
        data.put("request", "search_torrents")
        data.put("query", query)

        super.send(data)
        val response = super.receive()

        updateUI(response)
    }

    private fun updateUI(response : Response) {
        val statusAdapter = SearchEntryAdapter(this, response)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = statusAdapter
    }

    // go mack to main menu
    fun goBack(view: View) {
        super.goTo(MainActivity())
    }
}
