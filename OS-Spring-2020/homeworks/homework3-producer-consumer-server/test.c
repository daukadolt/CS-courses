#include <stdio.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

int main() {
    unsigned int i = 70;
    unsigned int j = 70;
    int destFD = open("test.txt", O_RDWR | O_TRUNC | O_CREAT, S_IRUSR | S_IWUSR);
    printf("destfd %d\n", destFD);
    write(destFD, &j, 4);
    write(2, &j, 4);
    printf("\n%d\n", *(&j));
    return 0;
}