import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class CW2Q4 {
	
	 Vector<String> names = new Vector<String>();
	 int progress = 0;
	 MockMemory mem = new MockMemory(50000);
	 Node head;
	 Node tail;
	
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
				temp = mem.getElementAt(xor(temp_old.getXorLink(), temp.getXorLink()));
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
		
		for(int i = 0; i<mem.getLength(); i++) {
			mem.setElementAt(i, null);
		}
		
		int currAddr = mem.findNextSpace();
		mem.setElementAt(currAddr, new Node(""));
		int nextAddr = mem.findNextSpace();
		mem.setElementAt(currAddr, null);
		
		curr.setData(names.get(progress++));
		curr.setXorLink(xor(null, nextAddr));
		mem.setElementAt(currAddr, new Node(curr.getData(),curr.getXorLink()));		//put curr in memory at the next available space
		head = mem.getElementAt(currAddr);
		tail = mem.getElementAt(currAddr);
		prev = mem.getElementAt(currAddr);
		currAddr = mem.findNextSpace();
		mem.setElementAt(currAddr, new Node(""));
		nextAddr = mem.findNextSpace();
		mem.setElementAt(currAddr, null);
		
		while(progress<names.size()) {
			curr.setData(names.get(progress++));
			curr.setXorLink( xor(prev.getXorLink(), nextAddr) );	//so now curr is established fully.
			mem.setElementAt(currAddr, new Node(curr.getData(),curr.getXorLink()));		//put curr in memory at the next available space
			prev = mem.getElementAt(currAddr);
			tail = prev;
			currAddr = mem.findNextSpace();
			mem.setElementAt(currAddr, new Node(""));
			nextAddr = mem.findNextSpace();
			mem.setElementAt(currAddr, null);
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
			temp = mem.getElementAt(xor(temp_old.getXorLink(), temp.getXorLink()));
			temp_old = changeover;
		}
		} catch (NullPointerException e) {
			return;
		}
	}

	public Node getNextNode(Node n)  {
		Node temp = n;
		int addr = mem.getMemAddrOf(n);
		return (mem.getElementAt( xor(addr, temp.getXorLink()) ));
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
		
		int forNewEl = mem.findNextSpace();
		int nextNewXor = xor(forNewEl, ( xor(next.getXorLink(), mem.getMemAddrOf(curr))) );
		int currNewXor = xor(forNewEl, ( xor(mem.getMemAddrOf(next), prev.getXorLink()) ));
		int newNewXor = xor(mem.getMemAddrOf(curr), mem.getMemAddrOf(next));
		
		curr.setXorLink(currNewXor);
		next.setXorLink(nextNewXor);
		mem.setElementAt( forNewEl, new Node(newObj, newNewXor));
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
			
			mem = new MockMemory(2 * dummy.size());
			
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
//		list.mem.showAllMemAssignments();
		
//		list.printList();
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
