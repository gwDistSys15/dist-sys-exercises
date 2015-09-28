# Add and Remove Protocol

As we disscussed in the other room,
add command should look like this: 
```
add <unit_src> <unit_des> <IP> <port>\n
```
whereas remove command looks like this:
```
remove <IP> <port>\n
```

Add:

    convertion server -> discovery server: "conv:a:unit_1 unit_2:host port"
    discovery server -> proxy server: "discov:updt_t_a:unit_1 unit_2"
    
    proxy server -> discovery server: "proxy:a:host port"
    
Remove:

    convertion server -> discovery server: "conv:r:unit_1 unit_2"
    discovery server -> proxy server: "discov:updt_t_r:unit_1 unit_2"
    
    proxy server -> discovery server: "proxy:r"

