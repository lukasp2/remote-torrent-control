#!/usr/bin/env python3

import socket
import json
import sys

import torrent_handler

class Server:
    @staticmethod
    def run():
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind(('', 9999))
        s.listen()

        while True:
            print('waiting for connection ...')
            clientsocket, address = s.accept() 
            print('connected by', address)

            while True:
                data = clientsocket.recv(1024)

                if not data:
                    break
                
                data = json.loads(data)

                if data['request'] == 'search_torrents':
                    query = data['request']
                    torrents = asyncio.get_event_loop().run_until_complete(
                        TorrentHandler.search_torrents(query)
                    )
                    msg = {
                        'request' : data['request'],
                        'response' : torrents
                        }

                elif data['request'] == 'download':
                    magnet = data['magnet_link']
                    msg = { 
                        'request' : data['request'],
                        'response' : TorrentHandler.start_download(magnet_link)
                        }

                elif data['request'] == 'status_check':
                    msg = { 
                        'request' : data['request'],
                        'response' : TorrentHandler.check_torrent_status(),
                        }
                    
                data = json.dumps(msg)
                try:
                    clientsocket.sendall(bytes(data, encoding="utf-8"))
                    print('sent', repr(data), 'to client')
                except socket.error:
                    print("Failed to send")
                    sys.exit()

        s.close()

if __name__ == '__main__':
    Server.run()
