package com.example.remotetorrentcontrol

// when adding a new config parameter in config.json, all you need to do is add it here
// and access it through configHandler.getConfig()
data class Config(
    val HEADER_SIZE : Int,
    val DISCONNECT_MSG : String,
    val FAIL_MSG : String,
    val SERVER : String,
    val PORT : Int,
    val FORMAT : String)

class ConfigHandler() {
    // TODO: read config from file

    //val jsonConfigStr = {}.javaClass.getResource("config.json").readBytes().toString(Charsets.UTF_8)
    //val file = File("config.json")
    //val stream = file.inputStream()
    //val bytes = stream.readBytes()
    //val jsonConfigStr = bytes.toString(Charsets.UTF_8)

    //private val jsonConfigStr = File("src/config.json").inputStream().readBytes().toString(Charsets.UTF_8)
    // private val config = Klaxon().parse<Config>(jsonConfigStr) as Config

    private val config = Config(64, "!DISCONNECT", "!FAIL", "10.8.2.5", 9999, "utf-8")

    fun getConfig() : Config {
        return this.config
    }
}
