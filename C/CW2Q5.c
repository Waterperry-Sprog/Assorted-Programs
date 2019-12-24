#include <stdio.h>
#include <stdlib.h>

char** sentence;
int wordsImported = 0;
char** redact;
int redactImported = 0;

int lengthOf(char* s);
void printArray(void);
void printSentence(void);
int strcomp(char* one, char* two, int charIndex);
char* prependToString(char* str, char c);
char* appendToString(char* str, char c);
void importFileToVar(char* filename);
void importRedactedFile(char* filename);
void add(char* name);
void addRedacted(char* word);
void censor(void);
char* shouldCensor(char* word);
char* asterisk(char* word);

int main(int argc, char** argv){
	if(argc==1||argc==2){
		printf("Usage: %s <filename> <redactedfilename>\n", argv[0]);
		exit(0);
	}

	printf("Processing redacted words. Please wait.\n");
//	for(int i = 2; i<argc; i++){
//		addRedacted(argv[i]);
//	}

	redact = malloc(sizeof(char*));
	sentence = malloc(sizeof(char*));

	importRedactedFile(argv[2]);

	printf("Importing text.\n");
	importFileToVar(argv[1]);

	printf("Censoring.\n");
	censor();
	printArray();

	printf("Command executed successfully.\n");
	return 0;
}

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

void printArray(void){
        //print out sorted array
        FILE* f = fopen("results.txt","w+");
        for(int x = 1; x < wordsImported; x++){
                if(x==1) fprintf(f,"%s",sentence[x]);
                else fprintf(f," %s", sentence[x]);
        }
        fprintf(f,"\n");
        fclose(f);
}


//this function returns 0 if the strings match, 1 otherwise.
int strcomp(char* one, char* two, int charIndex){
	if(lengthOf(one)!=lengthOf(two)) {
		return 1;
	}
	if(charIndex==lengthOf(one)){
		return 0;
	}
	else if(one[charIndex]==two[charIndex]){
		return strcomp(one,two,++charIndex);
	}
	return 1;
}

void importFileToVar(char* filename){
        FILE *f;
        char* word = malloc(1);
        word[0] = '\0';
        f = fopen(filename, "r");
        if (f != NULL) {
                char c;
                while((c = fgetc(f))!=EOF){
                        if((c==' '||c=='\0')&&(sizeof(word)!=sizeof(char))){
                                add(word);
                                word = malloc(1);
                                word[0] = '\0';
                        }
                        else{
                                word = appendToString(word, c);
                        }
                }
                add(word);              //add the last name not marked by a comma.
                fclose(f);
        }
}
void importRedactedFile(char* filename){
        FILE *f;
        char* word = malloc(1);
        word[0] = '\0';
        f = fopen(filename, "r");
        if (f != NULL) {
                char c;
                while((c = fgetc(f))!=EOF){
                        if((c==' '||c=='\0'||c=='\n'||c=='\t')&&(sizeof(word)!=sizeof(char))){		//only add the word if it isn't a '\0' or similar.
                                addRedacted(word);
                                word = malloc(1);
                                word[0] = '\0';
                        }
                        else{
                                word = appendToString(word, c);
                        }
                }
                addRedacted(word);              //add the last name not marked by a comma.
                fclose(f);
        }
}

void add(char* c){
	char** temp;
	temp = (char**) malloc(sizeof(char*)*(++wordsImported));

	//clone sentence into temp
	for(int i = 0; i<wordsImported; i++){
		temp[i] = sentence[i];
	}

        sentence = temp;

        //resize target char*
        sentence[wordsImported] = malloc(lengthOf(c)*sizeof(char));

        //copy name char by char
        for(int i = 0; i<lengthOf(c); i++){
                sentence[wordsImported][i] = c[i];
        }
}

void addRedacted(char* c){
	char** temp;
	temp = (char**) malloc(sizeof(char*)*(++redactImported));


	//clone current redact into temp
	for(int i = 0; i<redactImported; i++){
		temp[i] = redact[i];
	}

        redact = temp;

        //resize target char*
        redact[redactImported] = malloc(lengthOf(c)*sizeof(char));

        //copy name char by char
        for(int i = 0; i<lengthOf(c); i++){
                redact[redactImported][i] = c[i];
        }
}

char* appendToString(char* str, char c){
        int x = lengthOf(str);

	char* temp = malloc((x+1)*sizeof(char));

	for(int i = 0; i<x; i++){
		temp[i] = str[i];
	}

        str = temp;
        str[x-1] = c;
        str[x] = '\0';
        return str;
}

char* prependToString(char* str, char c){
        int x = lengthOf(str);
	char* temp = (char*) malloc((x+1)*sizeof(char));
	for(int i = x; i>0; i--){
		temp[i] = str[i-1];
	}
        temp[0] = c;
        return temp;
}

void printSentence(void){
	for(int i = 1; i<=wordsImported; i++){
		if(i==1) printf("%s",sentence[i]);
		else printf(" %s", sentence[i]);
	}
	printf("\n");
}

void censor(void){
	for(int i = 1; i<wordsImported; i++){
		sentence[i] = shouldCensor(sentence[i]);
	}
}

int isChar(char c){
	if((c>64 && c<91) || (c>96 && c<123)) return 0;
//	else if (c=='\0') return 0;
	return 1;
}

char* shouldCensor(char* word){
	if(lengthOf(word)<2) return word;
	for(int i = 1; i<=redactImported; i++){
		if(isChar(word[0])!=0){
			char* subWord = malloc(lengthOf(word)-1);
			for(int i = 1; i<lengthOf(word); i++){
				subWord[i-1] = word[i];
			}
			return prependToString(shouldCensor(subWord),word[0]);
		}
		if(isChar(word[lengthOf(word)-2])!=0){
			char* subWord = malloc(lengthOf(word)-1);
			for(int i = 0; i<lengthOf(word)-2; i++){
				subWord[i] = word[i];
			}
			subWord[lengthOf(word)-2] = '\0';
			return appendToString(shouldCensor(subWord), word[lengthOf(word)-2]);
		}
		if(strcomp(word, redact[i], 0)==0){
			return asterisk(word);
		}
	}
	return word;
}

char* asterisk(char* word){
	char* str = malloc(1);
	str[0] = '\0';
	for(int i = 0; i<lengthOf(word)-1; i++){
		str = appendToString(str, '*');
	}
	return str;
}
