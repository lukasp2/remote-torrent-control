package com.example.remotetorrentcontrol

import android.view.View
import android.widget.ListView
import org.json.JSONObject

class TorrentStatusActivity : BaseActivity(R.layout.activity_status) {
    // sends status request to server
    // TODO: make @onClick method
    fun sendStatusRequest(view: View) {
        val data = JSONObject()
        data.put("request", "status_check")

        super.send(data)
        val response = super.receive()
        println("TorrentStatusActivity::updateUI() got response $response")
        updateUI(response)
        println("TorrentStatusActivity::updateUI() DONE")
    }

    // update UI
    private fun updateUI(response : Response) {
        println("TorrentStatusActivity::updateUI()")
        val entryAdapter = StatusEntryAdapter(this, response)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = entryAdapter
    }

    // go mack to main menu
    fun goBack(view: View) {
        super.goTo(MainActivity())
    }
}
