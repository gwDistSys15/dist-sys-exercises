#!/usr/bin/env python

#******************************************************************************
#
#  CS 6421 - Simple Conversion
#  Execution:    python dollarsYen.py portnum
#  Author: Tim Stamler
#  Group: Malcolm Goldiner
#
#******************************************************************************

import socket
import sys

yenConv = 100

printServerHost = "malcolmgoldiner@koding.io"
printServerPort = "5555"

hostAddress = "timstamler@koding.io"
    
def convert(unit, userInput):
    if unit == "dollars":
        return float(userInput)*yenConv
    elif unit == "yen":
        return float(userInput)/yenConv

## Function to process requests
def process(conn):
    conn.send("Welcome to the yen/dollars converter!\n")

    # read userInput from client
    userInput = conn.recv(BUFFER_SIZE)
    if not userInput:
        print "Error reading message"
        sys.exit(1)

    inputList = userInput.split(" ")
    
    if inputList[0] == "yen" and inputList[1] != "dollars":
        conn.send("Invalid input!\n")
        return
        
    if inputList[0] == "dollars" and inputList[1] != "yen":
        conn.send("Invalid input!\n")
        return

    result = convert(inputList[0], inputList[2])
        
    print "Received message: ", userInput
    
    conn.send(str(result) + "\n")

    conn.close()


### Main code run when program is started
BUFFER_SIZE = 1024
interface = ""

# if input arguments are wrong, print out usage
if len(sys.argv) != 2:
    print >> sys.stderr, "usage: python {0} portnum\n".format(sys.argv[0])
    sys.exit(1)

portnum = int(sys.argv[1])

#get current host address
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) 
s.connect(('google.com', 0)) 
ip = s.getsockname()[0]
s.close()

#report information
discServSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
discServSocket.connect((printServerHost, printServerPort))
discServSocket.send(ip + " " + sys.argv[1] + "\n") #waiting on protocol
discServSocket.close()

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