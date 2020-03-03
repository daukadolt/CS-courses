#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

#define THREADS	2
#define BUFSIZE 20

/*
** This simple producer-consumer just has producers writing X's in an array
** and consumers changing the X's back to NULL.
** With no protection of the critical section, the race condition problems
** become obvious quickly, and manifest in the following ways:
** 1> Items are left over even though the number of producers and consumers is equal
** 2> The program hangs forever
** 3> The program crashes with a segmentation fault
*/

typedef struct item_t
{
	char *product;
	int size;
} ITEM;

ITEM *makeItem()
{
	printf( "MAKE\n" );
	fflush(stdout);
	int i;
	ITEM *p = malloc( sizeof(ITEM) );
	p->size = random()%80;
	p->product = malloc(p->size);	
	for ( i = 0; i < p->size-1; i++ )
		p->product[i] = 'X';
	p->product[i] = '\0';
	return p;
}

void useItem( ITEM *p )
{
	printf( "USE\n" );
	fflush(stdout);
	printf( "%s \n", p->product );
	fflush(stdout);
	free( p->product );
	free( p );
}

int count;
ITEM *buffer[BUFSIZE];

void *produce( void *ign )
{
	// Produce the item
	ITEM *p = makeItem();

	// Wait for room in the buffer
	while ( count > BUFSIZE );

	// Put it in the next slot in the buffer
	buffer[count] = p;
	count++;

	printf( "PCount is %d\n", count );
	fflush(stdout);

	pthread_exit( NULL );
}

void *consume( void *ign )
{
	// Wait for items in the buffer
	while ( count <= 0 );

	// Clear its space in the buffer
	ITEM *p = buffer[count-1];
	buffer[count-1] = NULL;
	count--;

	printf( "CCount is %d\n", count );
	fflush(stdout);

	useItem( p );

	pthread_exit( NULL );
}

int main( int argc, char **argv )
{
	pthread_t threads[THREADS*2];
	int status, i, j;

	count = 0;
	for ( j = 0, i = 0; i < THREADS; i++ )
	{
		status = pthread_create( &threads[j++], NULL, produce, NULL );
		if ( status != 0 )
		{
			printf( "pthread_create error %d.\n", status );
			exit( -1 );
		}
		status = pthread_create( &threads[j++], NULL, consume, NULL );
		if ( status != 0 )
		{
			printf( "pthread_create returned error %d.\n", 
				status );
			exit( -1 );
		}
	}
	for ( j = 0; j < THREADS*2; j++ )
		pthread_join( threads[j], NULL );
	printf( "Finally, the count is %d.\n", count );
	pthread_exit( 0 );
}
