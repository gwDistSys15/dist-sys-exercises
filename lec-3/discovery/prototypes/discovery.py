#!/usr/bin/env python

# Ahsen Uppal
#
# CS 6421 - Simple Python based discovery server, including path find.
# Uses a dictionary dispatch table to process simple get
# and set commands.
# 
# For path-finding, it builds an internal graph representation of the
# conversion server network, and processes conversion requests by
# doing a shortest-path traversal through the conversion network.
#

import socket, sys
import traceback
import numpy as np
from collections import defaultdict

BUFFER_SIZE = 1024

# Key-Value stores
# Tricky data structure for unit_to_server, a dict of dicts
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
    return 'Success\n'


# Look up a a single conversion server from u1 to u2
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

# A list of all available units, and conversions to and from unit ids
units = ('ft', 'in', 'cm', 'm', 'kg', 'g', 'lbs', 'b', '$', 'y')
n_units = len(units)
unit_to_id = {}
id_to_unit = {}
for i,e in enumerate(units):
    unit_to_id[e] = i
    id_to_unit[i] = e

MAX_DIST = 99

# Find paths using the Floyd-Warshall
# algorithm for all-pairs shortest distances.
def compute_distances(edges, n_units):
    dist = np.zeros((n_units, n_units), dtype=int)
    next = np.empty((n_units, n_units), dtype=int)
    next.fill(-1)

    for i in range(n_units):
        for j in range(n_units):
            dist[i][j] = MAX_DIST

    for i in range(n_units):
        dist[i][i] = 0

    for i in range(n_units):
        for j in range(n_units):
            if edges[i][j] > 0:
                dist[i][j] = edges[i][j]
                next[i][j] = j

    for k in range(n_units):
        for i in range(n_units):
            for j in range(n_units):
                if dist[i][j] > dist[i][k] + dist[k][j]:
                    dist[i][j] = dist[i][k] + dist[k][j]
                    next[i][j] = next[i][k]

    return (next, dist)

# Path reconstruction from next-neighbor matrix.
def get_path(src, dst):
    print("Get path %s %s\n" % (src, dst))

    edges = np.zeros((n_units, n_units), dtype=int)
    for i in unit_to_server.items():
        u0 = i[0][0]
        u1 = i[0][1]
        edges[unit_to_id[u0]][unit_to_id[u1]] = 1

    next,dist = compute_distances(edges, n_units)

    path = []
    u = unit_to_id[src]
    v = unit_to_id[dst]

    if next[u, v] == -1:
        return path

    while u != v:
        u = next[u, v]
        path.append(u)

    return path


# Look up a path from u1 to u2
def cmd_path(tokens):
    if len(tokens) != 3:
        return 'Failure invalid command. Expected: path u1 u2.\n'
    u1,u2 = tokens[1:]

    if u1 not in unit_to_id:
        return 'Failure invalid unit1\n'

    if u2 not in unit_to_id:
        return 'Failure invalid unit2\n'

    p = get_path(u1, u2)

    if not p:
        msg = "Failure could not find a suitable input conversion server :(\n"
    else:
        msg = ""
        u = unit_to_id[u1]
        for i in p:
            v = i
            s,d = id_to_unit[u],id_to_unit[v]
            # Tricky dereference: lookup in a dictionary, get a dictionary, get the first entry, and its key.
            host,port = unit_to_server[(s,d)].items()[0][0]
            msg += ('Query %s %s to server at %s %s\n' % (s, d, host, port))
            u = v
    return msg


# Dict for command dispatch.
commands = { "add" : cmd_add,
             "remove" : cmd_remove,
             "lookup" : cmd_lookup,
             "path" : cmd_path,
             }


# The standard recv function is not neccessarily line-delimited.
def recv_lines(conn):
    if not conn in recv_lines.keep_line:
        recv_lines.keep_line[conn] = ''
    lines_recieved = 0
    while lines_recieved == 0:
        buf = conn.recv(BUFFER_SIZE).decode('UTF-8')
        print("conn.recv buf: %s" % buf)
        if not buf:
            yield None
        lines = buf.splitlines(True)
        for l in lines:
            if l.endswith('\n'):
                yield recv_lines.keep_line[conn] + l
                recv_lines.keep_line[conn] = ''
                lines_recieved = 1
            else:
                recv_lines.keep_line[conn] += lines[-1]
recv_lines.keep_line = {}


# Main processing function for a single connection
def process(conn):
    greeting = "Welcome, you are connected to a Python-based simple command server.\n"
    conn.sendall(greeting.encode('UTF-8'))

    for userInput in recv_lines(conn):
        if not userInput:
            print("Error reading message")
            return

        sys.stdout.write("Received message: %s\n" % userInput)
        tokens = userInput.lower().split()

        if not tokens:
            # Empty line received
            continue

        try:
            if tokens[0] in commands:
                print("Processing command.")
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
        print("Started Python-based discovery command server on port %s" % (portnum))
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
