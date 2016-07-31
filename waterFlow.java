import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class waterFlow {
	public static void main(String[] args) {
		
		String line=null;
		int off_times = 0;
		int numberOfTestCases;
		String temp=null,startNode=null,endNode=null,source=null;
		int i,numberOfPipes,startTime,lengthOfPipe;
		Map<String, ArrayList<Node>> parentChildMap = new HashMap<String, ArrayList<Node>>();
		FileReader fileReader;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter;
		BufferedWriter bufferedWriter=null;
		
		String fileName = null;
		if(args[0].equals("-i"))
			fileName = args[1];
		
		try {
			
			fileReader=new FileReader(fileName);
			bufferedReader=new BufferedReader(fileReader);
			fileWriter = new FileWriter("output.txt");
			bufferedWriter=new BufferedWriter(fileWriter);
						
			line=bufferedReader.readLine();
			numberOfTestCases=Integer.parseInt(line);
			
			while(numberOfTestCases-- >0)
			{
				line=null;
				off_times = 0;
				temp=null;
				startNode=null;
				endNode=null;
				source=null;
				i=0; numberOfPipes=0 ; startTime=0; lengthOfPipe=0;
				parentChildMap = new HashMap<String, ArrayList<Node>>();
				
				String algo=bufferedReader.readLine();
				
				source=bufferedReader.readLine();
							
				Node src = new Node(source,0);						//source node
	
				temp=bufferedReader.readLine();
				String[] destinations=temp.split(" ");
				int dest_len = destinations.length;
				
				Node [] dest= new Node[dest_len];					//destination nodes array
				for(i=0;i<dest_len;i++){
					Node temp_node = new Node(destinations[i],0);
					dest[i] = temp_node;					
				}
							
				temp=bufferedReader.readLine();
				
				String[] middleNodes=temp.split(" ");						//middle nodes array
				int middle_len = middleNodes.length;
				Node [] middle_nodes= new Node[middle_len];
				for(i=0;i<middle_len;i++){
					Node temp_node = new Node(middleNodes[i],0);
					middle_nodes[i] = temp_node;					
				}
				
				numberOfPipes=Integer.parseInt(bufferedReader.readLine());				
				
				Edge [] edges = new Edge[numberOfPipes];
				for(i=0;i<numberOfPipes;i++)
				{
					temp=bufferedReader.readLine();
					String[] pipeDetails=temp.split(" ");
					startNode= pipeDetails[0];
					endNode=pipeDetails[1];
					lengthOfPipe=Integer.parseInt(pipeDetails[2]);
					
					Node parent = new Node(startNode,0);
					Node child = new Node(endNode,0);
					
					Edge e = new Edge(parent, child, lengthOfPipe);
					if(algo.equalsIgnoreCase("UCS"))
					{
						off_times=Integer.parseInt(pipeDetails[3]);
						
						e.setNum_of_off_time(off_times);
						int[] timeArray=new int[24];
						
						for(int x=0;x<off_times;x++)			//setting the array values with off times
						{
							String time_periods=pipeDetails[4+x];			//each time period
							
							String[] time_slots=time_periods.split("-");		//each slot 2-4
						
							int start_index=Integer.parseInt(time_slots[0]) ;
							
							int end_index=Integer.parseInt(time_slots[1]);
							
							for(int j=start_index;j<=end_index;j++)			//set the individual time to 1
							{
								if(timeArray[j%24]!=1)
									timeArray[j%24]=1;						
							}
								
						}
						e.setTimeArray(timeArray);						
					}				
					
					edges[i] = e;					
				}
				
				for(Edge e:edges)
				{					
					if(parentChildMap.containsKey(e.getParent().getName()))
					{
						ArrayList<Node> children= parentChildMap.get(e.getParent().getName());
						children.add(e.getChild());
					}
					else
					{
						ArrayList<Node> child=new ArrayList<Node>();
						child.add(e.getChild());
						parentChildMap.put(e.getParent().getName(), child);
					}
				}
					
				
				startTime=Integer.parseInt(bufferedReader.readLine());				
				
				Node sol=null;
				if(algo.equalsIgnoreCase("BFS"))
				{
					sol=BFS(parentChildMap,src,dest,middle_nodes);
					if(sol!=null)
						sol.setCost(sol.getCost()+startTime);
				}
				else if(algo.equalsIgnoreCase("DFS"))	
				{
					sol=DFS(parentChildMap,src,dest,middle_nodes);
					if(sol!=null)
						sol.setCost(sol.getCost()+startTime);
				}					
				else if(algo.equalsIgnoreCase("UCS"))
				{
					sol=UCS(parentChildMap,src,dest,middle_nodes,edges,startTime);
					if(sol!=null)
					{						
						//System.out.println(sol.getName() + " " +sol.getCost());
					}					
				}
					
				
				if(sol==null)		
					bufferedWriter.append("None");
				else
					bufferedWriter.append(sol.getName() + " " + (sol.getCost()%24));
				
				bufferedReader.readLine();
				bufferedWriter.newLine();
									
			}		//end while number of test cases
		} catch (FileNotFoundException e) {
			e.printStackTrace();			
		} catch (IOException e) {			
			e.printStackTrace();			
		}finally{
			try {
				bufferedReader.close();
				bufferedWriter.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		}
		
	}

	private static Node UCS(Map<String, ArrayList<Node>> parentChildMap,
			Node src, Node[] dest, Node[] middle_nodes, Edge[] edges, int startTime) {

		ArrayList<Node> openQueue= new ArrayList<Node>();
		ArrayList<Node> exploredQueue=new ArrayList<Node>();
		
		Node solution=new Node("solution", 0);
		
		openQueue.add(src);
		exploredQueue.add(src);
		
			while(!openQueue.isEmpty())
			{
				if (isGoal(openQueue.get(0).getName(),dest)) 				// check if first node is goal before processing it
				{			
					solution=openQueue.get(0);
					return solution;
				}
				
				
				if(parentChildMap.containsKey(openQueue.get(0).getName()))		// if it is a parent 
				{
					Node parent=openQueue.get(0);
					openQueue.remove(0);
					if( !exploredQueue.contains((Object)parent))				// if parent not present in explored queue add it
						exploredQueue.add(parent);				
										
					ArrayList<Edge> child_edge=new ArrayList<Edge>();
					for(Edge e:edges)
					{
						if(e.getParent().getName().equalsIgnoreCase(parent.getName()))  		//edge having that parent
						{
							child_edge.add(e);
						}
					}
					
					Collections.sort(child_edge);								// sort edges based on length
					
					for(int i=0;i<child_edge.size();i++)			// for all children
					{				
						if (!(child_edge.get(i).getParent().getName().equalsIgnoreCase(src.getName())))			//if edge's parent is source
							startTime=child_edge.get(i).getParent().getCost();								//startTime = 0
			
						if( ( isValid(startTime,child_edge.get(i).getTimeArray()) ))
						{
							Node child=child_edge.get(i).getChild();				//get the child of each edge of the sorted edge list
							
							int oldCost=child_edge.get(i).getChild().getCost() ;
							int newCostofNode= (startTime + child_edge.get(i).getLength()) ;  
							if(newCostofNode!=0)			
							{
								for(int x=0;x<openQueue.size();x++)
								{
									if(openQueue.get(x).getName().equalsIgnoreCase(child.getName()))
									{
										if( (newCostofNode) < (oldCost) )
										{									
											openQueue.remove(x);
										}	
										else
											newCostofNode=oldCost;
									}									
								}
								
								int flag2=0;
								//if child is present in explored queue do not update cost otherwise update
								for(int x=0;x<exploredQueue.size();x++)
								{
									if(exploredQueue.get(x).getName().equalsIgnoreCase(child.getName()))
									{
										flag2=1;
									}									
								}

								if(flag2==0)
								{									
									child.setCost( newCostofNode  );			//update cost of the child
									update(child, ( newCostofNode ) ,edges);

								}								
							}							
							
							int flag=0;							
							for(int n=0;n<exploredQueue.size();n++)
							{
								if(exploredQueue.get(n).getName().equalsIgnoreCase(child.getName()))
								{
									 flag=1;
									 break;
								}
							}
							
							if(flag==0)
							{
								openQueue.add(child);									//add to open Queue
							}
														
							//sort the queue based on cost of node
							Collections.sort(openQueue,new Node());							
							
						}					//end if valid
													
					} 					// end for loop for the number of children
									
				}						//if it is a parent closing
				
				else															// if not a parent					
				{	
							exploredQueue.add(openQueue.remove(0));		
				}
				
			}	//end while		
		return null;
	}

	
	
	private static void update(Node child, int newCostofNode, Edge[] edges) {

		for(Edge e:edges)
		{
			if(e.getChild().getName().equalsIgnoreCase(child.getName()))
			{
				e.getChild().setCost(newCostofNode);
			}
			
			if(e.getParent().getName().equalsIgnoreCase(child.getName()))
			{
				e.getParent().setCost(newCostofNode);
			}
			
		}
		
	}

	private static boolean isValid(int startTime, int[] timeArray) {

		startTime = startTime%24;
		if(timeArray[startTime]==1)			//time is set
				return false;					// hence not valid
			return true;
			
	
	}

	private static Node BFS(Map<String, ArrayList<Node>> parentChildMap,
			Node src, Node[] dest, Node[] middle_nodes) {
		
		ArrayList<Node> openQueue= new ArrayList<Node>();
		ArrayList<Node> exploredQueue=new ArrayList<Node>();
		
		Node solution=new Node("solution", 0);
		
		openQueue.add(src);
		exploredQueue.add(src);
		
			while(!openQueue.isEmpty())
			{				
				if (isGoal(openQueue.get(0).getName(),dest)) 				// check if first node is goal before processing it
				{			
					solution=openQueue.get(0);
					return solution;
				}
												
				if(parentChildMap.containsKey(openQueue.get(0).getName()))		// if it is a parent 
				{
					if( !exploredQueue.contains((Object)openQueue.get(0)))		// if parent not present in explored queue add it
						exploredQueue.add(openQueue.get(0));				
					
					//else if it is not goal
					List<Node> children=new ArrayList<Node>();					
					children=parentChildMap.get(openQueue.get(0).getName());	//get children of the parent	
					Collections.sort(children);									//sort the children
			
					Node temp = openQueue.remove(0); 							// remove parent from openQueue
					
					for(int i=0;i<children.size();i++)							//add children to open queue with updated cost
					{
						if ( !(openQueue.contains((Object)children.get(i)) )  ) 
						{
							children.get(i).setCost(temp.getCost()+1);
							openQueue.add(children.get(i));				
						}
					}
															
				}
				else															// if not a parent					
						exploredQueue.add(openQueue.remove(0));
	
			}
		return null;
	}
	
	

	private static Node DFS(Map<String, ArrayList<Node>> parentChildMap,
		Node src, Node[] dest, Node[] middle_nodes) {
		
		ArrayList<Node> openQueue= new ArrayList<Node>();
		ArrayList<Node> exploredQueue=new ArrayList<Node>();
		
		Node solution=new Node("solution", 0);
		
		openQueue.add(src);
		exploredQueue.add(src);

		
			while(!openQueue.isEmpty())
			{				
				if (isGoal(openQueue.get(0).getName(),dest)) 				// check if first node is goal before processing it
				{			
					solution=openQueue.get(0);
					return solution;
				}
				
								
				if(parentChildMap.containsKey(openQueue.get(0).getName()))		// if it is a parent 
				{
					if( !exploredQueue.contains((Object)openQueue.get(0)))		// if parent not present in explored queue add it
						exploredQueue.add(openQueue.get(0));				
					
					//else if it is not goal
					List<Node> children=new ArrayList<Node>();					
					children=parentChildMap.get(openQueue.get(0).getName());	//get children of the parent	
					Collections.sort(children);									//sort the children
			
					Node temp = openQueue.remove(0); 	// remove parent from openQueue
					
					for(int i=children.size()-1;i>=0;i--)							//add children to open queue with updated cost
					{
						if ( !(openQueue.contains((Object)children.get(i)) )  ) 
						{
							children.get(i).setCost(temp.getCost()+1);
							openQueue.add(0, children.get(i));
				
						}
					}
				}
				else															// if not a parent
				{					
					exploredQueue.add(openQueue.remove(0));
				}
				
			}
		
		return null;
	}	
		
	
	
	private static boolean isGoal(String destination,Node[] dest) {

		for (Node n : dest) {
			if(n.getName().equalsIgnoreCase(destination))
				return true;
		}
		
		return false;
		}

}
