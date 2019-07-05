import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CW2Q8{
	
	/**
	 * converts a string to an int based on the chars in the string. 
	 * CANNOT detect when it is passed an invalid string (does not throw exceptions)
	 * @param input the (sanitized) input string
	 * @return the int that is represented by the input string
	 */
	public int toInt(String input) {
		int returnValue = 0;
		for(int i = 0; i < input.length(); i++){
			returnValue *= 10;
			returnValue += ( input.charAt(i) - '0' );
		}
		
		return returnValue;
	}
	
	/**
	 * Calculates the bottom right value of the square.
	 * @param x the length n of one side of the square
	 * @return the value stored in the bottom right cell of the n x n square.
	 */
	 long bottomRight(long x){
	        if(x==1) return 1;
	        return bottomRight(x-2) + 4*x -10;
	}
	
	/**
	 * This calculates the bottom left value of an n x n square based on the bottom right value. 
	 * @param x The size of one side of the square.
	 * @return The value stored in the bottom left cell of the square.
	 */
	 long bottomLeft(long x){
	        return bottomRight(x) + x - 1;
	}
	
	/**
	 * This calculates the top left value of an n x n square based on the bottom right value.
	 * @param x The size of one side of the square.
	 * @return The value stored in the top left cell of the square.
	 */
	 long topLeft(long x){
	        return bottomRight(x) + 2*x - 2;
	}
	
	/**
	 * This calculates the top right value of an n x n square based on the bottom right value.
	 * @param x The size of one side of the square.
	 * @return The value stored in the top right cell of the square.
	 */
	 long topRight(long x){
	        return bottomRight(x) + 3*x - 3;
	}
	
	 long sumBtmRt(long x){
	        long sum = 0;
	        long curr = x;
	        while (curr!=1){
	                sum += bottomRight(curr);
	                curr-=2;
	        }
	        return sum;
	}
	
	 long sumBtmLft(long x){
	        long sum = 0;
	        long curr = x;
	        while (curr!=1){
	                sum += bottomLeft(curr);
	                curr-=2;
	        }
	        return sum;
	}
	
	 long sumTopLft(long x){
	        long sum = 0;
	        long curr = x;
	        while (curr!=1){
	                sum += topLeft(curr);
	                curr-=2;
	        }
	        return sum;
	}
	
	 long sumTopRt(long x){
	        long sum = 0;
	        long curr = x;
	        while (curr!=1){
	                sum += topRight(curr);
	                curr-=2;
	        }
	        return sum;
	}
	
	 long sumDiag(int x){
	        long sum = sumBtmRt(x) + sumBtmLft(x) + sumTopLft(x) + sumTopRt(x);
	        return sum + 1;
	}
	
	public static void main(String[] args) {
		
		CW2Q8 spiralCalc = new CW2Q8();
		
		System.out.print("Demo for 1001x1001. Value is > ");
		System.out.println(spiralCalc.sumDiag(1001));
		System.out.print("Enter a number >");
		String num = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			num = reader.readLine();
		} catch (IOException e) {
			System.exit(1);
		}
		try {
			long input = spiralCalc.toInt(num);
			if((2*(input/2))==input){				//works because longeger division rounds down. Dividing an even number by two yields no change: dividing an odd 
				System.out.println("Sorry, I can only work with odd numbers. Exiting.");		//number by two will reduce its value by 0.5 
			}
			else{
				System.out.println(spiralCalc.sumDiag(spiralCalc.toInt(num)));
			}
		}catch(StackOverflowError so) {
			System.out.println("Sorry, that number is too big.");
		}
	}
}