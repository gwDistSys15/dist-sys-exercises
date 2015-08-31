# CS 6421 - Simple Message Board Client in Python
# T. Wood
# Run with:     python msgclient.py

import socket

host = "twood02.koding.io";
portnum = 5555;

clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientsocket.connect((host, portnum))
clientsocket.send('YOUR NAME\n')
clientsocket.send('YOUR MESSAGE\n')

print("Sent message to server!")
