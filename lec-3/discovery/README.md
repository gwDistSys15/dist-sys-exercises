# Introduction

This is the HW 4 submission for Ahsen Uppal and Luona Guo, Zhenyu Han, Zuocheng Ding, Qi Liu, Yi Zhou.
This shows an example of a Python-based discovery conversion server
that supports add, remove, and lookup. For extra credit, this
discovery server includes path-finding.

For path-finding, it builds an internal graph representation of the
conversion server network, and processes conversion requests by doing
a shortest-path traversal through the conversion network.

# Protocol
The protocol supports add, remove, lookup, and path commands. In the
following format:


# Connecting
The discovery server accepts line-delimited ascii commands on a well-known
TCP port. In our testing we used port 5555.

# Commands

## Add
```
add unit_src unit_dst conversion-host conversion-port\n
```
This adds a source and destination unit to the discovery server.

## Add Response
```
Success\n
```
or
```
Failure\n
```

## Remove
```
remove host port\n
```
This removes a previously added conversion server.

## Remove Response
```
Success\n
```
or
```
Failure\n
```


# Lookup
```
lookup unit1 unit2\n'
```

This queries the discovery server for a single source and destination
unit pair and returns the host and port associated with it.
Response: `host port\n` or `Failure\n`


## Lookup Response
```
host port\n
```
or
```
Failure\n
```

# Path finding
```
path unit1 unit2\n'
```

This queries the discovery server for a conversion path between the source and destination
unit pair and returns the list of hosts and ports along that path.


## Path Response
```
host1 port1\n
host2 port2\n
...
```
or
```
Failure\n
```
