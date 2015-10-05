This is the HW 4 submission for Ahsen Uppal and Luona Guo, Zhenyu Han, Zuocheng Ding, Qi Liu, Yi Zhou.

client can talk to ProxyServer to get the result of convertion. 

# Add and Remove Protocol

    add command: 
    
        add <unit_1> <unit_2> <IP> <port>\n
        return SUCCESS or Error:[info]

    whereas remove command is:

        remove <IP> <port>\n
        return SUCCESS or Error:[info]
        
# Lookup Protocol

    Lookup <unit_src> <unit_dest>

        lookup UNIT1 UNIT2\n
        return IP PORT or Error:[info]
        
test plan
    python discover-server.py 5555
    
    python convServer_b_in.py 5556
    
    python convServer_b_lbs.py 5557
    
    javac ConvServer_b_g.java 
    java ConvServer_b_g 5558
    
    javac ProxyServer.java
    java ProxyServer 5559
    
    telnet localhost 5559
    lbs in 5

