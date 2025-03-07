// Joshua Herrera
// Guha MWF 3502C
// inventory.c (p6)
// 11/27/23

// Defines
#define _CRT_SECURE_NO_WARNINGS
#define MAXLEN 19
#define TABLESIZE 300007
#define INITCASH 100000

// Libraries
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// item struct
typedef struct item {
	char name[MAXLEN + 1];
	int quantity;
	int saleprice;
} item;

// linked list struct
typedef struct node {
	item* iPtr;
	struct node* next;
} node;

// Hash Table struct
typedef struct hashtable {
	node** lists;
	int size;
} hashtable;

// Guha's special-Hash function
int hashFunction(char* word, int size) {
	int length = strlen(word);
	int result = 0;
	for (int i = 0; i < length; i++)
		result = (1151 * result + (word[i] - 'a')) % size;
	return result;
}

// Global Variables
int totalCash = INITCASH;
int totalComplexity = 0;

// Buy input function
void BuyFunction(hashtable* hashTable, char* itemName, int quantity, int totalprice) {
	int index = hashFunction(itemName, hashTable->size);
	node* current = hashTable->lists[index];

	// Timecomplexity runtime variable
	int runtime = 1;

	// Search for the item in the linked list
	while (current != NULL && strcmp(current->iPtr->name, itemName) != 0) {
		current = current->next;
		runtime++;
	}

	// Creates a new item if item not found
	if (current == NULL) {
		// DMA
		item* newItem = (item*)malloc(sizeof(item));
		strcpy(newItem->name, itemName);
		newItem->quantity = quantity;

		// Start with price at 0, update if necessary
		newItem->saleprice = 0;

		// DMA
		node* newNode = (node*)malloc(sizeof(node));
		newNode->iPtr = newItem;
		newNode->next = hashTable->lists[index];
		hashTable->lists[index] = newNode;

		// Update runtime
		totalComplexity += runtime;
	}
	else {
		// Item found, update its quantity
		current->iPtr->quantity += quantity;

		// Update runtime
		totalComplexity += runtime;
	}

	// Update total cash
	totalCash -= totalprice;

	// Output
	printf("%s %d %d\n", itemName, (current == NULL) ? quantity : current->iPtr->quantity, totalCash);
}

// Sell command Function
void SellFunction(hashtable* hashTable, char* itemName, int quantityToSell) {
	int index = hashFunction(itemName, hashTable->size);
	node* current = hashTable->lists[index];
	// Timecomplexity runtime variable
	int runtime = 1;

	// Runs when there is something to do (i.e not NULL)
	while (current != NULL) {
		// If the item was found update information
		if (strcmp(current->iPtr->name, itemName) == 0) {
			int availableQuantity = current->iPtr->quantity;
			int soldQuantity = (availableQuantity >= quantityToSell) ? quantityToSell : availableQuantity;
			int saleAmount = soldQuantity * current->iPtr->saleprice;

			// Update inventory and total cash vars
			current->iPtr->quantity -= soldQuantity;
			totalCash += saleAmount;

			// Update total complexity
			totalComplexity += runtime;

			// Output
			printf("%s %d %d\n", itemName, current->iPtr->quantity, totalCash);
			return;
		}
		// Onto the next item
		current = current->next;

		// Increment runtime O(n)
		runtime++;
	}
}

// Change price input
void changePriceFunction(hashtable* hashTable, char* itemName, int newPrice) {
	// Find index from guhas special who hash
	int index = hashFunction(itemName, hashTable->size);

	// Find the item in the linked list at the index (From collisions)
	node* current = hashTable->lists[index];

	// Init timecomplexity variable
	int runtime = 1;

	// If there is still something to do update the sales price
	while (current != NULL) {
		if (strcmp(current->iPtr->name, itemName) == 0) {
			current->iPtr->saleprice = newPrice;

			// Updatin the time complexity
			totalComplexity += runtime;
			return;
		}

		// Onto the next item
		current = current->next;

		// Updatin da runtime for each item
		runtime++;
	}
}


// Init guhas who hash
void initializeHashTable(hashtable* hashTable) {
	// DMA
	hashTable->lists = (node**)malloc(TABLESIZE * sizeof(node*));

	// Set everything intitially to NULL
	for (int i = 0; i < TABLESIZE; i++) {
		hashTable->lists[i] = NULL;
	}

	// Hash table set to maxsize (TABLESIZE)
	hashTable->size = TABLESIZE;
}


// The main
int main() {
	// n = original input
	int numInput = 0;

	// Original Input n <300000
	scanf("%d", &numInput);

	// Init guhas who Hash Table
	hashtable hashTable;
	initializeHashTable(&hashTable);

	// Loop for inputs
	for (int i = 0; i < numInput; i++) {
		// Init vars
		char input[20], itemName[MAXLEN + 1];
		int amount = 0;
		int price = 0;

		// Main part of loop
		scanf("%s", input);
		if (strcmp(input, "buy") == 0) {
			scanf("%s %d %d", itemName, &amount, &price);
			BuyFunction(&hashTable, itemName, amount, price);
		}
		else if (strcmp(input, "sell") == 0) {
			scanf("%s %d", itemName, &amount);
			SellFunction(&hashTable, itemName, amount);
		}
		else if (strcmp(input, "change_price") == 0) {
			scanf("%s %d", itemName, &price);
			changePriceFunction(&hashTable, itemName, price);
		}
	}
	// Output
	printf("%d\n%d", totalCash, totalComplexity);

	// Needed for correct DMA
	for (int i = 0; i < TABLESIZE; i++) {
		node* current = hashTable.lists[i];
		while (current != NULL) {
			node* temp = current;
			current = current->next;
			free(temp->iPtr);
			free(temp);
		}
	}

	// Free meh pointers
	free(hashTable.lists);

	// End program
	return 0;
}