// CS 6421 - Simple Message Board Client in C
// T. Wood
// Compile with: gcc msg_client -o msg_client
// Run with:     ./msg_client

#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <inttypes.h>
#include <string.h>

int main(int argc, char ** argv)
{
        char* server_port = "5555";
        char* server_ip = "twood02.koding.io";

        // your code here!

        printf("Done.\n");
        return 0;
}
