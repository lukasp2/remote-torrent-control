package com.example.remotetorrentcontrol

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/* For displaying lists of data.
 * - used for inheritance by all .kt's that communicate with server and displays response.
 */
open class EntryAdapter(private val context: Context)
    : BaseAdapter() {
    protected open var data: ArrayList<Map<String, String>> = ArrayList()

    protected val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Map<String, String> {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // override this method to update the UI
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        throw Exception("EntryAdapter::getView(): You need to override me!")
    }
}