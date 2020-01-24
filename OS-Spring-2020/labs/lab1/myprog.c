#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

int main(int argc, char **argv) {
    if(argc < 1 || argc < 4 || argc > 4) {
        printf("Error: wrong number of arguments\n");
        exit(1);
    }
    char *filename = argv[1];
    off_t offset = atoi(argv[2]);
    char *string = argv[3];
    int fd = open(filename, O_RDONLY);
    if(fd == -1) {
        printf("Error: file could not be found\n");
        exit(1);
    }
    printf("File opened successfully. File Descriptor = %d\n", fd);
    offset = lseek(fd, offset, SEEK_SET);
    printf("offset = %lld\n", offset);
    return 0;
}
