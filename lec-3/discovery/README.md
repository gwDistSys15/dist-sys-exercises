# Discovering Services
From the conversion server exercise you should have discovered that coordinating services to work together can be quite difficult, especially if they are not all under your control.

In this exercise we will design and build a `discovery` service, which allows a client (e.g., a conversion proxy server) to lookup the IP and port of a server hosting a particular service.

#### Discovery Service Goals
Your discovery service should be able to:
 1. Register the IP/port of new conversion servers
 2. Allow lookups to find IP/port for a conversion
 3. Remove servers that cleanly shutdown
 4. Balance load across replicas of a conversion
 5. Handle server crashes gracefully

**For classes on 9/21 and 9/28 we will focus only on the first three goals.**

## Table tasks
The students at your table should divide up into the following categories:
 - 2 Protocol Designers
   - Add/Remove protocol
   - Lookup protocol
 - N Programmers
   - System architect: overall system structure
   - Data management: how to store data?
   - Server connections: how should the `discovery` server accept connections?
   - Client connections / testing: how will clients (e.g., proxy servers) connect?  How will you test the system?
   - other students can help with any of the above roles

Depending on your role, you will move to one of the steps below

## Protocol Designers
This will be done by students assigned a *Protocol Designer* role.

We need to develop at least two basic protocols:
  - Add/Remove: when conversion servers or proxies start and stop, they should advertise what services they can provide to the `discovery` server.
  - Lookup: when a client (e.g., a simple telnet session or a proxy) needs to find a server running a particular conversion it should be able to find its IP and port.

#### Phase 1: Within your group (15 minutes)
Start by discussing these protocols with the other protocol designer on your team. Come up with answers to these questions:
  - What message types should the system support?
  - What will be the ordering of messages?
  - What will be the format of the requests and replies?

Look back at the [protocol specification](https://github.com/gwDistSys15/dist-sys-exercises/blob/master/lec-2/conversions/readme.md#protocol-specification) from the Conversion Server assignment to get ideas for the level of detail needed.

If you finish early, discuss your ideas with some of the programmers on your team.

#### Phase 2: Protocol Conference (30 minutes?)
All of the protocol designers from all teams will come together to discuss their protocols and reach consensus.  We will do this in two stages.

**First,** the protocol designers will split into two groups (Add/Remove and Lookup) to discuss the similarities and differences of their protocols.

**Second,** the two groups will come together and one person from each protocol group will present the protocol. At this point there may be further refinements based on feedback from the other designers.

**Third,** the two groups will split up again, and formally write out a specification of their protocol. This should be saved into a file named `add-remove.md` or `lookup.md` stored in the `lec-3/discovery/protocols/` directory.  This should be written into a fork of this repository.

Once you have finalized your protocol, you must create a Pull Request to have your protocol merged into the main repository.

#### Phase 3: Protocol Implementation
All protocol designers should return to their original tables to explain the protocols we have decided on and help implement them into the code.


## Architecture Developers
This will be done by students assigned a *Programmer* role.

**Step 0:** As a first step, one of the students in your group should create a `fork` of this git repository ([read this if you need help]([https://help.github.com/articles/fork-a-repo/])).  You should then each clone the fork you have created and you should commit all of your changes to your fork as you work on the project.

#### Phase 1: Architecture (15 minutes)
Spend the first fifteen minutes discussing with the other programmers what language you will use and what your overall architecture is going to be.  Divide the design and implementation tasks up among the students in your group.

If you finish early, you can continue to the next phase.

#### Phase 2: Prototypes (30 minutes?)
The protocol designers on your team will be discussing the exact protocol your system will use, but you should be able to begin developing the code framework that will run your system.

Think about how you can build a set of prototypes that will help with this project.  Try to build each of these simple servers. Store them into different files and later you will combine them to build your overall system.
  1. `simple-serve` A server that calls different functions depending on whether a client sends a string where the first word is "set" or "get". Try to make this very flexible so it is easy to add different message types later (This is the most basic server prototype, and you should be able to reuse your old code)
  2. `print-serve` A server that simply displays any messages that are sent to it.  (This will be useful for debugging/testing later on)
  3. `store-serve` A server that can store a single value. For example a client sending `set value1` would cause the server to save the string `value1` in memory.  Later, if a client sends the message `get`, the server should return `value1`. (This will help you figure out how to build a "stateful" server, i.e., one that stores data and can return it later)
  4. Finally, you should modify your conversion server and proxy so that when they start they will send a message to the Print server described above. (This will mimic the behavior your servers will need later to contact the `discovery` server to announce what conversions they support)

Keep working on these types of servers and any other examples you can think of that could be useful until your protocol designers return.

#### Phase 3: Protocol Implementation
Once the protocol designers return to their original tables they will explain the protocols they have decided on and help implement them into the code.
