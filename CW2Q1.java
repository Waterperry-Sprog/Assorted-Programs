import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class CW2Q1 {
	
	private  String[] returnList = new String[1];
	private  String invalid;		//only used if a string in the input is invalid.
	private  int currentIndex = 0;
	private  boolean swapOccurred = true;
	
	/**
	 * checks if the return array is a valid list of names (i.e. does not contain any special symbols etc.)
	 * @return true if the array is full of valid names, false otherwise.
	 */
	private  boolean checkValid() {
		System.out.println("Checking if names are of a valid format.");
		for(String s : returnList) {
			for(int p = 0; p < s.length(); p++) {
				int currentChar = s.charAt(p);
				if ( (currentChar > 64 && currentChar < 91) || ((currentChar >96) && (currentChar<123)) ) {
					//the character is valid. continue.
				} 
				else {
					invalid = s;
					return false;
				}
			}
			
		}
		return true;
	}
	
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
	 * This method removes the need to implement String.getValue(char[] array) 
	 * @param array The array of chars to convert into a string
	 * @return the char array as a string.
	 */
	public  String buildStrFromCharArray(char[] array) {
		String returnString = "";
		for(int i = 0; i<array.length; i++) {
			returnString = returnString + array[i];
		}
		return returnString;
	}
	
	/**
	 * normalises the returnArray to make the first character of each name upper case (to make sure comparisons work properly).
	 */
	private  void normalize() {
		System.out.println("Converting list to uppercase letters only.");
		for(int r = 0; r < returnList.length; r++) {
			char[] current = returnList[r].toCharArray();
			for(int i = 0; i < returnList[r].length(); i++) {
				char currentChar = returnList[r].charAt(i);
				
				current[i] = (char) ((isCapital(currentChar))?(currentChar):(currentChar-49));
			}
			returnList[r] = buildStrFromCharArray(current);
		}
	}
	
	/**
	 * Sorts the return array.
	 * @param names the string array of names to be sorted.
	 * @return returns true if array is sorted successfully.
	 */
	private  boolean sortReturnList() {
		if( !checkValid() ) {
			System.out.println("Invalid input on token " + invalid);
			return false;
		}
		normalize();
		System.out.println("List normalized. Continuing.");
		
		
		
		while(true) {		//this should repeat until the array is sorted. This is implemented by returning if an entire pass
							//is made without swapping any names.
			swapOccurred = false;
			while(currentIndex < returnList.length - 1) {
				try {
					//do this over and over
					currentIndex += compare(0);		//this will always increment currentIndex by 1, but will also swap values at greater than 0 charindex
				} catch (ArrayIndexOutOfBoundsException e) {	//when this is thrown, we have been through the entire array.
					e.printStackTrace();
				}
			}
			if(!swapOccurred) {
				return true;
			}
			currentIndex = 0;			//go again.
		}
	}
	
	/**
	 * 
	 * @param i the index of the first value being compared
	 * @param charPointer the character which is being compared (normally 0 unless the two values have the same first char)
	 * @param name1 the first name being compared
	 * @param name2 the second name being compared
	 * @return returns an integer which makes the correct adjustment to the index position when += is performed.
	 */
	private  int compare(int charPointer) {
		int x = returnList[currentIndex].length();
		int y = returnList[currentIndex+1].length();
		
		if(charPointer>=x) {
			//if the charPointer is greater than x, the two strings are identical up to x but x is shorter so comes first.
			return 1;
		}
		if(charPointer>=y) {
			//if charPointer is greater than y, the values need to be swapped.
			swapOccurred = true;
			swap();
			return 1;
		}
		
		char m = returnList[currentIndex].charAt(charPointer);
		char n = returnList[currentIndex+1].charAt(charPointer);
		
		if( m > n ) {
			swapOccurred = true;
			swap();
			return 1;	
		}
		else if ( m == n ) {
			return compare(++charPointer);
		}
		else {
			return 1;
		}
	}
	
	/**
	 * swaps the values in the returnList at the given index points
	 * @param x the index of the first value to be swapped
	 * @param y the index of the second value to be swapped
	 */
	private  void swap() {
		String temp = returnList[currentIndex];
		returnList[currentIndex] = returnList[currentIndex+1];
		returnList[currentIndex+1] = temp;
		swapOccurred = true;
	}
	
	/**
	 * Imports a list of names as either a CSV file or a CSV and quoted (e.g. "nameOne","nameTwo") file.
	 * @param filename The filename to be imported.
	 */
	public  void importNames(String filename) {
		Vector<String> names = new Vector<String>();
		try {	
			System.out.println("Looking for file "+filename);
			FileReader fr = new FileReader(new File(filename));
			char c;
			String name = "";
			
			while((c=(char)fr.read())!= 65535) {
				if(c=='"'||c=='\n'||c=='\r'||c=='\t') {
				}
				else if (c==',') {
					names.add(name);
					name = "";
				}
				else {
					name = name + c;
				}
			}
			
			names.add(name); 	//add the last name not marked by a comma
			fr.close();
			
			returnList = new String[names.size()];
			for(int i = 0; i<names.size();i++) {
				returnList[i] = names.get(i);
			}
			names.clear();
			
		} catch (FileNotFoundException e1) {
			System.out.println("File not found. exiting.");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} 
	}
	
	/**
	 * This method prints the array in its current state.
	 */
	public  void printArray() {
		System.out.print("list: ");
		for(int i = 0; i<returnList.length; i++) {
			System.out.print(returnList[i] + ", ");
		}
		System.out.println();
	}
	
	/**
	 * This method creates a results file and prints the sorted array into it. 
	 * @param resultName the name of the results file (currently only results.txt)
	 */
	public  void returnResults(String resultName) {
		File file = new File(resultName);
		try {
		if (!file.exists()) {
            file.createNewFile();
        }
		PrintWriter pw = new PrintWriter(resultName, "UTF-8");
		for(int i = 0; i<returnList.length-1; i++) {
			pw.print("\""+returnList[i] + "\",");		//print in the format the names.txt file was given to us in.
		}
		pw.print("\""+returnList[returnList.length-1]+"\""); 		//don't print a comma after the last value.
		pw.println();
		pw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * driver function
	 * @param args
	 */
	public static void main(String[] args) {
		
		CW2Q1 sorter = new CW2Q1();
		try {
			sorter.importNames(args[0]);
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please provide a filename as an argument.");
			System.exit(1);
		}
		sorter.printArray();
		
		System.out.println("Sorting list.");
		if(sorter.sortReturnList()) {
			System.out.println("Sorted list.");
		}
		sorter.printArray();
		sorter.returnResults("results.txt");
	}
}