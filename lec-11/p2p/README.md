# Visualizing P2P Networks
This exercise will help you understand how different P2P network architectures by visualizing their structure.  We will use a javascript library to draw the visualizations after defining networks with different structures.

You should either download these files to your own computer and view them locally, or put them in the ``Web`` directory of your Koding.com VM so that you will be able to view the web pages produced by the system.

## Ring network
The sample files I've provided show how to define and draw a ring network.  The ``graph.html`` file has all the code needed to draw a set of nodes and the edges between them.  It gets the data that defines the network topology by reading the ``ring.js`` file.  You won't need to modify the ``graph.html`` file, or even understand how it works.  Try opening the file in your web browser to see the graph.  Note that the script tries to automatically place the nodes so they are evenly spaced; you may have to refresh the screen to get a reasonable placement. It's also fun to watch them fly around.

By looking at the ``ring.js`` script it should be pretty obvious how the graph is defined: you simply need to create an ``adjacencyList`` variable that specifies for each vertex what other vertices it is connected to.  In this example, node 1 connects to node 2, and so on.  Nodes 10, 11, and 12 are a separate connected group.  Try changing one of the edges or add a new one and then reload the web page to see the graph change.

## Gnutella v1
In the original Gnutella P2P file sharing service, every new node randomly picked 5 neighbors to use as peers.  The first part of this exercise is to write a program which will create an adjacencyList that mimics this kind of network.  Note that most nodes will end up having more than 5 edges total since they will have five outgoing links but may also have some incoming links as well.

You can write your program in any language (it does not have to be javascript!), and it should create a file called ``gnutella.js`` which will have the same format as ``ring.js``.  Your program should take an argument specifying how many total nodes to create in the network (default 100), and how many neighbors each should pick (default 5).  Then create a ``gnutella.html`` file that is a copy of ``graph.html``, but that changes the data input file on line 7.

## Gnutella v2
Next you should write a program that creates a network topology similar to the updated Ultra Peer / Leaf Node architecture later adopted by Gnutella.  In this kind of system, each Leaf node connects to 3 Ultra Peers, and each Ultra Peer connects to 32 other Ultra Peers.

Write a new program that creates an adjacencyList that meets this criteria. Your program should take inputs to indicate the number of leaf nodes (default 1000), number of Ultra Peers (default 256), number of connections for each Leaf Node (default 3), and number of connections for each Ultra Peer (default 32).

## Chord
In Chord, nodes are structured in a ring with links to two adjacent nodes, plus a finger table with additional links to speed up lookups.  Thus node ``i`` will have ``m`` total links to nodes ``i+2^0, i+2^1, i+2^2, i+2^3, ... i+2^m``.

Write a program that will create this topology.  Your program should have parameters that specify the total number of nodes (default 64) and the number of entries in the finger table (default 6).

## Make it interactive
If you have time (and sufficient Javascript expertise), take the graph display a step further so that selecting a node will highlight it and all of its edges.  [This example might help](http://jsfiddle.net/tristanreid/xReHA/636/).
