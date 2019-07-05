import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class CW2Q4 {
	
	 Vector<String> names = new Vector<String>();
	 int progress = 0;
	 Node[] mockMemory;
	 Node head;
	 Node tail;
	
	 /**
	  * This method searches memory for an empty space to put a node.
	  * @return the memory address of the empty space.
	  */
	public  int findNextSpace() {
		for(int i = 0; i<mockMemory.length;i++) {
			if(mockMemory[i]==null) {		//the list is laid out sequentially in memory, but the code doesn't rely on this
				return i;							//being the case
			}
		}
		System.out.println("List full.");
		return -1;
	}
	
	/**
	 * This method searches memory to find a given node.
	 * @param n The node to search for
	 * @return the address in memory of the node.
	 */
	public int getMemAddrOf(Node n) {
		for(int i = 0; i<mockMemory.length; i++) {
			if(n.equals(mockMemory[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public  String getHeadName() {
		return head.getData();
	}
	
	public  String getTailName() {
		return tail.getData();
	}
	
	
	/**
	 * Make the temp node seek through the array until it finds the input name.
	 * @param name The name to search for.
	 * @return null if the name is not found, otherwise it will return the Node.
	 */
	public Node seek(String name) {
		try {
			Node temp = head;
			Node temp_old = new Node("");
			while(true) {
				if(strcmp(temp.getData(), name)){
					return temp;				//if we find a match, return temp
				}
				//otherwise, move on until we reach the end of the list
				Node changeover = temp;						//variable to facilitate changeover
				temp = mockMemory[xor(temp_old.getXorLink(), temp.getXorLink())];
				temp_old = changeover;
			}
		} catch(NullPointerException n) {
			System.out.println("Name not found.");
			return new Node("");
		}
	}
	
	/**
	 * This method copies all names from the input file into the mock memory array as nodes, and establishes the 
	 * XOR links between nodes.
	 */
	public void instantiateNamesInMemory() {
		Node prev = new Node("");
		Node curr = new Node("");
		
		for(int i = 0; i<mockMemory.length; i++) {
			mockMemory[i] = null;
		}
		
		int currAddr = findNextSpace();
		mockMemory[currAddr] = new Node("");
		int nextAddr = findNextSpace();
		mockMemory[currAddr] = null;
		
		curr.setData(names.get(progress++));
		curr.setXorLink(xor(null, nextAddr));
		mockMemory[currAddr] = new Node(curr.getData(),curr.getXorLink());		//put curr in memory at the next available space
		head = mockMemory[currAddr];
		tail = mockMemory[currAddr];
		prev = mockMemory[currAddr];
		currAddr = findNextSpace();
		mockMemory[currAddr] = new Node("");
		nextAddr = findNextSpace();
		mockMemory[currAddr] = null;
		
		while(progress<names.size()) {
			curr.setData(names.get(progress++));
			curr.setXorLink( xor(prev.getXorLink(), nextAddr) );	//so now curr is established fully.
			mockMemory[currAddr] = new Node(curr.getData(),curr.getXorLink());		//put curr in memory at the next available space
			prev = mockMemory[currAddr];
			tail = prev;
			currAddr = findNextSpace();
			mockMemory[currAddr] = new Node("");
			nextAddr = findNextSpace();
			mockMemory[currAddr] = null;
		}
	}

	/**
	 * This method prints all assignments of nodes to memory addresses.
	 */
	public void showAllMemAssignments() {
		for(int i = 0; i<mockMemory.length;i++) {
			if(mockMemory[i].getData()!="") {
				System.out.println(i+": "+mockMemory[i].getData());
			}
		}
	}
	
	public Integer xor(Integer x, Integer y) {
		if(x==null) return y;
		if(y==null) return x;
		return x^y;
	}
	
	public void printList() {
		try {
		Node temp = head;
		Node temp_old = new Node("");
		while((xor(temp.getXorLink(),temp_old.getXorLink()))!=null) {
			Node changeover = temp;						//variable to facilitate changeover
			temp = mockMemory[xor(temp_old.getXorLink(), temp.getXorLink())];
			temp_old = changeover;
		}
		} catch (NullPointerException e) {
			return;
		}
	}

	public Node getNextNode(Node n)  {
		Node temp = n;
		int addr = getMemAddrOf(n);
		return (mockMemory[ xor(addr, temp.getXorLink()) ]);
	}
	
	public Node getPrevNode(Node n) {
		Node temp = head;
		while(true) {
			Node next = getNextNode(temp);
			if(strcmp(next.getData(), n.getData())) {
				return temp;
			}
			else {
				temp = next;
			}
		}
	}
	
	public void insertAfter ( String after , String newObj ) {
		Node temp = seek(after);
		Node temp_old;
		try {
			temp_old = getPrevNode(temp);
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Name not found! Cannot add new name!");
			return;
		}
		Node prev = temp_old;
		Node curr = temp;
		Node next = getNextNode(temp);
		
		temp = seek(after);
		
		
		
		
		
		int forNewEl = findNextSpace();
		int nextNewXor = xor(forNewEl, ( xor(next.getXorLink(), getMemAddrOf(curr))) );
		int currNewXor = xor(forNewEl, ( xor(getMemAddrOf(next), prev.getXorLink()) ));
		int newNewXor = xor(getMemAddrOf(curr), getMemAddrOf(next));
		
		curr.setXorLink(currNewXor);
		next.setXorLink(nextNewXor);
		mockMemory[forNewEl] = new Node(newObj, newNewXor);
	}
	
	public void insertBefore ( String before , String newObj ) {
		Node n = seek(before);
		Node beforeNode = getPrevNode(n);
		insertAfter(beforeNode.getData(), newObj);
		
	}
	
	/**
	 * This method compares two strings to see if they are equal.
	 * @param x String one.
	 * @param y String two.
	 * @return true if the strings are equal, false otherwise.
	 */
	public boolean strcmp(String x, String y) {
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
	 * Imports a list of names as either a CSV file or a CSV and quoted (e.g. "nameOne","nameTwo") file.
	 * @param filename The filename to be imported.
	 */
	public void importNames(String filename) {
		try {	
			Vector<String> dummy = new Vector<String>();
			System.out.println("Looking for file "+filename);
			FileReader fr = new FileReader(new File(filename));
			char c;
			String name = "";
			while( (int)(c= (char) fr.read()) != 65535) {		//sort of reimplementing bufferedreader and tokenizer
				if(c=='"') {
				}
				else if (c==',') {
					dummy.add(name);
					name = "";
				}
				else {
					name = name + c;
				}
			}
			
			mockMemory = new Node[2*dummy.size()];
			
			names.add(name);			//add the last name not marked by a comma
			names.add("");		//add this to make it easier to instantiate list
			fr.close();
			names.clear();
			
			for(String s:dummy) {
				names.add(s);
			}
			
			System.out.println("Names imported.");
			
		} catch (FileNotFoundException e1) {
			System.out.println("File not found. exiting.");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} 
	}
	
	public static void main(String[] args) {
		if(args.length==0) {
			System.out.println("Usage: java CW2Q4 <filename>");
			System.exit(0);
		}
		
		CW2Q4 list = new CW2Q4();
		list.importNames(args[0]);
		list.instantiateNamesInMemory();
		System.out.println("List instantiated.");
		
		list.printList();
		//list.insertAfter("LINDA", "insertAfterIsWorking!");
		//list.printList();
		
		//System.out.println((list.getNextNode(list.seek("MARY"))).getData());
		//System.out.println((list.getPrevNode(list.seek("MARIA"))).getData());
		
		//System.out.println(list.getNextNode(list.seek("LINDA")).getData());
		//System.out.println(list.getNextNode(list.seek("insertAfterIsWorking")).getData());
		
		//list.insertBefore("LINDA", "insertBeforeIsWorking!");
		//System.out.println(list.getPrevNode(list.seek("LINDA")).getData());
	}
	
	
}
