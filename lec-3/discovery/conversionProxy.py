#******************************************************************************
#
#  CS 6421 - Conversion Proxy
#  Execution:    python conversionProxy.py portnum
#  Author: Tim Stamler
#  Group: Malcolm Goldiner
#
#******************************************************************************

import socket
import sys

BUFFER_SIZE = 1024
interface = ""

printServerHost = "malcolmgoldiner@koding.io"
printServerPort = 5555

hostAddress = "timstamler@koding.io"

ouncesDollarsHost = "malcolmgoldiner@koding.io"
ouncesDollarsPort = 5555

dollarsYenHost = "timstamler@koding.io"
dollarsYenPort = 6666

inchesOuncesHost = "timstamler@koding.io"
inchesOuncesPort = 7777

def remoteConvert(host, port, args):
    clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    clientsocket.connect((host, port))
    print clientsocket.recv(BUFFER_SIZE)
    clientsocket.send(args[0] + " " + args[1] + " " + args[2] + "\n")
    return clientsocket.recv(BUFFER_SIZE)
    
    
def convert(conn, unit, userInput):
    if unit == "inches":
        result = remoteConvert(inchesOuncesHost, inchesOuncesPort, ["inches", "ounces", userInput])
        conn.send("Converted from inches to ounces: " + result + " ounces\n")
        result = remoteConvert(ouncesDollarsHost, ouncesDollarsPort, ["ounces", "dollars", result])
        conn.send("Converted from ounces to dollars: " + result + " dollars\n")
        return float(remoteConvert(dollarsYenHost, dollarsYenPort, ["dollars", "yen", result]))
        
    elif unit == "yen":
        result = remoteConvert(dollarsYenHost, dollarsYenPort, ["yen", "dollars", userInput])
        conn.send("Converted from yen to dollars: " + result)
        result = remoteConvert(ouncesDollarsHost, ouncesDollarsPort, ["dollars", "ounces", result])
        conn.send("Converted from dollars to ounces: " + result)
        return float(remoteConvert(inchesOuncesHost, inchesOuncesPort, ["ounces", "inches", result]))

## Function to process requests
def process(conn):
    conn.send("Welcome to the inches of bananas/yen converter!\n")

    # read userInput from client
    userInput = conn.recv(BUFFER_SIZE)
    if not userInput:
        print "Error reading message"
        sys.exit(1)

    inputList = userInput.split(" ")
    
    if len(inputList) < 3: 
        conn.send("Not enough input!\n")
        conn.close()
        return
    
    if inputList[0] == "inches" and inputList[1] != "yen":
        conn.send("Invalid input!\n")
        conn.close()
        return
        
    if inputList[0] == "yen" and inputList[1] != "inches":
        conn.send("Invalid input!\n")
        conn.close()
        return

    result = convert(conn, inputList[0], inputList[2])
        
    print "Received message: ", userInput
    
    conn.send("Final result: \n")
    conn.send(str(result) + " " + inputList[1] + "\n")

    conn.close()

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