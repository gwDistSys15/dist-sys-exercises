#!/usr/bin/env python

#******************************************************************************
#
#  CS 6421 - Simple Conversation
#  implement convertion between bananas and inches of bananas
#  Execution:    python convServer_2.py portnum
#
#******************************************************************************

import socket
import sys
DISCOV_IP = '127.0.0.1'
DISCOV_PORT = 5555
BUFFER_SIZE = 1024

## Function to send msg to discover server
def send_to_discov(msg): 
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((DISCOV_IP,DISCOV_PORT))   #get connetion
        sock.send(msg) #send messages
        result = sock.recv(BUFFER_SIZE);
        print result
        sock.close()
    except socket.error, arg:
        print 'operations failed. More info:', arg
        sock.close()   #close socket
        sys.exit(-1)

## Function to process requests
def process(conn):
    conn.send("Welcome to the Bananas (b) to Inches (in) conversion server!\n")

    # read userInput from client
    userInput = conn.recv(BUFFER_SIZE)
    if not userInput:
        print "Error reading message"
        sys.exit(1)

    print "Received message: ", userInput
    
    # TODO: add convertion function here, reply = func(userInput)
    mylist = userInput.split(" ")
    # excption handler
    if len(mylist) != 3:
        conn.send('pls input 3 arguements. Usage: eg. b in 6 or in b 6\n')
    elif mylist[0] == mylist[1] or mylist[0] != 'b' and mylist[0] != 'in' or mylist[1] != 'b' and mylist[1] != 'in':
        conn.send('Wrong input. Usage: eg. b in 2 or in b 2\n');
    else:
        # send convertion result
        if mylist[0] == 'b':
            conn.send(str(float(mylist[2]) * 6)+'\n');
        elif mylist[0] == 'in':
            conn.send(str(float(mylist[2]) / 6)+'\n');
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

# get local ip address and port
l_addr = socket.gethostbyname(socket.gethostname())
l_port = sys.argv[1];

# wait for connection from the client
s.bind((interface, portnum))
s.listen(5)

send_to_discov('add b in ' + l_addr + ' ' + l_port + '\n')

exit_flag = False
try:
    print("Started Python-based command server on port %s" % (portnum))
    while not exit_flag:
        conn, addr = s.accept()
        print ('Accepted connection from client ', addr)
        try:
            process(conn)
        except:
            traceback.print_exc(file=sys.stdout)
            print ('Failed to process request from client. Continuing.')
        conn.close()
except KeyboardInterrupt:
    exit_flag = True

print("Exiting...")
send_to_discov('remove ' + l_addr + ' ' + l_port + '\n')
s.close()
sys.exit(0)
