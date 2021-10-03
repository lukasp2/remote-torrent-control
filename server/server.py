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

                    # do something with return value (data)
                    print(data)

                    # like this we can send something back 
                    clientsocket.sendall(data)

            #clientsocket.close()

if __name__ == '__main__':
    Server.run()
