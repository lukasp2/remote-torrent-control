package com.example.remotetorrentcontrol

import android.view.View
import android.widget.ListView
import org.json.JSONObject

class StatusActivity : BaseActivity(R.layout.activity_status) {
    // sends status request to server
    fun sendStatusRequest(view: View) {
        val data = JSONObject()
        data.put("request", "status_check")

        super.send(data)
        val response = super.receive()

        updateUI(response)
        println("[ DEBUG ] StatusActivity::sendStatusUpdate() 5")
    }

    // update UI
    private fun updateUI(response : Response) {
        println("[ DEBUG ] StatusActivity::updateUI() 1")
        val entryAdapter = StatusEntryAdapter(this, response)
        println("[ DEBUG ] StatusActivity::updateUI() 2")
        val listView = findViewById<ListView>(R.id.listview)
        println("[ DEBUG ] StatusActivity::updateUI() 3")
        listView.adapter = entryAdapter
        println("[ DEBUG ] StatusActivity::updateUI() 4")
    }

    // go mack to main menu
    fun goBack(view: View) {
        super.goTo(MainActivity())
    }
}