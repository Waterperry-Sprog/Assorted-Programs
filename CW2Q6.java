import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class CW2Q6 {

	public  Vector<String> censored = new Vector<String>();
	public  Vector<String> possibleNonProperNouns = new Vector<String>();	//this stores words which can frequently come at the start of sentences but which aren't proper nouns.
	public  String[] temp = {"It","He","She","Who","What","Where","When","Why","How","The","A","Then"};
	public  Vector<String> input = new Vector<String>();
	public  Vector<String> results = new Vector<String>();
	
	/**
	 * This method determines whether a given char is a capital letter.
	 * @param c The char to be processed.
	 * @return True if the char is upper case, false otherwise.
	 */
	public  boolean isCapital(char c) {
		if(c>=65 && c<=90) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method takes the substring starting at the index position of a given string s.
	 * @param s The string to be reduced.
	 * @param index The char to start reading the substring at.
	 * @return The substring (or the string, if the index is greater than the length of the string).
	 */
	public  String tail(String s, int index) {
		String h = "";
		if(index>s.length()) {
			return s;
		}
		for(int i = index; i<s.length();i++) {
			h = h + s.charAt(i);
		}
		return h;
	}
	
	/**
	 * This method takes the substring starting at the index position of a given string s.
	 * @param s The string to be reduced.
	 * @param index The char to start reading the substring at.
	 * @return The substring (or the string, if the index is greater than the length of the string).
	 */
	public  String head(String s, int index) {
		String h = "";
		if(index>s.length()) {
			return s;
		}
		for(int i = 0; i<index;i++) {
			h = h + s.charAt(i);
		}
		return h;
	}
	
	/**
	 * This method determines whether a given character is a lower-/upper-case letter or number.
	 * @param c the char to be processed.
	 * @return true if c is a letter or number, false otherwise.
	 */
	
	public  boolean isChar(char c) {
		if((c>=65 && c<=90)||(c>=97 && c<=122)||(c-'0'<10 && c-'0'>0)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param s
	 * @param c
	 * @return
	 */
	public  boolean contains(String s, char c) {
		for(int i = 0; i<s.length();i++) {
			if(s.charAt(i)==c) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method compares two strings to see if they are equal.
	 * @param x String one.
	 * @param y String two.
	 * @return true if the strings are equal, false otherwise.
	 */
	public  boolean strcmp(String x, String y) {
		if(x.length()!=y.length()) {
			return false;
		}
		for(int i = 0; i<x.length();i++) {
			if(x.charAt(i)!=y.charAt(i)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method returns a censored version of the input word (i.e. asterisks the length of the word).
	 * @param s The string to be censored.
	 * @return A string consisting of solely asterisks that is the same length as the input string.
	 */
	public  String censor(String s) {
		//make sure it is actually a proper noun.
		for(String str : possibleNonProperNouns) {
			if (strcmp(str,s)) {
				return s;
			}
		}
		String dummy = "";
		//set the input to a string of stars the same length as the string.
		for(int i = 0; i<s.length();i++) {
			dummy = dummy + "*";
		}
		return dummy;
	}
	
	/**
	 * This method searches the list of redacted words and returns true if the query exists in the list.
	 * @param s The string to be compared.
	 * @return True if the string exists in the list, false otherwise.
	 */
	public  boolean searchRedactedWords(String s) {
		for (String str : censored) {
			if(strcmp(str, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method processes a word to determine if it is a proper noun and/or should be censored.
	 * @param s The string (word) to be processed.
	 * @param onlySearchRedacted If true, this specifies that the word should only be censored if it exists
	 * 			in the list of redacted words.
	 * @return The string, either as a string of asterisks or in its original form.
	 */
	public  String processWord(String s, boolean onlySearchRedacted) {
		if(s.length() == 0) {
			return s;
		}
		//search vector for word
		if(searchRedactedWords(s)) {
			return censor(s);
		}
		
		if(!isChar(s.charAt(0))) {
			return s.charAt(0) + processWord( tail(s,1), onlySearchRedacted);
		}
		if(!isChar(s.charAt(s.length()-1))) {
			return processWord( head(s,s.length()-1), onlySearchRedacted) + s.charAt(s.length()-1);
		}
		
		//otherwise censor any proper noun
		else {
			//otherwise censor if its first letter is capital.
			if(isCapital(s.charAt(0)) && !onlySearchRedacted && s.length()>1){
				return censor(s);
			}
			
		}
		return s;
	}
	
	/**
	 * this method splits the string into words by spaces.
	 * @param input The input string (which should be the whole document).
	 * @return The fully processed string.
	 */
	public  String processString(String input) {
		String returnString = "";
		String word = "";
		for(int i = 0; i<input.length(); i++) {
			//if the character is not a space continue to construct the next word to be processed
			if(input.charAt(i)!=' ') {
				word = word + input.charAt(i);
			}
			//if the character is a space, process the word and reset it to an empty string
			if(input.charAt(i)==' ') {
				try {
					if( ! isChar(input.charAt(i - word.length() - 2))) {
						returnString = returnString + processWord(word, false) + " ";
						word = "";
					}
					else {
						returnString = returnString + processWord(word, false) + " ";
						word = "";
					}
				} catch (StringIndexOutOfBoundsException s) {
					returnString = returnString + processWord(word, false) + " ";
					word = "";
				}
			}
		}
		returnString = returnString + processWord(word, false);
		return returnString;
	}
	
	/**
	 * This method imports the redacted names to the vector storing them.
	 * @param filename The name of the file in the src folder which contains a list of redacted words.
	 */
	public  void importRedactedNames(String filename) {
		try {
			System.out.println("Redact file:\t"+filename);
			FileReader fr = new FileReader(new File(filename));
			char c;
			String name = "";
			
			while((c=(char)fr.read())!= 65535) {
				if (c==' '||c=='\n') {
					censored.add(name);
					name = "";
				}
				else {
					name = name + c;
				}
			}
			
			censored.add(name); 	//add the last name not marked by a comma
			fr.close();
			
		} catch(FileNotFoundException f) {
			
		} catch(IOException e) {
			
		}
	}
	
	/**
	 * This method reads a file into a string.
	 * @param filename The filename to be read into a string.
	 * @return The file contents as a single string.
	 */
	public  String importFileToString(String filename) {
		System.out.println("Text file:\t"+filename);
		try {
		InputStream i = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(i));
		String line = "";
		String s = "";
		while((line= br.readLine()) != null){
			results.add(processString(line) + "\r\n");
		}
		br.close();
		return s;
		
		}catch(FileNotFoundException f) {
			System.out.println("File not found. Exiting.");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method creates a results file and prints the redacted text into it. 
	 * @param resultName the name of the results file (currently only results.txt)
	 */
	public  void returnResults(String resultName) {
		File file = new File(resultName);
		try {
		if (!file.exists()) {
            file.createNewFile();
        }
		PrintWriter pw = new PrintWriter(resultName, "UTF-8");
		for(String s : results) {
			pw.append(s);
		}
		pw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if(args.length == 0){
			System.out.println("Usage: java CW2Q6 <filename> <redactedfilename>");
			System.exit(0);
		}
		
		CW2Q6 censorer = new CW2Q6();
		
		
		
		for(int i = 0; i<censorer.temp.length; i++) {
			censorer.possibleNonProperNouns.add(censorer.temp[i]);
		}
		
		System.out.println("Importing words to be redacted.");
		censorer.importRedactedNames(args[1]);
		
		System.out.println("Working.");
		censorer.processString( censorer.importFileToString(args[0]) );
		censorer.returnResults("results.txt");
		System.out.println("Done. Results outputted to results.txt .");
	}
}
