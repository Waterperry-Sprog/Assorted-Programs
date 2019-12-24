
public class MockMemory {

	Node[] mockMemory;
	
	
	public MockMemory(int len){
		mockMemory = new Node[len];
	}
	
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
	
	public void setElementAt(int position, Node n) {
		mockMemory[position] = n;
	}
	
	public Node getElementAt(int position) {
		return mockMemory[position];
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
	
	public int getLength() {
		return mockMemory.length;
	}
	
	/**
	 * This method prints all assignments of nodes to memory addresses.
	 */
	public void showAllMemAssignments() {
		try {
			for(int i = 0; i< mockMemory.length;i++) {
				if(!mockMemory[i].getData().contentEquals("")) {
					System.out.println(i+": "+mockMemory[i].getData());
				}
			}
		} catch(NullPointerException e) {
			
		}
	}
	
}
