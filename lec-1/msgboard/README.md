# Lec 1: The Simplest Distributed System


In this exercise you will build the simplest type of distributed system---a client/server application.

#### A very simple Message Board
Our program will do the following:

The **server** acts as message board; it will print out any messages that are sent to it along with the name of the sender.

The **client** connects to the server and sends its name and the message it wants printed.  After that it disconnects. The server does not send anything back to the client: I told you this was very simple!

## Getting the code
*Everyone in your group should do this!*

Before you can begin you need to get a copy of the sample code.
  * Start your VM on Koding.com
  * in the terminal, run `git clone https://github.com/gwDistSys15/dist-sys-exercises.git`
  * run `cd dist-sys-exercises/lec-1/msgboard` to change to the directory containing all of the sample code for this exercise.

## Find your VM's hostname and IP
*Everyone in your group should do this!*

In order to connect to your VM from another computer you need to know its IP or hostname.  

**First,** open the *VM Settings* window by clicking the "..." icon next to your VM name on the left panel as shown in this screenshot:
![Koding VM Setup](_res/koding-setup1.png)

On the first page of the popup window will be a table with your *Public IP* and *Assigned URL*.  Make a note of your IP.  

**Second**, click on the *Domains* tab of the settings window and click the slider to enable the domain "yourusername.koding.io".  This will allow you to easily access your VM with the host name `yourusername.koding.io`

## Starting the Server
*Select one of the students in your group to run the server. Only she or he needs to follow these directions*

The provided server is written in Java so you will need to compile and then run the program as follows.
```
cd server              # change to the server/ directory
javac MsgServer.java   # compile the code
java MsgServer         # start the server
```

## Client 0: `telnet`
The first client you will use to connect to the server won't require any coding at all--you will use `telnet`.  Telnet is a very simple program that is useful for debugging network applications and protocols.  It simply sends and receives lines of data to and from a server.  To run telnet, run: `telnet HOST port`

This will open a TCP connection to the specified `HOST` (either an IP or hostname) and `PORT`.  If you type some text and hit enter, `telnet` will transmit that line of text to the server.  `telnet` is also always listening for messages coming from the server, and will print them out one line at a time.

Try sending a few messages to verify that your server is working.

**Once you know the server works, you can then try the different languages below in any order.**

## Client 1: Sockets in C

## Client 2: Sockets in Java

## Client 3: Sockets in Python
