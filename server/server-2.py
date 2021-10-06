#!/usr/bin/env python3

import json
import socket
import threading

from torrent_handler import TorrentHandler

class Server:
    def __init__(self):
        self.HEADER = 64 # length of the header message which contains num. bytes in the next msg
        self.FORMAT = 'utf-8'
        self.PORT = 9999
        self.DISCONNECT_MSG = '!DISCONNECT'

        self.SERVER = socket.gethostbyname(socket.gethostname())
        self.ADDR = (self.SERVER, self.PORT)

        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind(self.ADDR)

    def handle_client(self, conn, addr):
        print(f"[NEW CONNECTION] {addr} connected.")
        connected = True
        while connected:
            msg_length = conn.recv(self.HEADER).decode(self.FORMAT)
            if not msg_length:
                continue
            msg = json.loads(conn.recv(msg_length).decode(self.FORMAT))
            print(f"[{addr}] {msg}")
            if msg['request'] == self.DISCONNECT_MSG:
                connected = False

        conn.close()

    def run(self):
        self.server.listen()
        print("[SERVER] Server is listening on {self.SERVER}")
        while True:
            conn, addr = self.server.accept()
            thread = threading.Thread(target=self.handle_client, args=(conn, addr))
            thread.start()
            print(f"[ACTIVE CONNECTIONS] {threading.active_count() - 1}")

if __name__ == '__main__':
    s = Server()
    s.run()