/*
**	This program is a very simple shell that only handles 
**	single word commands (no arguments).
**	Type "quit" to quit.
*/
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>

#define CMDLEN 256
#define ARGLEN 256

int main()
{
	int pid;
	int status;
	int i, fd;
	char command[CMDLEN];
    char *arguments[ARGLEN];
	printf( "Program begins.\n" );
	
	for (;;)
	{
		printf( "daush$:   " );
		fgets( command, CMDLEN, stdin );
		command[strlen(command)-1] = '\0';
		if ( strcmp(command, "quit") == 0 )
			break;

		pid = fork();
		if ( pid < 0 )
		{
			printf( "Error in fork.\n" );
			exit(-1);
		}
		if ( pid != 0 )
		{
			//printf( "PARENT. pid = %d, mypid = %d.\n", pid, getpid() );
			waitpid( pid, &status, 0 );
		}
		else
		{
			//printf( "CHILD. pid = %d, mypid = %d.\n", pid, getpid() );
            char* token = strtok(command, " ");
            int i;
            arguments[0] = command;
            for(i = 1; token != NULL; i++) {
                if(token == NULL) break;
                token = strtok(NULL, " ");
                arguments[i] = token;
                //printf("%d, %s\n", i, token);
            }
			execvp( command, arguments );
		}
	}
}
