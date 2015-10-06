# Introduction

This is the HW 4 submission for Ahsen Uppal and Luona Guo, Zhenyu Han, Zuocheng Ding, Qi Liu, Yi Zhou.
This shows an example of a Python-based discovery conversion server
(in prototypes/discovery.py) that supports add, remove, and lookup. For extra credit, this
discovery server includes path-finding and support for unidirectional
servers (see the adduni command). And the proxy_conv_server.py
supports querying and using this protocol for path conversions. The
conversion servers in convServer.py here support registering and
de-registering with the discovery server upon startup and shutdown.

For path-finding, it builds an internal graph representation of the
conversion server network, and processes conversion requests by doing
a shortest-path traversal through the conversion network.

All the code is in the lec-3/discovery directory.

# Protocol
The protocol supports add, remove, lookup, and path commands. In the
following format:


# Connecting
The discovery server accepts line-delimited ascii commands on a well-known
TCP port. In our testing we used port 5555.

# Commands

## Bidirectional Add
```
add unit_src unit_dst conversion-host conversion-port\n
```
This adds a source and destination unit, and the reverse conversion as
well to the discovery server. Responds with a success or failure with
an optional reason as indicated below.

## Bidirectional Add Response
```
Success\n
```
or
```
Failure [reason]\n
```


## Unidirectional Add
```
adduni unit_src unit_dst conversion-host conversion-port\n
```
This adds a source and destination unit to the discovery
server. Responds with a success or failure with an optional reason as
indicated below.

## Unidirectional Add Response
```
Success\n
```
or
```
Failure [reason]\n
```

## Remove
```
remove host port\n
```
This removes a previously added conversion server. Responds with a
success or failure with an optional reason as indicated below.

## Remove Response
```
Success\n
```
or
```
Failure [reason]\n
```


# Lookup
```
lookup unit1 unit2\n
```

This queries the discovery server for a single source and destination
unit pair and returns the host and port associated with it.
Responds with a success (host port or "None." for not found) or
failure with an optional reason as indicated below.


## Lookup Response
```
host port\n
```
or
```
None.\n
```
or
```
Failure [reason]\n
```

# Path finding
```
path unit1 unit2\n
```

This queries the discovery server for a conversion path between the source and destination
unit pair and returns the list of hosts and ports along that path. It
returns failure with an optional reason for failure.


## Path Response
```
Query u1a u1b to server at host1 port1
Query u2a u2b to server at host2 port2
Query u3a u3b to server at host3 port3
...
```
or
```
Failure [reason]\n
```
Where u1a u1b is the first unit pair to query, u2a u2b is the second
and so on.


# Test Plan
To start the discovery server, just run with a port number.
Example:
```
python2 discovery.py 5555
```

Then you can add conversion servers to it. Multi-line messages are
supported! I have created several input files for ease of use. You can
of course, type each line from the input test files separately in
telnet or nc.


Example:
Basic bi-directional tests:
```
>cat input-03.txt
add in cm localhost 7777
lookup in cm
lookup cm in
remove localhost 7777
lookup in cm
lookup cm in

>nc localhost 5555 < input-03.txt
Welcome, you are connected to a Python-based simple command server.
Success
localhost 7777
localhost 7777
Success
None.
None.
```

Unidirectional tests:
```
>cat input-02.txt                      
adduni ft m localhost 5570
adduni m ft localhost 5571
adduni ft in localhost 5572
adduni in ft localhost 5573
adduni in cm localhost 5574
adduni cm in localhost 5575
adduni b m localhost 5576
adduni m b localhost 5577
adduni b y localhost 5578
adduni y b localhost 5579
adduni y $ localhost 5580
adduni $ y localhost 5581
adduni b kg localhost 5582
adduni kg b localhost 5583
adduni kg lbs localhost 5584
adduni lbs kg localhost 5585
adduni lbs g localhost 5586
adduni g lbs localhost 5587
path ft lbs

>nc localhost 5555 < input-02.txt
Welcome, you are connected to a Python-based simple command server.
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Success
Query ft m to server at localhost 5570
Query m b to server at localhost 5577
Query b kg to server at localhost 5582
Query kg lbs to server at localhost 5584
```

The Query strings are the responses are the responses to the path
command.

# Test cases discovery server
Briefly, input-00.txt verifies basic unidirectional discovery add, remove, and lookup capabilities. Including for duplicate entries. In input-01.txt we
verify that syntax errors are properly handled. And input-02.txt
verifies multi-path queriues. The input-03.txt verifies basic bidirectional
server support.

# Test cases conversion and proxy server
Starting a conversion server with the optional command-line arguments
to point to a discovery server will automatically register and
de-register the service. Usage: python convServer.py portnum [unit1
unit2] [myhost discovery-host discovery-port].
Example:
```
python convServer.py 7777 cm in localhost localhost 5555
```
This starts a cm to in conversion server and registers it with the
local discovery server.
Similarly, starting a proxy server with the optional command-line arguments
to point to a discovery server will automatically query the discovery
server for conversion path information and then query the conversion
servers along that path. Usage: python proxy_conv_server.py portnum
discovery-host discovery-port.

Example:
```
python proxy_conv_server.py 7778 localhost 5555
```

# Other notes
We discovered that the recv socket call may return less data than has
actually been sent by the remote side. So in the discovery server, we
handle this case with a recv_lines function. This has the added
benefit of supporting multi-line commands.
