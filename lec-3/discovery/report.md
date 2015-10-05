# Group Members

- Engin Kayraklioglu(e-kayrakli) : engin [at] gwu (programmer)
- Malcolm Goldiner - mgoldiner@gwu.edu (programmer) 
- Tim Stamler - tstamler@gwu.edu (programmer)
- Grace Liu - 	guyue@gwu.edu (protocol designer) 
- Shuo Zhang - zhangshuo@gwu.edu (programmer) 

# Brief Summary

Our discovery server has a number of python conversion servers that take advantage of the discovery server.
At startup, each server registers its conversion with the discovery server, which is stored in a hash table. 
After this, any client or server can get this conversion and server information from the discovery server.
Each server can be quit and then will remove itself from the discovery server. 

#### Protocol Specification

The discovery server accepts three types of commands:
    - add <unit1> <unit2> <address> <port> 
        *This will add a new conversion and server to the hash table
    - lookup <unit1> <unit2>
        *This will lookup a conversion in the table and send back the response
    - remove <unit1> <unit2>
        *This will remove a conversion from the hash table, if a server goes down
## Test Plan
    -First the discovery server must be compiled and started
        *javac DiscoveryServer.java
        *java DiscoveryServer <port>
    -Then any of the conversion servers can be started
        *python <servername.python> <host address> <host port> <discovery server address> <discovery server port>
    -Any client like telnet can be used to give commands to the python or discovery server
