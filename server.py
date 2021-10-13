#!/usr/bin/env python3

import sys
import json
import socket
import threading
import asyncio

from torrent_handler import TorrentHandler

class Server:
    def __init__(self):
        f = open('config.json',)
        self.config = json.load(f)
        f.close

        self.torrentHandler = TorrentHandler(self.config)
        
        self.ADDR = (self.config['SERVER'], self.config['PORT'])
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind(self.ADDR)

    # run by a thread handling each client
    def handle_client(self, conn, addr):
        print(f'[ INFO ] {addr} connected')
        
        connected = True
        while connected:
            msg_length = conn.recv(self.config['HEADER_SIZE'])

            if not msg_length:
                continue

            msg_length = int(msg_length.decode(self.config['FORMAT']))
            
            try:
                data = conn.recv(msg_length)
            except socket.error:
                print('[ ERROR ] recieve failed, exiting ...')
                conn.close()
                sys.exit()

            data = json.loads(data.decode(self.config['FORMAT']))
            print(f'[ INFO ] request {data} from {addr}')

            if data['request'] == self.config['DISCONNECT_MSG']:
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
        msg = msg.encode(self.config['FORMAT'])
        msg_length = len(msg)
        send_length = str(msg_length).encode(self.config['FORMAT'])
        send_length += b' ' * (self.config['HEADER_SIZE'] - len(send_length))

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
            data = self.config['FAIL_MSG']

        response = {
            'data' : data
        }
   
        return response

    # main loop
    def run(self):
        self.server.listen()

        print(f'[ INFO ] listening on {self.config["SERVER"]} ...')

        while True:
            conn, addr = self.server.accept()
            thread = threading.Thread(target=self.handle_client, args=(conn, addr))
            thread.start()
            print(f'[ INFO ] {threading.active_count() - 1} active connection(s)')

if __name__ == '__main__':
    s = Server()
    s.run()
    
