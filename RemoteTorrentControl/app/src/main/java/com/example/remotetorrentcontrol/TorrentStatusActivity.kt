package com.example.remotetorrentcontrol

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.google.gson.*
import org.json.JSONObject

class StatusActivity : BaseActivity(R.layout.activity_status) {
    class Respose {
        val data : Map<String, String> = HashMap()
    }

    fun sendStatusRequest(view: View) {
        val data = JSONObject()
        data.put("request", "status_check")

        super.send(data)
        val response = super.receive()

        println("[ INFO ] mapToArray ...")

        val array = jsonToArray(response)

        updateUI(array)
    }

    // update UI
    private fun updateUI(response : ArrayList<Map<String, String>>) {
        val entryAdapter = StatusEntryAdapter(this, response)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = entryAdapter
    }

    // navigate to another menu
    fun goBack(view: View) {
        super.goTo(MainActivity())
    }
}

// used by Status::updateUI to update its UI
class StatusEntryAdapter(private val context: Context,
                         override var data: ArrayList<Map<String, String>>)
    : EntryAdapter(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.status_list, parent, false)

        val title = rowView.findViewById(R.id.item_name) as TextView
        val status = rowView.findViewById(R.id.item_status) as TextView

        //val typeFace = ResourcesCompat.getFont(context, R.font.josefinsans_semibolditalic)
        //title.typeface = typeFace
        //status.typeface = typeFace

        val rowItem = getItem(position)

        title.text = rowItem["title"]
        status.text = rowItem["status"]

        // Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        return rowView
    }
}