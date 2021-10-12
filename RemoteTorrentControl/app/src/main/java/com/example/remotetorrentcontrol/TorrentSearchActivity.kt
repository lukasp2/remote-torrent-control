package com.example.remotetorrentcontrol

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject

class SearchActivity : BaseActivity(R.layout.activity_search_torrent) {
    fun search(view: View) {
        val query = findViewById<EditText>(R.id.search_torrents).text
        val data = JSONObject()
        data.put("request", "search_torrents")
        data.put("query", query)

        super.send(data)
        val response = super.receive()
        val array = jsonToArray(response)
        updateUI(array)
    }

    private fun updateUI(response : ArrayList<Map<String, String>>) {
        val statusAdapter = SearchEntryAdapter(this, response)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = statusAdapter
    }

    fun back(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }
}

// used by SearchTorrent::updateUI to update its UI
class SearchEntryAdapter(private val context: Context,
                         override var data: ArrayList<Map<String, String>>)
    : EntryAdapter(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = super.inflater.inflate(R.layout.status_list, parent, false)

        val title = rowView.findViewById(R.id.item_name) as TextView
        val filesize = rowView.findViewById(R.id.filesize) as TextView
        val seeders = rowView.findViewById(R.id.seeders) as TextView
        val leechers = rowView.findViewById(R.id.leechers) as TextView

        val rowItem = super.getItem(position)

        title.text = rowItem["name"]
        filesize.text = rowItem["size"]
        seeders.text = rowItem["seed"]
        leechers.text = rowItem["leech"]

        return rowView
    }
}