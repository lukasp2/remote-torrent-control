#!/usr/bin/env python3

import socket
import sys
import json

class Client:
    @staticmethod
    def run():        
        HOST = 'raspberrypi'  # server's hostname or IP address
        PORT = 9999         # server's port

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((HOST, PORT))

            # input query you would like to mock
            x = int(input())

            if x == 1:
                msg = {"request": "search_torrents", "query": "batman"}
            elif x == 2:
                msg = {"request": "status_check"}
            elif x == 3:
                msg = {"request": "download", "url" : ""}
            
            data = json.dumps(msg)
            try:
                s.sendall(bytes(data, encoding="utf-8"))
            except socket.error:
                print("failed to send, exiting ...")
                sys.exit()

            data = json.loads(s.recv(1024))

        print('received', data)

if __name__ == '__main__':
    Client.run()
