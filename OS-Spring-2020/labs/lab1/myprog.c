#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>


int replace_at( int fd, off_t offset, char *s, int n ) {
    if(offset < 0) {
        printf("Error: offset cannot be negative\n");
        return -1;
    }
    off_t fileSize = lseek( fd, 0, SEEK_END );
    if(offset > fileSize) {
        printf("Error: offset went past the filesize. Maximum offset = %lld\n", fileSize);
        return -1;
    }
    off_t newOffset = lseek( fd, offset, SEEK_SET );
    if(newOffset == -1) {
        printf("Error: unknown error when trying to lseek\n");
        return -1;
    }
    ssize_t bytesWritten = write( fd, s, n );
    if(bytesWritten == -1) {
        printf("Error: could not perform write to the file\n");
        return -1;
    }
    return (int) lseek( fd, 0, SEEK_CUR );
}


int main(int argc, char **argv) {
    if(argc < 1 || argc < 4 || argc > 4) {
        printf("Error: wrong number of arguments\n");
        exit(1);
    }
    char *filename = argv[1];
    off_t offset = atoi(argv[2]);
    char *string = argv[3];
    int fd = open(filename, O_WRONLY);
    if(fd == -1) {
        printf("Error: file could not be found\n");
        exit(1);
    }
    printf("File opened successfully. File Descriptor = %d\n", fd);
    int newOffset = replace_at(fd, offset, string, strlen(string));
    return 0;
}
