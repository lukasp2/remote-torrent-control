#!/usr/bin/env python3

import asyncio

import socket
import json
import sys

from torrent_handler import TorrentHandler

class Server:
    @staticmethod
    def run():
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind(('', 9999))
        s.listen()

        torrentHandler = TorrentHandler()

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
                    query = data['query']
                    torrents = asyncio.get_event_loop().run_until_complete(
                        torrentHandler.search_torrents(query)
                    )
                    msg = {
                        'request' : data['request'],
                        'response' : torrents
                        }

                elif data['request'] == 'download':
                    magnet = data['magnet']
                    msg = { 
                        'request' : data['request'],
                        'response' : torrentHandler.start_download(magnet)
                        }

                elif data['request'] == 'status_check':
                    msg = { 
                        'request' : data['request'],
                        'response' : torrentHandler.check_torrent_status(),
                        }
                    
                data = json.dumps(msg)
                try:
                    clientsocket.sendall(bytes(data, encoding="utf-8"))
                    print('sent', data, 'to client')
                except socket.error:
                    print("Failed to send")
                    sys.exit()

        s.close()

if __name__ == '__main__':
    Server.run()
