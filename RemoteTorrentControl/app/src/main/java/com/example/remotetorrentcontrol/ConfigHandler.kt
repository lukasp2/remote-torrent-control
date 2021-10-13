package com.example.remotetorrentcontrol

import com.beust.klaxon.Klaxon
import java.io.File

// when adding a new config parameter in config.json, all you need to do is add it here
// and access it through configHandler.getConfig()
data class Config(
    val HEADER_SIZE : Int,
    val DISCONNECT_MSG : String,
    val FAIL_MSG : String,
    val SERVER : String,
    val PORT : Int,
    val FORMAT : String)

// TODO: the server script reads from a config.py file :( fix the server
class ConfigHandler() {
    private val configPath = "..\\..\\..\\..\\..\\..\\..\\..\\config.json"
    private val jsonConfigStr = File(this.configPath).inputStream().readBytes().toString(Charsets.UTF_8)
    private val config = Klaxon().parse<Config>(jsonConfigStr) as Config

    fun getConfig() : Config {
        return this.config
    }
}