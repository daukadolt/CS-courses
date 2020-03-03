#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <semaphore.h>

#define THREADS	100
#define BUFSIZE 20
#define ITEM "Some Item Name"

typedef struct item
{
	char name[32];
	int size;
} item_t;

int count;
item_t *buffer[BUFSIZE];

pthread_mutex_t mutex;

void *produce( void *ign )
{
	item_t *item;

	// produce an item
	item = (item_t *) malloc( sizeof(item_t) );
	//item->name = (char *) malloc( strlen(ITEM)+1 );
	strcpy( item->name, ITEM );
	item->size = strlen(ITEM);
	
	// wait for there to be a slot to put it in
	while ( count > BUFSIZE );

	pthread_mutex_lock( &mutex );

	printf( "P count is %d\n", count );
	fflush( stdout );

	buffer[count] = item;
	count++;

	pthread_mutex_unlock( &mutex );

	pthread_exit( NULL );
}

void *consume( void *ign )
{
	item_t *item;

	// Wait for there to be an item to consume
	while ( count <= 0 );

	pthread_mutex_lock( &mutex );

	printf( "C count is %d\n", count );
	fflush( stdout );

	item = buffer[count-1];
	buffer[count-1] = NULL;
	count--;

	pthread_mutex_unlock( &mutex );

	// dispose of the item
	//free( item->name );
	free( item );

	pthread_exit( NULL );
}

int main( int argc, char **argv )
{
	pthread_t threads[THREADS*2];
	int status, i, j;

	pthread_mutex_init( &mutex, NULL );

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
