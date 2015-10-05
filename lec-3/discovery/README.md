(Import google-collect-1.0.jar)

For Java DiscoveryServer,

First compile:
```
javac DiscoveryServer.java
```
Then run it with port(ex.1111):
```
java DiscoveryServer 1111
```
open another terminal and telnet to the server:
```
telnet localhost 1111
```
and run an add command:
```
add lbs oz 127.0.0.1 1234
```
Then start a new telnet:
```
telnet localhost 1111
```
Lookup for the item we just added:
```
lookup lbs oz
```
Then start a new telnet:
```
telnet localhost 1111
```
Remove the item:
```
remove 127.0.0.1 1234
```


Table Members:

    Ahsen Uppal, Luona Guo, Zhenyu Han, Zuocheng Ding, Qi Liu, Yi Zhou
