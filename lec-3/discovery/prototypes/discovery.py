#!/usr/bin/env python

# Ahsen Uppal
#
# CS 6421 - Simple Python based single value store server.
# Uses a dictionary dispatch table to process simple single value get
# and set commands.
#

import socket, sys
import traceback
from collections import defaultdict

BUFFER_SIZE = 1024

# Key-Value store
unit_to_server = defaultdict(dict)
server_to_unit = {}

def cmd_add(tokens):
    if len(tokens) != 5:
        return 'Failure invalid command. Expected: add u1 u2 host port.\n'
    u1,u2,host,port = tokens[1:]
    if (host,port) in unit_to_server[(u1,u2)]:
        return 'Failure entry exists.\n'
    unit_to_server[(u1,u2)][(host,port)] = 1
    server_to_unit[(host,port)] = (u1,u2)
    print (unit_to_server)
    return 'Success\n'

def cmd_remove(tokens):
    if len(tokens) != 3:
        return 'Failure invalid command. Expected: remove host port.\n'
    host,port = tokens[1:]
    if not (host,port) in server_to_unit:
        return 'Failure entry does not exist.\n'
    u1,u2 = server_to_unit[(host,port)]

    del server_to_unit[(host,port)]
    del unit_to_server[(u1,u2)][(host,port)]

    if len(unit_to_server[(u1,u2)]) == 0:
           del unit_to_server[(u1,u2)]

    print(unit_to_server)
    print(server_to_unit)

    return 'Success\n'

def cmd_lookup(tokens):
    if len(tokens) != 3:
        return 'Failure invalid command. Expected: lookup u1 u2.\n'
    u1,u2 = tokens[1:]
    if not (u1,u2) in unit_to_server:
        return 'None.\n'
    L = [i for i in unit_to_server[(u1,u2)].items()]
    host,port = L[0][0]
    msg = '%s %s\n' % (host,port)
    return msg

commands = { "add" : cmd_add,
             "remove" : cmd_remove,
             "lookup" : cmd_lookup,
             }


# The standard recv function is not neccessarily line-delimited.
def recv_lines(conn):
    buf = conn.recv(BUFFER_SIZE).decode('UTF-8')
    end_newline = (buf[-1] == '\n')
    lines = buf.split('\n')
    if not conn in recv_lines.keep_line:
        recv_lines.keep_line[conn] = ''
    for l in lines[0:1]:
        yield recv_lines.keep_line[conn] + lines[0]
    recv_lines.keep_line[conn] = {}
    for l in lines[1:-1]:
        yield l
    if end_newline:
        yield lines[-1]
    else:
        recv_lines.keep_line[conn] = lines[-1]
recv_lines.keep_line = {}


def process(conn):
    greeting = "Welcome, you are connected to a Python-based simple command server.\n"
    conn.sendall(greeting.encode('UTF-8'))

    for userInput in recv_lines(conn):
        if not userInput:
            print("Error reading message")
            return

        sys.stdout.write("Received message: %s" % userInput)
        tokens = userInput.lower().split()

        try:
            if tokens[0] in commands:
                print("Processing command")
                response = commands[tokens[0]](tokens)
            else:
                response = ("Invalid command: %s\n" % tokens[0])

            sys.stdout.write("Sending response: %s" % response)
            conn.sendall(response.encode('UTF-8'))
        except:
            traceback.print_exc(file=sys.stdout)
            msg = ("Exception occurred: %s\n" % (userInput))
            conn.sendall(msg.encode('UTF-8'))

    return


if __name__ == '__main__':
    interface = ""

    if len(sys.argv) < 2:
        sys.stderr.write("usage: python {0} portnum\n".format(sys.argv[0]))
        sys.exit(1)

    portnum = int(sys.argv[1])
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((interface, portnum))
    s.listen(5)
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
            print('Closing connection.')
            conn.close()
    except KeyboardInterrupt:
        exit_flag = True

    print("Exiting...")
    s.close()
    sys.exit(0)
