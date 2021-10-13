package com.example.remotetorrentcontrol

import android.view.View

class MainActivity : BaseActivity(R.layout.activity_main) {
    // navigate to search torrent menu
    fun searchTorrents(view: View) {
        super.goTo(TorrentSearchActivity())
    }

    // navigate to torrent status menu
    fun checkStatus(view: View) {
        super.goTo(TorrentStatusActivity())
    }
}
