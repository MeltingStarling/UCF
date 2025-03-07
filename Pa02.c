/*
| Assignment: pa02 - Calculating an 8, 16, or 32 bit
| checksum on an ASCII input file
|
| Author: Joshua Herrera
| Language: c
|
| To Compile:
| gcc -o pa02 pa02.c
|
| To Execute:
| or c -> ./pa02 inputFile.txt 8
| where inputFile.txt is an ASCII input file
| and the number 8 could also be 16 or 32
| which are the valid checksum sizes, all
| other values are rejected with an error message
| and program termination
|
| Note: All input files are simple 8 bit ASCII input
|
| Class: CIS3360 - Security in Computing - Fall 2023
| Instructor: McAlpin
| Due Date: 1/12/23
*/

// Needed for my IDE (VSC)
#define _CRT_SECURE_NO_WARNINGS

// Libraries!
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

unsigned long updateChecksum(unsigned long checksum, unsigned char value, int checksumSizeBits) {
    // Initialize vars
    unsigned long sum = checksum + value;

    // Overflow for 16 bits
    if (checksumSizeBits == 16) {
        while (sum >> 16) {
            sum = (sum & 0xFFFF) + (sum >> 16);
        }
    }
    // Overflow for 32 bits
    else if (checksumSizeBits == 32) {
        while (sum >> 32) { 
            sum = (sum & 0xFFFFFFFF) + (sum >> 32);
        }
    }
    return sum & ((1UL << checksumSizeBits) - 1); // Mask to the correct checksum size
}

// The main!
int main(int inputChar, char* InputValue[]) {
    // Error if theres not exactly 3 inputs
    if (inputChar != 3) {
        fprintf(stderr, "Usage: %s <input_file> <checksum_size>\n", InputValue[0]);
        return EXIT_FAILURE;
    }

    // Init Var
    int checksumSize = atoi(InputValue[2]);

    // Error if input size != 8,16,32
    if (checksumSize != 8 && checksumSize != 16 && checksumSize != 32) {
        fprintf(stderr, "Valid checksum sizes are 8, 16, or 32\n");
        return EXIT_FAILURE;
    }

    // Open the file
    FILE* file = fopen(InputValue[1], "rb");
    if (!file) {
        perror("Error opening file");
        return EXIT_FAILURE;
    }

    // Init values
    unsigned long checksum = 0;
    size_t bytesRead;
    unsigned char buffer[1];
    int characterCnt = 0;
    int rowCharCnt = 0;

    // Read file and set checksum
    while ((bytesRead = fread(buffer, 1, 1, file)) > 0) {
        checksum = updateChecksum(checksum, buffer[0], checksumSize);
        characterCnt++;
        putchar(buffer[0]);
        rowCharCnt++;
        if (rowCharCnt == 80) {
            putchar('\n');
            rowCharCnt = 0;
        }
    }

    // Everything padding related
    unsigned char padChar = 'X';
    while ((checksumSize == 16 && characterCnt % 2 != 0) || (checksumSize == 32 && characterCnt % 4 != 0)) {
        checksum = updateChecksum(checksum, padChar, checksumSize);
        characterCnt++;
        putchar(padChar);
        rowCharCnt++;

        // Set everything into blocks of 80
        if (rowCharCnt == 80) {
            putchar('\n');
            rowCharCnt = 0;
        }
    }

    // Initiates a newline if needed
    if (rowCharCnt > 0) {
        putchar('\n');
    }

    // Close the file
    fclose(file);

    // Output checksum in hex
    // 8-Bit checksum 
    if (checksumSize == 8) {
        printf("%2d bit checksum is %02lx for all %4d chars\n", checksumSize, checksum & 0xFF, characterCnt);
    }
    
    // 16 bit checksum
    else if (checksumSize == 16) {
        printf("%2d bit checksum is %04lx for all %4d chars\n", checksumSize, checksum & 0xFFFF, characterCnt);
    }

    // 32 Bit checksum
    else if (checksumSize == 32) {
        printf("%2d bit checksum is %08lx for all %4d chars\n", checksumSize, checksum, characterCnt);
    }

    // End
    return EXIT_SUCCESS;
}

/*=============================================================================
| I Joshua Herrera jo053285 affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+============================================================================*/