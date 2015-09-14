#!/usr/bin/env python
# for distributed system
# simple python server: usage: python convServer.py portnum

import socket
import sys



# define buffer size here
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

# accept connection and print out info of client
conn, addr = s.accept()
print '\n'
print 'Receive from:', addr

# read query from client
query = conn.recv(BUFFER_SIZE)
if not query:
    print "no query found"
    sys.exit(1)

print "Received query:", query
reply = "Welcome,connected"
# TODO: add convertion function here, reply = func(query)



conn.send(reply + "\n")
conn.close()