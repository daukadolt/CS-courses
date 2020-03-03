#include <stdio.h>
#include <sys/time.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>

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

struct threadArg {
    struct args *parameters;
    int rowNum;
};

void printMatrix(int row, int col, int** matrix) {
    for(int i = 0; i<row; i++) {
        for(int j = 0; j < col; j++) {
            printf(" %d ", matrix[i][j]);
        }
		printf("\n");
    }
}

void printTime(struct timeval start, struct timeval end, int m1Row, int m1Col, int isConcurrent) {
	double startTotalMicrosecs = start.tv_sec*1000*1000 + start.tv_usec;
	double endTotalMicrosecs = end.tv_sec*1000*1000 + end.tv_usec;
	double timeDiff = endTotalMicrosecs - startTotalMicrosecs;
    if(isConcurrent == 1) fprintf(stderr, "Took %lf microseconds to multiply [%dx%d] on [%dx%d] concurrently\n", timeDiff, m1Row, m1Col, m1Col, m1Row);
    else fprintf(stderr, "Took %lf microseconds to multiply [%dx%d] on [%dx%d] sequentially\n", timeDiff, m1Row, m1Col, m1Col, m1Row);
}

void sequential(int m1Row, int m1Col, int** m1, int m2Row, int m2Col, int** m2) {
    struct timeval start, end;
    int result[m1Row][m2Col];
    gettimeofday(&start, NULL);
    for(int i = 0; i<m1Row; i++) {
        for(int j = 0; j<m2Col; j++) {
            int currentCellSum = 0;
            for(int y = 0; y<m2Row; y++) {
                currentCellSum += m1[i][y]*m2[y][j];
            }
            result[i][j] = currentCellSum;
        }
    }
    gettimeofday(&end, NULL);
    printTime(start, end, m1Row, m1Col, 0);
}

void concurrent(struct threadArg *arguments){
    int currentRow = arguments->rowNum;
    for(int j = 0; j<arguments->parameters->m2Col; j++) {
        int currentCellSum = 0;
        for(int y = 0; y<arguments->parameters->m2Row; y++) {
            currentCellSum += arguments->parameters->m1[currentRow][y] * arguments->parameters->m2[y][j];
        }
        arguments->parameters->result[currentRow][j] = currentCellSum;
    }
    pthread_exit(NULL);
};


void startConcurrent(int m1Row, int m1Col, int** m1, int m2Row, int m2Col, int** m2, int** result) {
    struct args *parameters = (struct args *) malloc(sizeof(struct args));

    parameters->m1 = m1;
    parameters->m1Row = m1Row;
    parameters->m1Col = m1Col;

    parameters->m2 = m2;
    parameters->m2Row = m2Row;
    parameters->m2Col = m2Col;

    parameters->result = result;

    pthread_t threads[m1Row];

    struct timeval start, end;
    struct threadArg tArgs[m1Row];
    gettimeofday(&start, NULL);
    for(int i = 0; i<m1Row; i++) {
        struct threadArg *tArg = (struct threadArg*)malloc(sizeof(struct args*) + sizeof(int));
        tArg->parameters = parameters;
        tArg->rowNum = i;
        pthread_create(&threads[i], NULL, concurrent, (void *)tArg);
    }
    for(int i = 0; i<m1Row; i++) {
        pthread_join(threads[i], NULL);
    }
    gettimeofday(&end, NULL);
    printTime(start, end, m1Row, m1Col, 1);
    printMatrix(m1Row, m2Col, result);
    free(parameters);
}

int** generateMatrix(int rows, int cols, int val) {
    int** matrix = malloc(rows*sizeof(int*));
    for(int i = 0; i<rows; i++) {
        matrix[i] = malloc(cols*sizeof(int));
        for(int j = 0; j<cols; j++) {
            matrix[i][j] = val;
        }
    }
    return matrix;
}

int main(int argc, char** argv) {
	if(argc != 5) {
		fprintf(stderr, "Incorrent number of parameters\n");
		exit(-1);
	}
	int rows = atoi(argv[1]);
	int cols = atoi(argv[2]);
	if(rows > 1024 || cols > 1024) {
		fprintf(stderr, "Max rows 1024, max cols 1024\n");
		exit(-1);
	}
	int val1 = atoi(argv[3]);
	int val2 = atoi(argv[4]);
    
    int** matrix1 = generateMatrix(rows, cols, val1);
    int** matrix2 = generateMatrix(cols, rows, val2);
    int** result = generateMatrix(rows, rows, 1);

    sequential(rows, cols, matrix1, cols, rows, matrix2);
    startConcurrent(rows, cols, matrix1, cols, rows, matrix2, result);
    return 0;
}
