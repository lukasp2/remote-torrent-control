import socket
import json
import sys

class Server:
    @staticmethod
    def run():
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind(('127.0.0.1', 9999))
        s.listen()

        while True:
            # await connections
            clientsocket, address = s.accept() 
            print('Connected by', address)

            while True:
                data = clientsocket.recv(1024)

                if not data:
                    break

                if data['request'] == 'search_torrents':
                    # search torrents matching data['query']
                    # use beautiful soup?
                    # return list of results
                    msg = { 'results' : [] }

                elif data['request'] == 'status_check':
                    # make status check to transmission
                    # return status
                    msg = { 'status' : 0 }

                data = json.dumps(msg)
                try:
                    clientsocket.sendall(bytes(data, encoding="utf-8"))
                except socket.error:
                    print("Failed to send")
                    sys.exit()

        s.close()

    def search_torrents():
        # TODO: enable VPN
        # TODO: send https req
        # TODO: parse result
        # TODO: return torrent list
        pass

    def get_status():
        # TODO: query transmission for status
        # TODO: send status report
        pass

if __name__ == '__main__':
    Server.run()
