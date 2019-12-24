#include <stdio.h>
#include <stdlib.h>
//last working: 20,000, 20, 1, 13
const int ARRAY_LEN = 20000;		//the max length of the storage array.
const int NAME_LEN = 20;		//the max length of a name (change this if necessary, it shouldn't affect functionality)
const int SCALER = 1;			//if necessary, reduce the hash function's output number size to map onto the array (careful of overflows)
const int BUCKET_SIZE = 13;		//the maximum number of collisions that can occur before the program exits.

char** array;
int maxNumAchievedSoFar = 0;
int numOfColls = 0;
int namesStored = 0;

void add(char* name);
void removeName(char* name);
int search(char* name);
int getIndexOf(char* name);
int hash(char* name);
int strcomp(char* one, char* two, int charIndex);
int lengthOf(char* s);
void printList(void);
void importFileToVar(char* filename);
char* appendToString(char* str, char c);

int main(int argc, char** argv){
	if(argc==1) {
		printf("Usage: %s <filename>\n", argv[0]);
		return 0;
	}
	array = malloc(sizeof(char*)*ARRAY_LEN);
	if(!array){
		printf("malloc error. exiting.\n");
		return -1;
	}
	for(int i = 0; i<ARRAY_LEN; i++){
		array[i] = malloc(NAME_LEN*sizeof(char));
		if(!array[i]){
			printf("malloc error. exiting.\n");
			return -1;
		}
	}

	printf("[INFORMATION] \nmax names:\t%d\nmax len(name):\t%d\nmax coll.n's:\t%d\n", ARRAY_LEN, NAME_LEN, BUCKET_SIZE);
	importFileToVar(argv[1]);
//	printList();							//to see list of names and addresses they are stored in, uncomment this line
	printf("%d names stored.\n\n",namesStored);

	char c;
	printf("WELCOME TO TB791'S MANAGEMENT CONSOLE FOR A HASHTABLE IMPLEMENTATION\n");
	printf("\nEnter operation you would like to perform\n(A - add, R - remove, S - search, P - print all assignments, E - exit) >");
	while((c = getchar())!= 'E'){
		if(c=='\n') ;
		else{
			if(c=='A'){
				printf("Enter the name you would like to add >");
				char* temp = malloc(NAME_LEN);
				scanf("%s", temp);
				printf("adding %s\n",temp);
				add(temp);
				printf("\nEnter operation you would like to perform\n(A - add, R - remove, S - search, P - print all assignments, E - exit) >");
			}
			else if(c=='R'){
				printf("Enter the name you would like to remove >");
				char* temp = malloc(NAME_LEN);
				scanf("%s", temp);
				removeName(temp);
				printf("\nEnter operation you would like to perform\n(A - add, R - remove, S - search, P - print all assignments, E - exit) >");
			}
			else if(c=='S'){
				printf("Enter the name you would like to search for >");
				char* temp = malloc(NAME_LEN);
				scanf("%s", temp);
				if(search(temp)!=-1) printf("FOUND at position %d\n", search(temp));
				else printf("NOT FOUND\n");
				printf("\nEnter operation you would like to perform\n(A - add, R - remove, S - search, P - print all assignments, E - exit) >");
			}
			else if(c=='P'){
				printList();
				printf("\nEnter operation you would like to perform\n(A - add, R - remove, S - search, P - print all assignments, E - exit) >");
			}
			else{
				printf("Command not recognised.\n");
				printf("\nEnter operation you would like to perform\n(A - add, R - remove, S - search, P - print all assignments, E - exit) >");
			}
		}
	}

	return 0;
}

//a function to hash the names. Returns the hash as an integer.
int hash(char* name){
	int index = lengthOf(name);
	for(int i = 0; i< lengthOf(name)-1; i++){
		index+=name[i];
		index*=name[i];
		while(index>=ARRAY_LEN){
			index %= ARRAY_LEN;
		}
	}
	maxNumAchievedSoFar = (maxNumAchievedSoFar<(index/SCALER))?(index/SCALER):(maxNumAchievedSoFar);
	return index/SCALER;
}

//adds a name to the list of names by resizing array.
void add(char* name){
	if(search(name)!=-1){
		printf("Name already exists in table!\n");
		return;
	}
	int index = hash(name);
	int bucketPos = 0;
	while(lengthOf(array[index]) != 1){
//		printf("COLLISION %d DETECTED ON TOKEN %s WITH ELEMENT %s\n", ++numOfColls, name, array[index]);
		if(index>=ARRAY_LEN || bucketPos>=BUCKET_SIZE){
			printf("fatal collision occurred (try making bucket bigger!). exiting.\n");
			exit(0);
		}
		index++;
		bucketPos++;
	}
	namesStored++;
	array[index] = name;
}

//the function to remove a name from the table.
void removeName(char* name){
	int index = search(name);
	if(index != -1) {
		array[search(name)]=malloc(NAME_LEN);
	}
	else {
		printf("Name Not Found!\n");
	}
}

//the function to search for a name. First it hashes the name, then does strcomp to see if that was indeed the name it wanted.
//It returns the index of the element, or -1 if it doesn't exist. Since -1 is false, any other return value is true and so this
//function can be resolved to a boolean expression easily.
int search(char* name){
	int supposedIndex = hash(name);
	if(strcomp(array[supposedIndex], name, 0)==0){		//if the name did not collide on storage it will be here.
		return supposedIndex;
	}
	else{
		int i = supposedIndex;
		while(i++<BUCKET_SIZE){				//otherwise search the bucket for the name.
			if(strcomp(array[i], name, 0)==0){
				return i;
			}
		}
	}
	return -1;		//return -1 if the name is not found.
}

//this function returns 0 if the strings are the same, or 1 otherwise.
int strcomp(char* one, char* two, int charIndex){
	if(lengthOf(one)!=lengthOf(two)) return 1;		//if the two strings aren't the same length...
	else if (charIndex == lengthOf(one)) return 0;		//if the end of the word has been reached -> same
        if(one[charIndex]==two[charIndex]){			//else compare current char and move on.
                return strcomp(one,two,++charIndex);
        }
	else{
		return 1;
	}

}

//a function to calculate the length of a string including its null terminating char.
int lengthOf(char* s) {
        int length = 0;
        for(int p = 0; p<255; p++){
                if(s[p] == '\0'){
                        break;
                }
                else {
                        length++;
                }
        }
        return length+1;        //+1 to account for the null terminating character
}

//a function to print the list to the command line.
void printList(void){
	for(int i = 0; i<ARRAY_LEN; i++){
		if(lengthOf(array[i])!=1){
			printf("%d:\t%s\n",i,array[i]);
		}
	}
}

//a function which imports a list of names separated by commas.
void importFileToVar(char* filename){
        FILE *f;
        char* name = malloc(1);
        name[0] = '\0';
        f = fopen(filename, "r");
        if (f != NULL) {
                char c;
                while((c = fgetc(f))!=EOF){
                        if(c==','){
                                add(name);
                                name = malloc(1);
                                name[0] = '\0';
                        }
                        else if(c!='"'){
                                name = appendToString(name, c);
                        }
                }
                add(name);              //add the last name not marked by a comma.
                fclose(f);
        }
}

//a function which appends a char to a string. It returns the new string with the character
//appended to it.
char* appendToString(char* str, char c){
        int x = lengthOf(str);
        str = realloc(str, x+1);
        str[x-1] = c;
        str[x] = '\0';
        return str;
}
