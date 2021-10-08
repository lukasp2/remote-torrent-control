package com.example.remotetorrentcontrol

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import org.json.JSONObject

class Status : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
    }

    fun sendStatusRequest(view: View) {
        val data = JSONObject()
        data.put("request", "status_check")

        val sr = ServerRequest()
        val response = sr.send(data)

        updateUI(view, response)
    }

    // update UI
    private fun updateUI(view : View, response : Response) {
        val statusAdapter = StatusEntryAdapter(this, response.data)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = statusAdapter
    }

    fun back(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }
}

class StatusEntryAdapter(private val context: Context,
                         private val dataSource: ArrayList<Pair<String, String>>)
    : BaseAdapter() {
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Pair<String, String> {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.status_list, parent, false)

        val title = rowView.findViewById(R.id.item_name) as TextView
        val status = rowView.findViewById(R.id.item_status) as TextView

        //val typeFace = ResourcesCompat.getFont(context, R.font.josefinsans_semibolditalic)
        //title.typeface = typeFace
        //status.typeface = typeFace

        val rowItem = getItem(position)

        title.text = rowItem.first
        status.text = rowItem.second

        // Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        return rowView
    }
}
