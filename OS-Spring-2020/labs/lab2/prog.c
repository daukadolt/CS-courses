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
    if(argc < 1 || argc < 5 || argc > 5) {
        printf("Error: wrong number of arguments\n");
        exit(1);
    }
    char *sourceFile = argv[1];
    char *destFile = argv[2];
    int offset = atoi(argv[3]);
    int numBytes = atoi(argv[4]);

    if(offset < 0) {
        printf("Offset cannot be negative\n");
        exit(1);
    }

    int sourceFd = open(sourceFile, O_RDONLY);

    if(sourceFd == -1) {
        printf("Failed to open source file\n");
        exit(1);
    }

    int destFd = open(destFile, O_WRONLY | O_CREAT, S_IRUSR | S_IWUSR);

    if(destFd == -1) {
        printf("Failed to open or create destination file\n");
        exit(1);
    }

    int sourceSize = lseek( sourceFd, 0, SEEK_END );

    if(sourceSize < offset) {
        printf("Error: offset %d is larger than source file size %d\n", offset, sourceSize);
        exit(1);
    } else if(sourceSize < offset + numBytes) {
        printf("Error: source file doesn't have enough characters to copy\n");
        exit(1);
    }

    printf("sourceSize %d, offset %d, num %d, offset + num %d\n", sourceSize, offset, numBytes, offset+numBytes);

    int sourceOffset = lseek( sourceFd, offset, SEEK_SET );

    if(sourceOffset == -1) {
        printf("Error: unknown error while setting offset for source file\n");
        exit(1);
    }

    char *buffer = (char*) malloc(numBytes * sizeof(char));

    read( sourceFd, buffer, numBytes );

    // Now I need to know the size of destination file. If size is less than the offset, i'll have to add nulls
    // or maybe I should try to simply write first

    int destFileSize = lseek( destFd, 0, SEEK_END );

    int spacesToFill = offset - destFileSize;

    while(spacesToFill > 0) {
        write( destFd, "\0", 1);
        spacesToFill--;
    }

    printf("buffer %s", buffer);
    
    replace_at( destFd, offset, buffer, numBytes );

    return 0;
}
