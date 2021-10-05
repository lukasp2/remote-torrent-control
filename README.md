# remote-torrent-control

## Setup
* router  
  * set dynDNS e.x. myhome.homefpt.net
  * port forward 9999 to <rasp pi IP> 9999
* andriod
  * install the app
* torrent server
  * run ```server.py``` on the server

## TODO
### client (android device) 
- [X] send search torrents request
- [X] send download torrent request
- [X] recieve answers
- [ ] display answers
### server (raspberry pi)
- [X] listen on port 9999
- [X] recieve torrent search / status requests
- [ ] enable/check VPN connection
- [ ] send search request to torrent website
- [ ] start download from torrent website
- [ ] check status of transmission
- [ ] send answers to sender
 ### router (for external access only)
- [ ] setup dynDNS
- [ ] setup portforwarding
