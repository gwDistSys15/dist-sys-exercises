
/**************************************************************************
 *  Single threaded convertion server templete 
 *  It sends out a welcome message when receive connection from client.
 *  please use gcc -o conv_server conv_server.c to compile 
 *  please use sudo ./conv_server -p portnum to run
 **************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <unistd.h>
#include <inttypes.h>
#include <fcntl.h>
#include <netdb.h>
#include <netinet/in.h>
#include <errno.h>
#include <getopt.h>
#include <string.h>
#include <sys/socket.h>


static int mode_flag = 0; //0:server mode; 1: client mode 

static int buffer_size = 1024; 
static char* port;
static char* msg = "welcome, connected\n";

static char* server_ip;
static char* value = "value\n";


/*
 * Print a usage message
 */
static void
usage(const char* progname) {
    printf("Usage: %s ", progname);
    printf("\n");
    printf("  -m : 's' for run as server; 'c' for as client; default as server\n");
    printf("  -p : your port number\n");
    //--TODO: add arguments explaination here 
    printf("  -h : your server ip address\n");
    printf("  -v : your value here\n");
    printf("\n\n");
}


/*
 * Parse the application arguments.
 */
static int
parse_app_args(int argc, char* argv[]) {
    const char* progname = argv[0];
    const char* flag_server = "s";
    const char* flag_client = "c";
    int rc;
   

    opterr = 0;
    
    while ((rc = getopt (argc, argv, "m:p:h:v:")) != -1)
    switch (rc) {
        case 'm':
        if (optarg == flag_server) {
            mode_flag = 0;
        }
        if (optarg == flag_client) {
            mode_flag = 1;
        }
        break;
        
	case 'p':
        port = optarg;
        break;
        
	//--TODO: add arguments handling here   

	case 'h':
        server_ip = optarg;
        break;

	case 'v':
        value = optarg;
        break;
        
	default:
        usage(progname);
	exit(1);
    }
    return optind;
}



/*
 *If connection is established then send out welcome message
 */

//--TODO: add your converting functions here 

void 
processing(int sock)
{
	int n;
	buffer_size = sizeof(value);
	bzero(value, buffer_size);
    
    n = read(sock, value, buffer_size);
    
    if (n < 0){
        perror("ERROR reading from socket");
        exit(1);
    }

    printf("Here is the message: %s\n", value);
    
    /* Write a response to the client */
    n = write(sock, msg, strlen(msg));
    
    if (n < 0){
        perror("ERROR writing to socket");
        exit(1);
    }
}



/*
 * Server
 */



int 
server( int argc, char **argv )
{
    int optval = 1;
    int sockfd, newsockfd;
    socklen_t clilen;
    char buffer[256];
    struct sockaddr_in serv_addr, cli_addr;
    int  n;

    /* First call to socket() function */
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof optval);
    
    if (sockfd < 0){
        perror("ERROR opening socket");
        exit(1);
    }
    
    /* Initialize socket structure */
    bzero((char *) &serv_addr, sizeof(serv_addr));
    
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(atoi(port));
    
    /* Now bind the host address using bind() call.*/
    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0){
        perror("ERROR on binding");
        exit(1);
    }
    
    /*
     *Listening for the client
     */
    
    listen(sockfd,5);
    clilen = sizeof(cli_addr);
    
    /* Accept connection from the client */
    newsockfd = accept(sockfd, (struct sockaddr *)&cli_addr, &clilen);
    if (newsockfd < 0){
        perror("ERROR on accept");
        exit(1);
    }
    
    /*
     *Processing and send out welcome message
     */
    processing(newsockfd);
    
    /*clean up*/
    close(sockfd);
    close(newsockfd);
    
    return 0;
}


/*
 * Client
 */

int
client(int argc, char ** argv)
{
    int rc;
    struct addrinfo hints, *server;
    
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    
    if ((rc = getaddrinfo(server_ip, port, &hints, &server)) != 0) {
        perror(gai_strerror(rc));
        exit(-1);
    }
    
    
    int sockfd = socket(server->ai_family, server->ai_socktype, server->ai_protocol);
    if (sockfd == -1) {
        perror("ERROR opening socket");
        exit(-1);
    }
    
    rc = connect(sockfd, server->ai_addr, server->ai_addrlen);
    if (rc == -1) {
        perror("ERROR on connect");
        close(sockfd);
        exit(-1);
    }
    
    rc = send(sockfd, value, strlen(value)+1, 0);
    if(rc < 0) {
        perror("ERROR on send message");
        exit(-1);
    }
    
    
    freeaddrinfo(server);
    close(sockfd);
    
    printf("Done.\n");
    
    return 0;
    
}

int main(int argc, char ** argv){
    
    const char* progname = argv[0];
    
    //--TODO: add arguments exception handling here
    if (argc < 3){
        usage(progname);
        printf("Not enough command-line arguments\n");
        exit(1);
    }
    
    if (parse_app_args(argc, argv) < 0){
        usage(progname);
        printf("Invalid command-line arguments\n");
        exit(1);
    }
    
    if (mode_flag == 0){
        if (server(argc, argv) == 1){
		printf("server in trouble\n");
		exit(1);	
        }
    }
    
    if (mode_flag == 1){
        if (client(argc, argv) == 1){
		printf("client in trouble\n");
		exit(1);	
        }
    }
    
    exit(1);

}
