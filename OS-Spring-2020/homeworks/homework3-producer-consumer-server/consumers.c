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

/*
**	Client
*/
int
main( int argc, char *argv[] )
{
	char		buf[] = {'C', 'O', 'N', 'S', 'U', 'M', 'E', '\r', '\n', '\0'};
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

	// 	Start the loop
	
	if ( write( csock, buf, strlen(buf) ) < 0 )
	{
		fprintf( stderr, "client write: %s\n", strerror(errno) );
		exit( -1 );
	}
	close( csock );

}


