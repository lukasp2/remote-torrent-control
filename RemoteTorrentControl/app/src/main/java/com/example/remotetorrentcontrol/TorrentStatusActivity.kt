package com.example.remotetorrentcontrol

import android.view.View
import android.widget.ListView
import org.json.JSONObject

class StatusActivity : BaseActivity(R.layout.activity_status) {
    fun sendStatusRequest(view: View) {
        val data = JSONObject()
        data.put("request", "status_check")

        super.send(data)
        val response = super.receive()

        updateUI(response)
    }

    // update UI
    private fun updateUI(response : Response) {
        val entryAdapter = StatusEntryAdapter(this, response)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = entryAdapter
    }

    // go mack to main menu
    fun goBack(view: View) {
        super.goTo(MainActivity())
    }
}