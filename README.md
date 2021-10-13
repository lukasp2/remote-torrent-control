# Remote Torrent Control
An android application which acts as a remote control for your torrent server. It's not illegal if you never run it ;)

Client app is written in Kotlin and developed in Android Studio. Server side scripts ```server```, ```torrent_handler``` and ```client_mock``` are written in Python.

## Setup
* client
  * install the app
* torrent server
  * setup split tunneling
  * setup nordvpn
  * setup transmission-remote https://pimylifeup.com/raspberry-pi-transmission/
  * run ```server.py```
* router (optional, for external access only)
  * set dynDNS e.x. myhome.homefpt.net
  * port forward 9999 to <rasp pi IP> 9999

## TODO
### client (android device) 
- [X] read config.json for setting of server, port and pwd
- [X] create UI for search torrent activity
- [X] send search torrents request
- [X] send download torrent request
- [X] recieve answers
- [ ] display answers
### server (raspberry pi)
- [X] listen on port 9999
- [X] recieve torrent search / status requests
- [X] send search request to torrent website
- [X] open magnet link in transmission
- [ ] enable split tunneling
- [ ] enable VPN connection
- [X] check VPN connection
- [X] check status of transmission
- [X] send answers to sender
 ### router (optional, for external access only)
- [ ] setup dynDNS
- [ ] setup portforwarding
