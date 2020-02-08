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
#include <fcntl.h>

#define CMDLEN 254
#define ARGNUM 100

int main()
{
	int pid;
	int status;
	int i, fd;
	char command[CMDLEN];
    char *arguments[ARGNUM];
    int runAsDaemon = 0;

	for (;;)
	{
        runAsDaemon = 0;
		printf( "daush$: " );
		fgets( command, CMDLEN, stdin );
		command[strlen(command)-1] = '\0';
        if(command[strlen(command)-1] == '&') {
            runAsDaemon = 1;
            command[strlen(command)-1] = '\0';
        }
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
			if(!runAsDaemon) waitpid( pid, &status, 0 );
		}
		else
		{
			//printf( "CHILD. pid = %d, mypid = %d.\n", pid, getpid() );

            // i is +1 than number of arguments, which include null as well. Hence to check "next-to-last"
            
            char* token = strtok(command, " ");
            int i = 0;
            arguments[0] = command;
            for(i = 1; token != NULL; i++) {
                token = strtok(NULL, " ");
                arguments[i] = token;
                //printf("argument %s\n", arguments[i]);
            }

            i--;

            if(i>1 && (strcmp(arguments[i-2], ">") == 0 || strcmp(arguments[i-2], ">>") == 0 )) {
                int mode = strcmp(arguments[i-2], ">") == 0 ? O_TRUNC : O_APPEND;
                int destFd = open(arguments[i-1], O_RDWR | O_CREAT | mode, S_IRUSR | S_IWUSR);
                if(destFd == -1) exit(-1);
                int dupStatus = dup2(destFd, 1);
                if(dupStatus == -1) exit(-1);
                arguments[i-2] = NULL;
                execvp(command, arguments);
            } else execvp(command, arguments);

            printf("No such program: %s\n", command);
            exit(-1);
		}
	}
}
