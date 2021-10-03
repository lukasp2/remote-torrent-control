import socket

class Server:
    @staticmethod
    def run():
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
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

                    # TODO: look at data, read the request, handle request
                    print(data)

                    # like this we can send something back 
                    clientsocket.sendall(data)
       
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
