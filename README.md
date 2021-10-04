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
* router
  * dynDNS
  * portforwarding
* sender (android device)
  * send search torrents request
  * send download torrent request
  * recieve answers
* reciever <raspberry pi>
  * enable/check VPN connection
  * listen on port 9999
  * recieve search / download torrent requests
  * send request to torrent website
  * send answers to sender
