package com.example.remotetorrentcontrol

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/* For displaying lists of data.
 * - used for inheritance by all .kt's that communicate with server and displays response.
 */
open class EntryAdapter(private val context: Context) : BaseAdapter() {
    protected open var response: Response = Response()

    protected val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return response.data.size
    }

    override fun getItem(position: Int): Map<String, String> {
        return response.data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // override this method to update the UI
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        throw Exception("EntryAdapter::getView(): You need to override me!")
    }

    private fun setLoadImage() {
        //set load image in imageView
        //Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)
    }

    private fun getTypeface() {
        //val typeFace = ResourcesCompat.getFont(context, R.font.josefinsans_semibolditalic)
        //title.typeface = typeFace
        //status.typeface = typeFace
    }
}

// used by SearchTorrent::updateUI to update its UI
class SearchEntryAdapter(private val context: Context,
                         override var response: Response)
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

// used by Status::updateUI to update its UI
class StatusEntryAdapter(private val context: Context,
                         override var response: Response)
    : EntryAdapter(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.status_list, parent, false)

        val title = rowView.findViewById(R.id.item_name) as TextView
        val status = rowView.findViewById(R.id.item_status) as TextView

        val rowItem = getItem(position)

        title.text = rowItem["title"]
        status.text = rowItem["status"]

        return rowView
    }
}