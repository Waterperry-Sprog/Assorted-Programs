
public class Vector<E> extends java.util.Vector<E>{
	
	/**
	 * Apparently this stops compilation warnings.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method returns the size of a vector by catching an exception in trying to get elements.
	 * @return the size as an integer.
	 */
	@Override
	public int size() {
		int size = 0;
		try {
			for(int i = 0; i<Integer.MAX_VALUE; i++) {
				get(i);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return size;
		}
		return -1;
	}
}
