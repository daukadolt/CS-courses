#include <unistd.h>
#include <stdio.h>

int main() {
    int offset = lseek(0, 0, SEEK_CUR);
    printf("offset = %d\n", offset);
    offset = lseek(0, 0, SEEK_CUR);
    printf("offset = %d\n", offset);
    return 0;
}
