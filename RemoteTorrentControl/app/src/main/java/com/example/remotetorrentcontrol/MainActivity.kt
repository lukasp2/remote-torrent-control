package com.example.remotetorrentcontrol

import android.view.View

class MainActivity : BaseActivity(R.layout.activity_main) {
    init {
        // TODO: check if this is run every time we visit main or just first time
        println("[ HELLOO OOOOO ]")
        // TODO: read config
    }

    // navigate to search torrent menu
    fun searchTorrents(view: View) {
        super.goTo(TorrentSearchActivity())
    }

    // navigate to torrent status menu
    fun checkStatus(view: View) {
        super.goTo(TorrentStatusActivity())
    }
}
