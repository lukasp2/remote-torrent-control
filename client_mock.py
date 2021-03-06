#!/usr/bin/env python3

import socket
import json

class Client:
    def __init__(self):
        f = open('config.json',)
        self.config = json.load(f)
        f.close

        self.ADDR = (self.config['SERVER'], self.config['PORT'])
        self.client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client.connect(self.ADDR)

    def send(self, msg):
        msg = json.dumps(msg)
        msg = msg.encode(self.config['FORMAT'])
        msg_length = len(msg)
        send_length = str(msg_length).encode(self.config['FORMAT'])
        send_length += b' ' * (self.config['HEADER_SIZE'] - len(send_length))

        try:
            self.client.send(send_length)
            print(f'[ INFO ] sent header to server: \"{send_length}\"')
            self.client.send(msg)
            print(f'[ INFO ] sent query to server: {msg}') 
            response = self.receive()
            print(f'[ INFO ] received from server: {response}')
        except socket.error:
            print('[ ERROR ] communication failed, exiting ...')
            conn.close()
            sys.exit()

        # TODO: do something with response

    def receive(self):
        msg_length = self.client.recv(self.config['HEADER_SIZE']).decode(self.config['FORMAT'])
        if not msg_length:
            return

        try:
            data = self.client.recv(int(msg_length))
        except socket.error:
            print('[ ERROR ] recieve failed, exiting ...')
            sys.exit()

        data = json.loads(data.decode(self.config['FORMAT']))

        return data

# mocks user queries to server
class User:
    def __init__(self):
        self.client = Client()

    def run(self):

        print("~~ Welcome, this is a great UI ~~")
        print("1. msg = {'request': 'search_torrents', 'query': 'batman'}")
        print("2. msg = {'request': 'status_check'}")
        print("3. msg = {'request': 'download', 'magnet' : '<this is a magnet>'}")
        print("4. msg = {'request': self.client.config['DISCONNECT_MSG'] }")
        print('')
        print('> Enter the query you would like to mock ...')
        print('> ', end='')
        x = int(input())
        
        if x == 1:
            msg = {'request': 'search_torrents', 'query': 'batman'}
        elif x == 2:
            msg = {'request': 'status_check'}
        elif x == 3:
            msg = {'request': 'download', 'magnet' : '<this is a magnet>'}
        elif x == 4:
            msg = {'request': self.client.config['DISCONNECT_MSG'] }

        response = self.client.send(msg)

if __name__ == '__main__':
    u = User()
    u.run()
