//run this with ./a.out <filename> <key>

#include <stdio.h>
#include <stdlib.h>

char* key;			//the key read from the CLI.
char* sortedKey;		//the key sorted alphabetically with no duplicate characters.
char* message;			//the message to be encoded
const int INTEGER_MAX = 2147483647;
char** columns;			//the message divided into columns
char** encCols;			//the message divided into alphabetically sorted columns
int* indexArray;		//an array of values representing the mapping of sortedKey values onto key values (key BAC leaves this array in state [1],[0],[2] for example)

void padMessage(void);		//function to pad message[] with X's if necessary
void addToMessage(char str[]);
void ensureKeyHasUniqueVals(void);
int lengthOf(char str[]);
int findIndexOf(char c);
void splitMessageIntoColumns(void);
void genIndexArray(void);
void sortKey(void);
void swapCols(void);
void printEncMessage(void);
void importMessage(char* filename);

int main(int argc, char** argv){
	if(argc==1||argc==2){
		printf("Usage: %s <filename> \"key\"\n", argv[0]);
		return 0;
	}

	//import message from specified file.
	message = malloc(sizeof(char)*1);
	importMessage(argv[1]);

	//ASSIGN key variable and message variable
	key = realloc(key, lengthOf(argv[2]));
	key = argv[2];
	ensureKeyHasUniqueVals();

	//pad the message with X's if it doesn't fit into the columns as is.
	padMessage();

	//divide the array into columns
	splitMessageIntoColumns();

	//generate the index array so that characters in columns can be put into their correct place for the encryption.
	genIndexArray();

	//swap characters into their encoded positions.
	swapCols();

	//output the file.
	printEncMessage();
	printf("Result is saved to results.txt\n");
	return 0;
}


//this function returns the length of a string (char*) in memory (i.e. hello is 6).
int lengthOf(char* str){
	int length = 0;
	for(int i = 0; i < INTEGER_MAX; i++){
		if(str[i] == '\0'){
			break;
		}
		else{
			length++;
		}
	}
	return ++length;
}

//this function decides whether a given char c is a letter (ABC...XYZ or abc...xyz) or not.
//it returns 0 if c is a letter, and 1 otherwise.
int isChar(char c){
	if((c>64&&c<91)||(c>96&&c<123)){
		return 0;
	}
	else{
		return 1;
	}
}

//this function appends a string to the message variable by resizing the message.
void addToMessage(char str[]){
	char* temp = malloc(sizeof(char)*lengthOf(str) + lengthOf(message));
	for(int i = 0; i<lengthOf(message)-1;i++){
		temp[i] = message[i];
	}

	int x = lengthOf(message)-1;
	for(int i = 0; i<lengthOf(str); i++){
		if(isChar(str[i])==0){
			temp[x++] = str[i];

		}
	}
	message = temp;
}

//this function reads the input file line by line and submits each line to addToMessage.
void importMessage(char* filename){
	FILE *f;
	f = fopen(filename, "r");
	if (f != NULL) {
		fseeko(f, 0, SEEK_END);
		long endPos = ftello(f);
		fseeko(f, 0, SEEK_SET);
		char* c = malloc(endPos*sizeof(char));

		while((fgets(c, endPos, f))!=NULL){
			addToMessage(c);
		}
		fclose(f);
	}
}

//this function pads the message with 'X' characters if necessary using modulo.
void padMessage(void){
	int keyLen = lengthOf(key)-1;
	while(((lengthOf(message)-1 )%keyLen) != 0){
		int newLen = lengthOf(message) + 1;
		char* temp = malloc(newLen);
		for(int i = 0; i<lengthOf(message); i++){
			temp[i] = message[i];		//copy existing chars from message, then pad message.
		}
		temp[newLen-2] = 'X';
		temp[newLen-1] = '\0';
		message = temp;
	}
}

//this function ensures that duplicate letters in the key are dealt with properly.
//the first occurrence of the duplicate letter keeps its place: subsequent occurrences get
//their value increased by one until there are no duplicates in the array.
void ensureKeyHasUniqueVals(void){
	for(int q = 0; q<lengthOf(key); q++){
		for(int p = 0; p<lengthOf(key); p++){
			if((p!=q)&&(key[p]==key[q])){
				key[q] += 1;
				p = 0;
				q = 0;
			}
		}
	}
}

//this function searches the original key and finds the original position of the char c.
int findIndexOf(char c){
	for(int i = 0; i<lengthOf(key); i++){
		if(key[i]==c) return i;
	}
	return -1;
}

//this function divides the message up into columns which are the size of the key provided.
void splitMessageIntoColumns(void){
	int numberOfColumns = (lengthOf(message)-1)/(lengthOf(key)-1);
	columns = malloc(sizeof(char*)*numberOfColumns);
	encCols = malloc(sizeof(char*)*numberOfColumns);
	for(int i = 0; i<numberOfColumns; i++){
		columns[i] = malloc(sizeof(char) * (lengthOf(key) - 1));
		encCols[i] = malloc(sizeof(char) * (lengthOf(key) - 1));
	}
	for(int i = 0; i<lengthOf(message)-1; i++){		//lengthOf-1 as we dont want to copy the nullchar
 		int y = (int)( ((double)i)/((double)(lengthOf(key)-1)) );
		int x = i%(lengthOf(key)-1);
		columns[y][x] = message[i];
	}
}

//this function generates the index array (i.e. the target position of each letter in the columns).
void genIndexArray(void){
	sortKey();
	indexArray = malloc(sizeof(int)*(lengthOf(key)-1));
	for(int i = 0; i<lengthOf(key)-1; i++){
		indexArray[i] = findIndexOf(sortedKey[i]);
	}
}

//this function executes a simple bubble sort on the letters in the key to put them alphabetically.
void sortKey(void){
	int swapOccurred = 0; 		//0 = true, 1 = false
	char* sortMe = malloc(sizeof(char)*lengthOf(key));
	for(int i = 0; i<lengthOf(key); i++){
		sortMe[i] = key[i];		//can't just set sortMe = key, otherwise we sort key
	}
	while(0==0){
		swapOccurred = 1;
		for(int i = 0;i<lengthOf(key)-2;i++){
			if(sortMe[i] > sortMe[i+1]){
				char temp = sortMe[i];
				sortMe[i] = sortMe[i+1];
				sortMe[i+1] = temp;
				swapOccurred = 0;
			}
		}
		if(swapOccurred == 1){
			break;
		}
	}
	sortedKey = sortMe;
}

//this function swaps the letters in the column so that they align with the key value's new arrangement.
void swapCols(void){
	int numberOfColumns = (lengthOf(message)-1)/(lengthOf(key)-1);
	for(int i = 0; i<numberOfColumns; i++){
		for (int x = 0; x<lengthOf(key)-1; x++){
			encCols[i][x] = columns[i][indexArray[x]];
		}
	}
}

//this function prints the encrypted message to a file called "results.txt"
void printEncMessage(void){
	FILE *fp;
	fp = fopen("results.txt","w+");
	int numberOfColumns = (lengthOf(message)-1)/(lengthOf(key)-1);
	for(int i = 0; i<numberOfColumns; i++){
		for (int x = 0; x<lengthOf(key)-1; x++){
			fprintf(fp,"%c",encCols[i][x]);
		}
	}
	fprintf(fp,"\n");
	fclose(fp);
}
