package ex1.src;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;

/**
 * class for the algorithms of weighted graph
 * @author Israela
 *
 */
public class WGraph_Algo implements weighted_graph_algorithms{

	/**
	 * the graph
	 */
	private weighted_graph myGraph =null;
	/**
	 * the distances
	 */
	private HashMap<node_info, Double> distances = new HashMap<node_info, Double>();

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g the graph
     */
	@Override
	public void init(weighted_graph g) {
		myGraph = g;
	}

    /**
     * Return the underlying graph of which this class works.
     * @return the graph
     */
	@Override
	public weighted_graph getGraph() {
		return myGraph;
	}

    /**
     * Compute a deep copy of this weighted graph.
     * @return the copy of the graph
     */
	@Override
	public weighted_graph copy() {
		WGraph_DS graphTo = new  WGraph_DS();
		List<node_info> oldNodes = new ArrayList<node_info>( myGraph.getV());
		Iterator<node_info> iterator = oldNodes.iterator();

		//create all nodes and add to the graph 
		while(iterator.hasNext()){
			node_info from = iterator.next();
			graphTo.addNode(from.getKey());
			node_info to = graphTo.getNode(from.getKey());
			to.setInfo(from.getInfo());
			to.setTag(from.getTag());
		}

		//for each node, add friends
		iterator = oldNodes.iterator();
		while(iterator.hasNext())
		{
			node_info currentNode = iterator.next();
			List<node_info>	adjecents=new ArrayList<node_info>(myGraph.getV(currentNode.getKey()));
			Iterator<node_info> iteratorAdjecents = adjecents.iterator();
			while(iteratorAdjecents.hasNext()) {
				node_info next=iteratorAdjecents.next();
				graphTo.connect(currentNode.getKey(), next.getKey(),myGraph.getEdge(currentNode.getKey(), next.getKey()));
			}
		}
		graphTo.setNumberOfEdges(((WGraph_DS)myGraph).getNumberOfEdges());
		graphTo.setMC(myGraph.getMC());
		return graphTo;
	}

	/**
	 * set the distance for a specific node
	 * @param node the node
	 * @param distance the distance
	 */
	private void setDistance(node_info node, double distance) {
		distances.put(node, distance);
	}
	/**
	 * get the distance of a specific node
	 * @param node the node
	 * @return the distance of a specific node
	 */
	private double getDistance(node_info node) {
		return distances.get(node);
	}

	/**
	 * activate the Dijkstra algorithm
	 * @param graph the graph
	 * @param source the source node
	 */
	private void Dijkstra(weighted_graph graph, node_info source) {
		Collection<node_info> nodeInfoArrayList = graph.getV();
		// compare by lambda function
		Comparator<node_info> nodeSorter=Comparator.comparing((node_info node) -> getDistance(node));
		PriorityQueue<node_info> nodeInfoPriorityOueue=new PriorityQueue<>(nodeSorter);
		
		//init all array and insert to queue
		for (node_info node : nodeInfoArrayList) {
			setDistance(node, Integer.MAX_VALUE);//set max value on all nodes 
			node.setTag(-1);
			node.setInfo("w");
			nodeInfoPriorityOueue.add(node);//add all nodes to queue
		  }
		
		//set source distance to 0
		setDistance(source, 0);
		//for every node in queue ,pull him out,get all adjecents wasn't visited and decide minimum distance from source
		while(nodeInfoPriorityOueue.size()>0) {
			
			node_info polledNode=nodeInfoPriorityOueue.poll();
			ArrayList<node_info>adjecents=new ArrayList<node_info>(graph.getV(polledNode.getKey()));
			for (int i = 0; i < adjecents.size(); i++) {
				node_info adj=adjecents.get(i);
				if(adj.getInfo().equals("w")) {
				
					double totalDistance=getDistance(polledNode)+myGraph.getEdge(polledNode.getKey(), adj.getKey());
					if(totalDistance < getDistance(adj)) {
						nodeInfoPriorityOueue.remove(adj);
						setDistance(adj, totalDistance);
						adj.setTag(polledNode.getKey());
						nodeInfoPriorityOueue.add(adj);
					}
				}
			}
			polledNode.setInfo("b");
		}	
	}

    /**
     * Returns true if and only if there is a valid path from EVREY node to each
     * other node. 
     * @return true if and only if there is a valid path from EVREY node to each other node
     */
	@Override
	public boolean isConnected() {
		
		ArrayList<node_info>allNodes=new ArrayList<node_info>(myGraph.getV());
		if(allNodes.size()==0 || allNodes.size()==1) {
			return true;
		}
		Dijkstra(myGraph,allNodes.get(0));
        for(int i=0;i<allNodes.size();i++) {
        	node_info n=allNodes.get(i);
		   if(getDistance(n)==Integer.MAX_VALUE) {
			   return false;
		   }
	   }
	return true;
	
	}

    /**
     * returns the length of the shortest path between src to dest
     *  if no such path returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return the length of the shortest path between src to dest
     */
	@Override
	public double shortestPathDist(int src, int dest) {
		
		node_info srcNode=myGraph.getNode(src);
		node_info destNode=myGraph.getNode(dest);
		if(srcNode==null || destNode==null)
			    return -1;
		if(srcNode==destNode)//are the same node
			  return 0;
		Dijkstra(myGraph, srcNode);
		if(getDistance(destNode)==Integer.MAX_VALUE) {
			return -1;
		}
		return getDistance(destNode);
	}

    /**
     * returns the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     *  if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return the shortest path between src to dest - as an ordered List of nodes
     */
	@Override
	public List<node_info> shortestPath(int src, int dest) {
		
		node_info srcNode=myGraph.getNode(src);
		node_info destNode=myGraph.getNode(dest);
		if(srcNode==null || destNode==null)
		          return null;
		if(srcNode==destNode) {//are the same node
			LinkedList<node_info> myList=new LinkedList<node_info>();
			myList.add(srcNode);
			return myList;
		}
	    LinkedList<node_info> myList=new LinkedList<node_info>();
		Dijkstra(myGraph, srcNode);
		node_info iterator=destNode;
		while(iterator!=srcNode) {
		     myList.add(iterator) ;                   	
		     iterator=myGraph.getNode((int)iterator.getTag());
		}
		myList.add(iterator);
		LinkedList <node_info>orderedList=new LinkedList<node_info>();
		ListIterator<node_info>iter=myList.listIterator(myList.size());
		while(iter.hasPrevious()) {
			orderedList.add(iter.previous());
		}
		return orderedList;
	}
    
    /**
     * Saves this weighted graph to the given
     * file name
     * @param file - the file name .
     * @return true - if the file was successfully saved
     */
	@Override
	public boolean save(String file) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject((WGraph_DS) myGraph);
			out.close();
			fileOut.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - if the graph was successfully loaded.
     * @throws IOException if the file cannot be opened
     */
	@Override
	public boolean load(String file) throws IOException {
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			myGraph = (WGraph_DS) in.readObject();
			in.close();
			fileIn.close();
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
