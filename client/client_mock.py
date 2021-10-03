#!/usr/bin/env python3

import socket
import sys

class Client:

    @staticmethod
    def run():        
        HOST = '127.0.0.1'  # The server's hostname or IP address
        PORT = 9999         # The port used by the server

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((HOST, PORT))
            try:
                s.sendall(b'Hello, world')
            except socket.error:
                print("Failed to send")
                sys.exit()
            data = s.recv(1024)

        print('Received', repr(data))

if __name__ == '__main__':
    Client.run()
