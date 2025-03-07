// Joshua Herrera
// COP 3502C
// 9/29/2023
// wheretositb.c

#include <stdio.h>
#include <stdlib.h>

// Depth-First-Search recusively called!
void depth_first_search(int node, int visited[], int** graph, int numIslands) {
    visited[node] = 1;
    for (int i = 1; i <= numIslands; i++) {
        if (!visited[i] && graph[node][i]) {
            depth_first_search(i, visited, graph, numIslands);  //Recursive call for requirements
        }
    }
}

int main() {
    int numIslands, numBridges;
    scanf("%d%d", &numIslands, &numBridges);

    // Create the graph with DMA
    int** graph = (int**)malloc((numIslands + 1) * sizeof(int*));   //DMA for reqs
    for (int i = 0; i <= numIslands; i++) {
        graph[i] = (int*)calloc(numIslands + 1, sizeof(int));   //DMA for reqs
    }

    // Use the info from above to create the amount of bridges and islands
    for (int i = 0; i < numBridges; i++) {
        int island1, island2;
        scanf("%d%d", &island1, &island2);
        graph[island1][island2] = 1;
        graph[island2][island1] = 1;
    }

    //Determine if the islands been visited yet
    int* visited = (int*)calloc(numIslands + 1, sizeof(int));

    // Call Depth-First-Search
    depth_first_search(1, visited, graph, numIslands);

    // Check if all islands connect
    int connected = 1;
    for (int i = 1; i <= numIslands; i++) {
        if (!visited[i]) {
            connected = 0;
            break;
        }
    }

    // Print
    if (connected) {
        printf("YES\n");
    } else {
        printf("NO\n");
    }

    // Free the DMA
    for (int i = 0; i <= numIslands; i++) {
        free(graph[i]);
    }
    free(graph);
    free(visited);

    return 0;
}
