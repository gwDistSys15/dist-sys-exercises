# Lookup Protocol

For request:
```
Lookup <unit_src> <unit_dest>
```
should receive response in this format:
if successful:
```
IP_ADDRESS PORT\n
```
if failed:
```
"null"\n
```
Lookup:

    proxy server -> discovery server: "proxy:lkup:unit_1 unit_2"
    discovery server -> proxy server: "discov:r_lkup:host port"
    
