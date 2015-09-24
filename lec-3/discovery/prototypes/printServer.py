#!/usr/bin/env python

#******************************************************************************
#  Print Server
#  CS 6421 - discovery
#  Execution:    python printServer.py portnum
#  Shuo Zhang (zhangshuo@gwu.edu)
#******************************************************************************

import socket
import sys

## Function to process requests
def process(conn):
    # Server sends a one line message to client saying what the server can do.
    conn.send("Welcome to the pint server!\n")

    # read userInput from client
    userInput = conn.recv(BUFFER_SIZE)
    if not userInput:
        print "Error reading message"
        sys.exit(1)

    print "Received message: ", userInput
    
    conn.close()


### Main code run when program is started
BUFFER_SIZE = 1024
interface = ""

# if input arguments are wrong, print out usage
if len(sys.argv) != 2:
    print >> sys.stderr, "usage: python {0} portnum\n".format(sys.argv[0])
    sys.exit(1)

portnum = int(sys.argv[1])

# create socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((interface, portnum))
s.listen(5)

while True:
    # accept connection and print out info of client
    conn, addr = s.accept()
    print 'Accepted connection from client', addr
    process(conn)
s.close()