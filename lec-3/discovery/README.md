For Java DiscoveryServer,

First compile:
```
javac DiscoveryServer.java
```
Then run it with port(ex.11111):
```
java DiscoveryServer 11111
```
open another terminal and telnet to the server:
```
telnet localhost 11111
```
and run an add command:
```
add lbs oz 127.0.0.1 12345
```
Then start a new telnet:
```
telnet localhost 11111
```
Lookup for the item we just added:
```
lookup lbs oz
```
Then start a new telnet:
```
telnet localhost 11111
```
Remove the item:
```
remove 127.0.0.1 12345
```


Table Members:

    Ahsen Uppal, Luona Guo, Zhenyu Han, Zuocheng Ding, Qi Liu, Yi Zhou
