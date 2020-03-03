#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>

#define THREADS	1000
#define BUFSIZE 20


typedef struct item_t
{
	char *product;
	int size;
} ITEM;

ITEM *makeItem()
{
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
	printf( "%s\n", p->product );
	free( p->product );
	free( p );
}

int count;
ITEM *buffer[BUFSIZE];

pthread_mutex_t mutex;
sem_t full, empty;

void *produce( void *ign )
{
	// Produce an item
	ITEM *p = makeItem();

	// Wait for room in the buffer
	// while ( count > BUFSIZE );
	sem_wait( &empty );

	pthread_mutex_lock( &mutex );
	// Put the item in the next slot in the buffer
	buffer[count] = p;
	count++;
	printf( "C Count %d.\n", count );
	pthread_mutex_unlock( &mutex );

	sem_post( &full );

	// Exit
	pthread_exit( NULL );
}

void *consume( void *ign )
{
	// Wait for items in the buffer
	// while ( count <= 0 );
	sem_wait( &full );

	pthread_mutex_lock( &mutex );
	// Remove the item and update the buffer
	ITEM *p = buffer[count-1];
	buffer[count-1] = NULL;
	count--;
	printf( "C Count %d.\n", count );
	pthread_mutex_unlock( &mutex );

	sem_post( &empty );

	// Now use it
	useItem( p );

	// Exit
	pthread_exit( NULL );
}

int main( int argc, char **argv )
{
	pthread_t threads[THREADS*2];
	int status, i, j;

	pthread_mutex_init( &mutex, NULL );
	sem_init( &full, 0, 0 );
	sem_init( &empty, 0, BUFSIZE );

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
