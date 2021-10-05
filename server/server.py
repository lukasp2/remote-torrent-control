#!/usr/bin/env python3

import socket
import json
import sys

import scraper

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
                    # TODO: enable VPN
                    msg = {
                        'request' : 'search_torrents',
                        'response' : scraper.search_torrents(data['query'])
                        }

                elif data['request'] == 'status_check':
                    # TODO: query transmission for status
                    # TODO: send status report
                    msg = { 
                        'request' : 'status_check',
                        'response' : ['status and info for download #1'], # statuses
                        }
                    
                elif data['request'] == 'download':
                    pass
                    
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
