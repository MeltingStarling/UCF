#include <stdio.h>
#include <stdlib.h>

// Here, we take the input from mergesort() and put it all together in 
// Lowest to highest order
void merge(long long arr[], long long l, long long m, long long r) {
    long long i;
    long long j;
    long long k;
    long long n1 = m - l + 1;
    long long n2 = r - m;

    // Used for temp arrays on left and right
    long long L[n1], R[n2];

    // Take info into the left
    for (i = 0; i < n1; i++)
        L[i] = arr[l + i];
    // Take info into the right
    for (j = 0; j < n2; j++)
        R[j] = arr[m + 1 + j];

    // Merge into the big boi from least to biggest
    i = 0; 
    j = 0; 
    k = l; 
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            arr[k] = L[i];
            i++;
        }
        else {
            arr[k] = R[j];
            j++;
        }
        k++;
    }

    // Copy any left over array info
    while (i < n1) {
        arr[k] = L[i];
        i++;
        k++;
    }

    // Copy any left over right array info
    while (j < n2) {
        arr[k] = R[j];
        j++;
        k++;
    }
}

// Divides recursively then sends to merge function
void mergesort(long long arr[], long long l, long long r) {
    if (l < r) {
        long long m = l + (r - l) / 2;

        // Recursion
        mergesort(arr, l, m);
        mergesort(arr, m + 1, r);

        // Send to merge
        merge(arr, l, m, r);
    }
}

// My main!
int main() {
    // n = num of cars 
    long long n;

    // p = deceleration constant
    long long p;

    // Take input
    scanf("%lld %lld", &n, &p);

    // Take in the distance nums into an array 
    long long distances[n];
    for (long long i = 0; i < n; i++) {
        scanf("%lld", &distances[i]);
    }

    // Sort the distances with mergesort
    mergesort(distances, 0, n - 1);

    // Main of main
    // Here we use the formula to calculate the distance
    long long min_distance = 0;
    for (long long i = 0; i < n; i++) {
        long long main_distance = p * (i + 1);
        long long previous_distance = distances[i];
        if (main_distance - previous_distance > min_distance) {
            min_distance = main_distance - previous_distance;
        }
    }

    // Output
    printf("%lld\n", min_distance + distances[0]);

    // End program
    return 0;
}
