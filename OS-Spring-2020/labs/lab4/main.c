#include <stdio.h>
#include <sys/time.h>
#include <pthread.h>
#include <stdlib.h>

int m1[2][3] = {
    {1,2,3},
    {4,5,6}
};
int m2[3][2] = {
    {1,2},
    {3,4},
    {5,6}
};

struct args {
    int m1Row, m1Col, m2Row, m2Col;
    int** m1;
    int** m2;
    int** result;
};

struct threadArgs {
    struct args *parameters;
    int rowNum;
};

void printMatrix(int row, int col, int matrix[row][col]) {
    for(int i = 0; i<row; i++) {
        printf("[ ");
        for(int j = 0; j < col; j++) {
            printf("%d ", matrix[i][j]);
        }
        printf("]\n");
    }
}

void sequential(int m1Row, int m1Col, int m1[m1Row][m1Col], int m2Row, int m2Col, int m2[m2Row][m2Col]) {
    struct timeval start, end;
    int result[m1Row][m2Col];
    gettimeofday(&start, NULL);
    for(int i = 0; i<m1Row; i++) {
        for(int j = 0; j<m2Col; j++) {
            int currentCellSum = 0;
            for(int x = 0; x < m1Col; x++) {
                for(int y = 0; y < m2Row; y++) {
                    if(x != y) continue;
                    currentCellSum += m1[i][x]*m2[y][j];
                }
            }
            result[i][j] = currentCellSum;
        }
    }
    gettimeofday(&end, NULL);
    /*
    printf("started at %ld seconds, %d microseconds\n", start.tv_sec, start.tv_usec);
    printf("finished at %ld seconds, %d microseconds\n", end.tv_sec, end.tv_usec);
    */
    printMatrix(m1Row, m2Col, result);
    printf("Took %ld seconds to multiply [%dx%d] on [%dx%d] sequentially\n", end.tv_sec-start.tv_sec, m1Row, m1Col, m2Row, m2Col);
}

void concurrent(struct threadArgs *arguments){
    int currentRow = arguments->rowNum;
    for(int j = 0; j<arguments->parameters->m2Col; j++) {
        int currentCellSum = 0;
        for(int x = 0; x<arguments->parameters->m1Col; x++) {
            for(int y = 0; y<arguments->parameters->m2Row; y++) {
                if(x!=y) continue;
                currentCellSum += arguments->parameters->m1[currentRow][x] * arguments->parameters->m2[y][j];
            }
        }
        arguments->parameters->result[currentRow][j] = currentCellSum;
        //printf("currentCellSum %d\n", currentCellSum);
    }
};

void startConcurrent(int m1Row, int m1Col, int m1[m1Row][m1Col], int m2Row, int m2Col, int m2[m2Row][m2Col]) {
    struct args *parameters = (struct args *) malloc(sizeof(struct args));
    parameters->m1 = malloc(m1Row*sizeof(int*));
    parameters->m1Row = m1Row;
    parameters->m1Col = m1Col;
    for(int i = 0; i<m1Row; i++) {
        parameters->m1[i] = malloc(m1Col*sizeof(int));
        for(int j = 0; j<m1Col; j++) {
            parameters->m1[i][j] = m1[i][j];
        }
    }
    /*
    for(int i = 0; i<m1Row; i++) {
        for(int j = 0; j<m1Col; j++) {
            printf("%d\n", parameters->m1[i][j]);
        }
    }
    */
    parameters->m2 = malloc(m2Row*sizeof(int*));
    parameters->m2Row = m2Row;
    parameters->m2Col = m2Col;

    for(int i = 0; i<m2Row; i++) {
        parameters->m2[i] = malloc(m2Col*sizeof(int));
        for(int j = 0; j<m2Col; j++) {
            parameters->m2[i][j] = m2[i][j];
        }
    }
    /*
    for(int i = 0; i<m2Row; i++) {
        for(int j = 0; j<m2Col; j++) {
            printf("%d\n", parameters->m2[i][j]);
        }
    }
    */

    parameters->result = malloc(m1Row*sizeof(int*));
    for(int i = 0; i<m1Row; i++) {
        parameters->result[i] = malloc(m2Col*sizeof(int));
    }

    pthread_t threads[m1Row];

    struct timeval start, end;
    gettimeofday(&start, NULL);
    for(int i = 0; i<m1Row; i++) {
        struct threadArgs *tArgs = (struct threadArgs*)malloc(sizeof(struct args*) + sizeof(int));
        tArgs->parameters = parameters;
        tArgs->rowNum = i;
        pthread_create(&threads[i], NULL, concurrent, (void *)tArgs);
    }
    for(int i = 0; i<m1Row; i++) {
        pthread_join(threads[i], NULL);
    }
    gettimeofday(&end, NULL);
    for(int i = 0; i<m1Row; i++) {
        printf("[ ");
        for(int j = 0; j < m2Col; j++) {
            printf("%d ", parameters->result[i][j]);
        }
        printf("]\n");
    }
    printf("Took %ld seconds to multiply [%dx%d] on [%dx%d] concurrently\n", end.tv_sec-start.tv_sec, m1Row, m1Col, m2Row, m2Col);
    free(parameters);
}

int main() {
    // generate super large matrices
    int dimension = 300;
    int largeA[dimension][dimension], largeB[dimension][dimension];
    for(int i = 0; i<dimension; i++) {
        for(int j = 0; j<dimension; j++) {
            largeA[i][j] = 1;
            largeB[i][j] = 1;
        }
    }
    sequential(2, 3, m1, 3, 2, m2);
    startConcurrent(2, 3, m1, 3, 2, m2);
    /*
     * Code below tests performance of both solutions at large matrices
     *
    sequential(dimension, dimension, largeA, dimension, dimension, largeB);
    startConcurrent(dimension, dimension, largeA, dimension, dimension, largeB);
    */
    return 0;
}
