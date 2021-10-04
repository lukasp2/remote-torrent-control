#!/usr/bin/env python3

import socket
import sys
import json

class Client:
    @staticmethod
    def run():        
        HOST = '127.0.0.1'  # server's hostname or IP address
        PORT = 9999         # server's port

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((HOST, PORT))

            # input query you would like to mock
            x = int(input())

            if x == 1:
                msg = {"request": "search_torrents", "query": "batman"}
            elif x == 2:
                msg = {"request": "status_check"}
            
            data = json.dumps(msg)
            try:
                s.sendall(bytes(data, encoding="utf-8"))
            except socket.error:
                print("Failed to send")
                sys.exit()

            data = json.loads(s.recv(1024))

        print('Received', data)

if __name__ == '__main__':
    Client.run()
