tb791's CM10228 Coursework Submission

==GENERAL==
Each program, when run with the correct command line arguments, will produce some console output. The results of the algorithms (for q2, the sorted list of names, etc.) will be 
	outputted to a text file called results.txt. This file will be overwritten if it exists already.
	
	==QUICKSTART==
	Q1: java CW2Q1 names.txt
	Q2: gcc q2.c && ./a.out names.txt
	Q3: gcc q3.c && ./a.out names.txt
	Q4: java CW2Q4 names.txt
	Q5: gcc q5.c && ./a.out debate.txt redact.txt
	Q6: java CW2Q6 warandpeace.txt redact.txt
	Q7: gcc q7.c && ./a.out input.txt LOVELACE
	Q8: java CW2Q8 


==C PROGRAMS==

==Q2==

Question 2 - QuickSort algorithm
Usage: ./a.out <filename> 	- filename should be a list of names, all on one line, with each name separated by commas. The program can handle "" quoted or unquoted names, but
					please make sure all names conform to one standard (i.e. all names are quoted or unquoted). Unquoted names will be processed faster but the 
					overhead is minimal for quoted names.

==Q3==

Question 3 - HashTable program
Usage: ./a.out <filename>	- as above, the program can handle quoted names but will only process and store A-Za-z chars in values. Input file should be one line, comma-separated.
					Functions implemented are void add(name), void removeName(name), and int search(name). The hashtable is 20,000 elements large and the bucket
					size is 13 (as this was the smallest achievable with the hashing algorithm and namelist used). The values of ARRAY_LEN, NAME_LEN and BUCKET_SIZE
					can be changed if a different list is used to test the code. Suitably increasing ARRAY_LEN and BUCKET_SIZE /should/ allow the code to work with
					larger namelists. This code includes a small management engine to allow the examiner to interact with the list. Please try changing the size
					of the bucket if adding a name to the list through this engine results in a crash with error "fatal collision occurred. exiting." as this will
					fix the issue. A name will not be added to the list if it exists already.

==Q5==

Question 5 - Redacting text
Usage: ./a.out <file> <redact>	- This program should be able to handle any type of text file given the character encoding is compatible. Note that the redact file will read in the
					words to be redacted as words, not as lines (as it is split by SPACE " " characters). This means that the algorithm is unable to censor 
					(for example) "Manchester United" without also censoring "Manchester" and "United". 

==Q7== 

Question 7 - Columnar Transposition Cipher
Usage: ./a.out <textfile> <key>	- This program handles any text file input, removes all punctuation and spaces, and outputs the result as a string of chars to a file. The key is 
					specified as a command-line argument rather than as a file name on the command line (ex. for testing with default settings, run the code
					with './a.out text.txt LOVELACE' . The cipher handles duplicate letters in the key by incrementing subsequent letters' ASCII values by 1
					until there are no more duplicate letters. Thus, any key (as long as it is plain text) should be accepted by the program (but using a key
					of length 1 is futile).


==JAVA PROGRAMS==

==Q1==

Question 1 - BubbleSort algorithm
Usage: java CW2Q1 <filename>	- This program performs bubblesort on a list of names imported from a plaintext file. It can handle either quoted or unquoted names,
										or a mix of both. The program then prints the sorted list of names to a file called results.txt
		
==Q4==

Question 4 - XOR Linked List	- This program doesn't have full functionality. It instantiates a linked list which it can traverse and search for elements in but 
										cannot correctly add or remove elements.
Usage: java CW2Q4 <filename>

		
==Q6==

Question 6 - Redacting text pt.2
Usage: java CW2Q6 <filename> <redact> - This program redacts all words beginning with a capital letter (with a few exceptions) from a text file.
											It is also capable of redacting further non-capital-beginning words inputted from the redact text file. An attempt was made to 
											prevent redaction of words which may commonly start a sentence (i.e. begin with a capital letter and not be a proper noun) but
											it is by no means a perfect algorithm (the words for which exceptions are awarded are listed at the top of the file as elements of 
											the "temp" vector).
											
==Q8==

Question 8 - Project Euler summing diagonals
Usage: java CW2Q8 			- This program does not read args[]. The program will allow the user to calculate the size of an n by n -sized number spiral by inputting n. It also
									outputs the value of the 1001x1001 spiral first, as per the spec. 