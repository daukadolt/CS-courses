#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <prodcon.h>
#include <time.h>

/*
**	Client
*/

static char *rand_string(char *str, size_t size)
{
    const char charset[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJK...";
    if (size) {
        --size;
        for (size_t n = 0; n < size; n++) {
            int key = rand() % (int) (sizeof charset - 1);
            str[n] = charset[key];
        }
        str[size] = '\0';
    }
    return str;
}

ITEM *generateItem() {
	ITEM *newItem = (ITEM *)malloc(sizeof(ITEM));
	newItem->size = rand()%100;
	newItem->letters = (char *)malloc(sizeof(char)*newItem->size);
	rand_string(newItem->letters, newItem->size);
	return newItem;
}

void writeToSocket(int socketFD, void *buffer, int length) {
	if(write(socketFD, buffer, length) <= 0) {
		close(socketFD);
		exit(-1);
	}
}

int
main( int argc, char *argv[] )
{
	char		initMessage[] = {'P', 'R', 'O', 'D', 'U', 'C', 'E', '\r', '\n', '\0'};
	char		buf[BUFSIZE];
	char		*service;		
	char		*host = "localhost";
	int		cc;
	int		csock;
	
	switch( argc ) 
	{
		case    2:
			service = argv[1];
			break;
		case    3:
			host = argv[1];
			service = argv[2];
			break;
		default:
			fprintf( stderr, "usage: chat [host] port\n" );
			exit(-1);
	}

	/*	Create the socket to the controller  */
	if ( ( csock = connectsock( host, service, "tcp" )) == 0 )
	{
		fprintf( stderr, "Cannot connect to server.\n" );
		exit( -1 );
	}

	/* Set the seed for random */
	srand(time(NULL));

	ITEM *item = generateItem();

	// 	Start the loop
	if ( write( csock, initMessage, strlen(initMessage) ) < 0 )
	{
		fprintf( stderr, "client write: %s\n", strerror(errno) );
		exit( -1 );
	}
	
	
	if( (cc = read(csock, buf, BUFSIZE)) <= 0) {
		printf("server has gone\n");
	}
	/* now we've read response to our PRODUCE */

	buf[cc] = '\0';
	if(strcmp(buf, "GO\r\n") == 0) {
		printf("GO received\n");
		int someNum = 100;
		int reordered = htonl(someNum);
		writeToSocket(csock, &reordered, 4);
		printf("sizeof someNum %d\n", reordered);
	}
	else {
		printf("not go\n");
		close(csock);
		exit(-1);
	}

	close( csock );

}


