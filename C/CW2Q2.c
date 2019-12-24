#include <stdio.h>
#include <stdlib.h>

char** listToSort;		//a list of strings to be sorted.
int numberOfPointers = 1;	//the number of names to be sorted (basically sizeof(listToSort)/sizeof(char*) but this didn't work for some reason)
int namesImported = 0;

char* appendToString(char* str, char c);
void add(char* c);
void importFileToVar(char* filename);
void swap(int x, int y);
int lengthOf(char* s);
void printArray();
void sort(char** array, int lowerIndex, int higherIndex);
int split(char** arr, int lowerIndex, int higherIndex);
char** join(char** x, char** y);
int strcomp(char* one, char* two, int charIndex);

int main(int argc, char** argv) {
	if(argc==1){
		printf("Usage: %s <filename>\n", argv[0]);
		return 0;
	}

	//import list of names from file.
	printf("Importing names. Will notify on completion.\n");
	importFileToVar(argv[1]);
	printf("Names imported.\n");

	//sort it
	printf("printing array\n");
	printf("Sorting array\n");
	sort(listToSort,0,namesImported-1);
	printf("Printing result to results.txt\n");
	printArray();
	return 0;
}

//import the names to the listToSort array
void importFileToVar(char* filename){
	FILE *f;
	char* name = malloc(1);
	name[0] = '\0';
        f = fopen(filename, "r");
        if (f != NULL) {
			char c;
			while((c = fgetc(f))!=EOF){
				if(c=='"'){
				}
				else if(c==','){
					add(name);
					name = malloc(1);
					name[0] = '\0';
				}
				else{
					name = appendToString(name, c);
				}
			}
			add(name);		//add the last name not marked by a comma.
			fclose(f);
        }
}

//a function to resize listToSort in order to accommodate an extra name, and to then
//add the extra name to listToSort.
void add(char* c){
	listToSort = realloc(listToSort, sizeof(char*)*(++numberOfPointers));

	//resize target char*
	listToSort[namesImported] = malloc(lengthOf(c)*sizeof(char));

	//copy name char by char
	for(int i = 0; i<lengthOf(c); i++){
		listToSort[namesImported][i] = c[i];
	}
	namesImported++;
}

//a function to append a char to a string (used for string building when importing 
//names to the program).
char* appendToString(char* str, char c){
	int x = lengthOf(str);
	str = realloc(str, x+1);
	str[x-1] = c;
	str[x] = '\0';
	return str;
}

//this function swaps values that are in the wrong place relative to the pivot element. integer comparisons can be used as the data are names, but strcomp is used so that 
//the list is in dictionary order and not just grouped by first letter.
int split(char** array, int lowerIndex, int higherIndex) {
	char* pivot = array[higherIndex];		//set the pivot to the right-most element (bad for sorted data, but the test array isn't sorted so doesn't matter)

	int i = lowerIndex - 1;

	for (int j = lowerIndex; j < higherIndex; j++){
		if(strcomp(array[j],pivot,0)==0){		//swap if the values are unsorted as determined by strcomp
			i++;
			swap(i,j);
		}
	}
	swap(i+1,higherIndex);
	return i + 1;
}

//this function is called from main and initiates the quicksort on values determined to be lower/higher than the pivot returned by split().
//it then recursively calls itself on values lower/higher than the partition index until the lower/higher index are equal (i.e. until the array has been sorted).
void sort(char** array, int lowerIndex, int higherIndex) {
	if (lowerIndex < higherIndex) {
		int parIndex = split(array, lowerIndex, higherIndex);
		sort(array, lowerIndex, parIndex - 1);
		sort(array, parIndex + 1, higherIndex);
	}
}

//this function returns a char** composed of all elements from two input char**'s.
char** join(char** x, char** y){
	char** temp = malloc(sizeof(x) + sizeof(y));
	for(int i = 0; i< (int) sizeof(x); i++){
		temp[i] = x[i];
	}
	int head = sizeof(x);
	for(int i = 0; i< (int) sizeof(y); i++){
		temp[head++] = y[i];
	}
	return temp;
}

//this function swaps two values by copying the values of x into a temp variable, then y into x, then temp into y.
void swap(int x, int y) {
	int lenx = lengthOf(listToSort[x]);
	int leny = lengthOf(listToSort[y]);

        //copy x into temp.
	char* temp = malloc(lenx*sizeof(char));
	for(int i = 0; i<lenx; i++){
		temp[i] = listToSort[x][i];
	}
	//copy y into x.
	listToSort[x] = malloc(leny*sizeof(char));
	for(int i = 0; i<leny; i++){
		listToSort[x][i] = listToSort[y][i];
	}
        //copy temp into y.
	listToSort[y] = malloc(lengthOf(temp)*sizeof(char));
	for(int i = 0; i<lengthOf(temp); i++){
		listToSort[y][i] = temp[i];
	}
}

//this function determines the length of a string by finding the null terminating character. It returns the length
//including the null terminating character as an int. This method replaces sizeof(char* str).
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
	return length+1;	//+1 to account for the null terminating character
}

//this function prints the sorted array into a file called results.txt
void printArray(){
        //print out sorted array
	FILE* f = fopen("results.txt","w+");
        for(int x = 0; x < namesImported; x++){
		if(x!=0) fprintf(f,",");
                fprintf(f,"%s", listToSort[x]);
        }
        fprintf(f,"\n");
	fclose(f);
}

//this function returns 1 if input word one comes before input word two alphabetically, or 0 otherwise (i.e. if they should
//be swapped).
int strcomp(char* one, char* two, int charIndex){
	if(one[charIndex]==two[charIndex]){
		return strcomp(one,two,++charIndex);
	}
	else if(one[charIndex]<two[charIndex]){
		return 0;
	}
	return 1;

}
