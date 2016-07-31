import java.util.Comparator;


public class Node implements Comparable<Node>,Comparator<Node> {

	private String name;
	private int path_cost;
	
	
	public Node() {

	}

	public Node(String name, int path_cost) {
		this.name = name;
		this.path_cost = path_cost;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCost() {
		return path_cost;
	}
	public void setCost(int cost) {
		this.path_cost = cost;
	}
	@Override
	public String toString() {
		return "Node [name=" + name + ", cost=" + path_cost + "]";
	}
	
	
	@Override
	public int compareTo(Node o) {

		return this.getName().compareToIgnoreCase(o.getName());
	}
	@Override
	public int compare(Node o1, Node o2) {

			//return o1.getCost()-o2.getCost();
		if(o1.getCost()<o2.getCost())
		{
			//System.out.println(o1.getCost()+" is less");
			return -1;
		}
			
		else if (o1.getCost()>o2.getCost())
		{
			//System.out.println(o2.getCost()+" is less");
			return 1;
		}
			
		else
		{
			//System.out.println("Both are equal : " +o1.getName().compareToIgnoreCase(o2.getName()));
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
		
	}
	

}
