
public class Node {
	
	String data;
	Integer xorLink;
	
	public Node(String data) {
		this.data = data;
		this.xorLink = null;
	}
	
	public Node(String data, int xorLink) {
		this.data = data;
		this.xorLink = xorLink;
	}
	
	public Integer getXorLink() {
		return this.xorLink;
	}
	
	public String getData() {
		return this.data;
	}
	
	public void setXorLink(int addr) {
		this.xorLink = addr;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
