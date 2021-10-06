#!/usr/bin/env python3

import socket
import json

class Client:
    def __init__(self) -> None:
        self.HEADER = 64 # length of the header message which contains num. bytes in the next msg
        self.FORMAT = 'utf-8'
        self.PORT = 9999
        self.DISCONNECT_MSG = '!DISCONNECT'

        self.SERVER = '<servers ip here>'
        self.ADDR = (self.SERVER, self.PORT)

        self.client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client.connect(self.ADDR)

    def send(self, msg):
        msg = json.dumps(msg)
        msg = msg.encode(self.FORMAT)
        msg_length = len(msg)
        send_length = str(msg_length).encode(self.FORMAT)
        send_length += b' ' * (self.HEADER - len(send_length))
        self.client.send(send_length)
        self.client.message(msg)
        self.receive()

    def receive(self):
        msg_length = self.client.recv(self.HEADER).decode(self.FORMAT)
        if not msg_length:
            return
        msg = self.client.recv(msg_length).decode(self.FORMAT)
        print(f"[{self.SERVER}] {msg}")
        msg = json.loads(msg)
        return msg

class User:
    def __init__(self):
        self.client = Client()

    def send(self):
        # input query you would like to mock
        x = int(input())

        if x == 1:
            msg = {"request": "search_torrents", "query": "batman"}
        elif x == 2:
            msg = {"request": "status_check"}
        elif x == 3:
            msg = {"request": "download", "magnet" : "<this is a magnet>"}
        
        response = self.client.send(msg)

        # answer from server
        data = json.loads(s.recv(4096 * 8))

if __name__ == '__main__':
    c = Client()
    c.run()
