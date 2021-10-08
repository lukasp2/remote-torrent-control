#!/usr/bin/env python3

import sys
import json
import socket
import threading
import asyncio

from torrent_handler import TorrentHandler
from config import CONFIG

class Server:
    def __init__(self):
        self.HEADER_SIZE = CONFIG["HEADER_SIZE"]
        self.FORMAT = CONFIG["FORMAT"]

        self.DISCONNECT_MSG = CONFIG["DISCONNECT_MSG"]
        self.FAIL_MSG = CONFIG["FAIL_MSG"]

        self.SERVER = CONFIG["SERVER"] #socket.gethostbyname(socket.gethostname())
        self.PORT = CONFIG["PORT"]
        self.ADDR = (self.SERVER, self.PORT)

        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind(self.ADDR)

        self.torrentHandler = TorrentHandler()

    # run by a thread handling each client
    def handle_client(self, conn, addr):
        print(f'[ INFO ] {addr} connected')
        #asyncio.set_event_loop(asyncio.new_event_loop())
        
        connected = True
        while connected:
            msg_length = conn.recv(self.HEADER_SIZE).decode(self.FORMAT)

            if not msg_length:
                continue
            
            try:
                data = conn.recv(int(msg_length))
            except socket.error:
                print('[ ERROR ] recieve failed, exiting ...')
                conn.close()
                sys.exit()

            data = json.loads(data.decode(self.FORMAT))
            print(f'[ INFO ] request {data} from {addr}')

            if data['request'] == self.DISCONNECT_MSG:
                connected = False
                continue

            response = self.get_response(data)

            print(f'[ INFO ] answered {data} to {addr}')
            self.send(response, conn, addr)
            
        conn.close()
        
        print(f'[ INFO ] {addr} disconnected')

    # sends message to addr
    def send(self, msg, conn, addr):
        msg = json.dumps(msg)
        msg = msg.encode(self.FORMAT)
        msg_length = len(msg)
        send_length = str(msg_length).encode(self.FORMAT)
        send_length += b' ' * (self.HEADER_SIZE - len(send_length))

        try:
            conn.send(send_length)
            print(f'[ INFO ] sent header to {addr}: \"{send_length}\"')
            conn.send(msg)
            print(f'[ INFO ] sent response to {addr}: {msg}')
        except socket.error:
            print(f'[ ERROR ] send failed, exiting ...')
            conn.close()
            sys.exit()

    # gets response to be sent back to client
    def get_response(self, data):
        if data['request'] == 'search_torrents':
            data = asyncio.new_event_loop().run_until_complete(
                self.torrentHandler.search_torrents(data['query']))
        elif data['request'] == 'status_check':
            data = self.torrentHandler.check_torrent_status(),
        elif data['request'] == 'download':
            data = self.torrentHandler.start_download(data['magnet'])
        else:
            data = '!FAIL'

        response = {
            'data' : data
        }
   
        return response

    # main loop
    def run(self):
        self.server.listen()

        print(f'[ INFO ] listening on {self.SERVER} ...')

        while True:
            conn, addr = self.server.accept()
            thread = threading.Thread(target=self.handle_client, args=(conn, addr))
            thread.start()
            print(f'[ INFO ] {threading.active_count() - 1} active connection(s)')
            
if __name__ == '__main__':
    s = Server()
    s.run()
    
