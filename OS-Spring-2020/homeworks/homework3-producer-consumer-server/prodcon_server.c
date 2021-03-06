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
#include <pthread.h>
#include <semaphore.h>

/*
**	This poor server ... only serves one client at a time
*/

pthread_mutex_t getThreadNum;
int threadNum = 0;


void writeToSocket(int socketFD, void *buffer, int length) {
	if(write(socketFD, buffer, length) <= 0) {
		close(socketFD);
		exit(-1);
	}
}

void serveProducer(void *socketFDNotPointer) {
	char			buf[BUFSIZE];
	int				cc;
	int				ssock = (int) socketFDNotPointer;
	writeToSocket(ssock, "GO\r\n", 4);
	int charNum;
	if ( (cc = read( ssock, &charNum, 4 )) <= 0 )
	{
		printf( "The client has gone.\n" );
		close(ssock);
	}
	charNum = ntohl(charNum);
	printf("number of chars - %d\n", charNum);

	ITEM *newItem = (ITEM *) malloc(sizeof(ITEM));
	newItem->size = charNum;
	newItem->letters = (char *) malloc(sizeof(char)*charNum);

	if ( (cc = read( ssock, newItem->letters, newItem->size )) <= 0 )
	{
		printf( "an error when reading letters.\n" );
		close(ssock);
		exit(-1);
	}

	printf("read string - %s\n", newItem->letters);
	

	writeToSocket(ssock, "DONE\r\n", 6);
	if ( (cc = read( ssock, buf, BUFSIZE )) <= 0 )
	{
		printf( "The client has gone.\n" );
		close(ssock);
	}
}

void serveConsumer(void *socketFDNotPointer) {

}

void serveRequest(void *socketFDNotPointer) {
	char		buf[100];
	int 		ssock = (int) socketFDNotPointer;
	int 		cc;
	if ( (cc = read( ssock, buf, BUFSIZE )) <= 0 )
			{
				printf( "The client has gone.\n" );
				close(ssock);
			}
			else
			{
				buf[cc] = '\0';
				printf( "The client says: %s\n", buf );
				if(strcmp(buf, "PRODUCE\r\n") == 0) {
					serveProducer(socketFDNotPointer);
				} else if(strcmp(buf, "CONSUME\r\n") == 0) {
					serveConsumer(socketFDNotPointer);
				} else {
					printf("closing connection\n");
					close(ssock);
				}
			}
}

int
main( int argc, char *argv[] )
{
	char			*service;
	struct sockaddr_in	fsin;
	socklen_t		alen;
	int			msock;
	int			ssock;
	int			rport = 0;
	int			cc;
	pthread_mutex_init(&getThreadNum, NULL);
	
	switch (argc) 
	{
		case	1:
			// No args? let the OS choose a port and tell the user
			rport = 1;
			break;
		case	2:
			// User provides a port? then use it
			service = argv[1];
			break;
		default:
			fprintf( stderr, "usage: server [port]\n" );
			exit(-1);
	}

	msock = passivesock( service, "tcp", QLEN, &rport );
	if (rport)
	{
		//	Tell the user the selected port
		printf( "server: port %d\n", rport );	
		fflush( stdout );
	}

	
	for (;;)
	{
		int	ssock;

		alen = sizeof(fsin);
		ssock = accept( msock, (struct sockaddr *)&fsin, &alen );
		if (ssock < 0)
		{
			fprintf( stderr, "accept: %s\n", strerror(errno) );
			exit(-1);
		}

		printf( "A client has arrived for echoes.\n" );
		fflush( stdout );

		/* start working for this guy */
		/* ECHO what the client says */
		
		pthread_mutex_lock(&getThreadNum);
		printf("threadNum is %d\n", threadNum);
		if(threadNum > MAX_CLIENTS) exit(-1);
		else if(threadNum == MAX_CLIENTS) close(ssock);
		else threadNum++;
		pthread_mutex_unlock(&getThreadNum);


		pthread_t thr;
		pthread_create(&thr, NULL, serveRequest, (void *) ssock);
	}
}


