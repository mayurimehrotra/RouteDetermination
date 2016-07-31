
public class Edge implements Comparable<Edge> {
	
	Node parent;
	Node child;
	int length;
	int num_of_off_time;
	int[] timeArray=new int[24];
	
	public int[] getTimeArray() {
		return timeArray;
	}
	public void setTimeArray(int[] timeArray) {
		this.timeArray = timeArray;
	}
	public int getNum_of_off_time() {
		return num_of_off_time;
	}
	public void setNum_of_off_time(int num_of_off_time) {
		this.num_of_off_time = num_of_off_time;
	}
	public Edge(Node parent, Node child, int length) {
		//super();
		this.parent = parent;
		this.child = child;
		this.length = length;
	}
	public Edge() {
		this.length = 0;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public Node getChild() {
		return child;
	}
	public void setChild(Node child) {
		this.child = child;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	@Override
	public String toString() {
		return "Edge [parent=" + parent + ", child=" + child + ", length="
				+ length + "]";
	}
	@Override
	public int compareTo(Edge o) {
		//System.out.println("lengths : "  + this.getLength() + " " + o.getLength());
		return (this.getLength()-o.getLength());
	}
	
}
