package ex1.src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * this class implements the weighted graph
 * @author Israela
 *
 */
public class WGraph_DS implements weighted_graph, Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * the nodes in the graph
	 */
	private HashMap<Integer, node_info> nodesInGraph = new HashMap<Integer, node_info>();
	/**
	 * the edges from each node
	 */
	private HashMap<Integer, HashMap<Integer, Edge>> edgesInGraph = new HashMap<>();
	/**
	 * the number of changes
	 */
	private int modeCount;
	/**
	 * the number of edges in the graph
	 */
	private int numberOfEdges;
	

    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
	@Override
	public node_info getNode(int key) {
		if (nodesInGraph.get(key) != null)
			return nodesInGraph.get(key);
		return null;
	}

    /**
     * return true iff there is an edge between node1 and node2
     * @param node1 the first node
     * @param node2 the second node
     * @return
     */
	@Override
	public boolean hasEdge(int node1, int node2) {
		if (!edgesInGraph.containsKey(node1))
			return false;
		HashMap<Integer, Edge> edges = edgesInGraph.get(node1);
		return edges.containsKey(node2);
	}

	/**
	 * @return true if the graphs are equal
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof weighted_graph)) {
			return false;
		}

		weighted_graph G = (weighted_graph) o;

		boolean isEqualGraphes = isEqual(G);

		return isEqualGraphes;
	}

	/**
	 * @param other the other graph to be compared to
	 * @return true if the graphs are equal
	 */
	private boolean isEqual(weighted_graph other) {
		
		if (nodesInGraph.size() != other.nodeSize())
			return false;
		for (Map.Entry<Integer, node_info> n : nodesInGraph.entrySet()) {
			node_info otherNode = other.getNode(n.getKey());
			if (otherNode == null)
				return false; // the node doesn't exist in the other graph
			if (! otherNode.equals(n.getValue()))
				return false;
		}
		WGraph_DS graph = (WGraph_DS) other;
		HashMap<Integer, HashMap<Integer, Edge>> otherEdgesInGraph = graph.edgesInGraph;
		if (edgesInGraph.size() != otherEdgesInGraph.size())
			return false;
		for (Map.Entry<Integer, HashMap<Integer, Edge>> e : edgesInGraph.entrySet()) {
			HashMap<Integer, Edge> otherEdges = otherEdgesInGraph.get(e.getKey());
			if (otherEdges == null)
				return false;
			if (e.getValue().size() != otherEdges.size())
				return false;
			for (Map.Entry<Integer, Edge> edge : otherEdges.entrySet()) {
				Edge otherEdge = otherEdges.get(edge.getKey());
				if ((otherEdge == null) || (!otherEdge.equals(edge.getValue())))
					return false;
			}
		}

		return true;
	}

    /**
     * return the weight of the edge (node1, node1). In case
     * there is no such edge return -1
     * @param node1 the first node
     * @param node2 the second node
     * @return the weight
     */
	@Override
	public double getEdge(int node1, int node2) {
		if (!edgesInGraph.containsKey(node1))
			return -1;
		HashMap<Integer, Edge> edges = edgesInGraph.get(node1);
		if (!edges.containsKey(node2))
			return -1;
		Edge edge = edges.get(node2);
		return edge.getWeight();
	}

    /**
     * add a new node to the graph with the given key.
     * if there is already a node with such a key -> no action performed.
     * @param key the key of the new node
     */
	@Override
	public void addNode(int key) {

		if (!nodesInGraph.containsKey(key)) {
			node_info ni = new NodeInfo();
			((NodeInfo) ni).setKey(key);
			nodesInGraph.put(key, ni);
			modeCount++;
		}
	}

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     * @param node1 the first node
     * @param node2 the second node
     */
	@Override
	public void connect(int node1, int node2, double w) {

		if ((node1 != node2) && (w >= 0 )) {

			if (!hasEdge(node1, node2)) {
				Edge e = new Edge();
				node_info firstNode = nodesInGraph.get(node1);
				node_info secondNode = nodesInGraph.get(node2);
				if(firstNode == null || secondNode ==null) {
					System.out.println("Can't connect");
					return;
				}
				e.setFirstNode(firstNode);
				e.setSecondNode(secondNode);
				e.setWeight(w);
				HashMap<Integer, Edge> edges = null;
				if (edgesInGraph.containsKey(node1))
					edges = edgesInGraph.get(node1);
				else {
					edges = new HashMap<Integer, Edge>();
					edgesInGraph.put(node1, edges);
				}
				edges.put(node2, e);

				e = new Edge();
				e.setFirstNode(secondNode);
				e.setSecondNode(firstNode);
				e.setWeight(w);
				if (edgesInGraph.containsKey(node2))
					edges = edgesInGraph.get(node2);
				else {
					edges = new HashMap<Integer, Edge>();
					edgesInGraph.put(node2, edges);
				}
				edges.put(node1, e);

				modeCount++;
				numberOfEdges++;
			}
			else {
				// update the weight if the edge already exist
				Edge e = getSpecificEdge(node1, node2);
				if(e.weight != w) {
				if (e != null)
					e.setWeight(w);
				e = getSpecificEdge(node2, node1);
				if (e != null)
					e.setWeight(w);
				    modeCount++;
				}
		    }
		}
	}

    /**
     * return the edge (node1, node1). In case
     * there is no such edge - should return null
     * @param node1 the first node
     * @param node2 the second node
     * @return the edge
     */
	private Edge getSpecificEdge(int node1, int node2) {
		if (!edgesInGraph.containsKey(node1))
			return null;
		HashMap<Integer, Edge> edges = edgesInGraph.get(node1);
		if (!edges.containsKey(node2))
			return null;
		Edge edge = edges.get(node2);
		return edge;
	}
	
	
	/**
	 * @param numberOfEdges set number of edges in the graph
	 */
	public void setNumberOfEdges(int numberOfEdges){
		this.numberOfEdges = numberOfEdges;
	}
	
	/**
     * @return the number of edges in the graph
     */
	public int getNumberOfEdges(){
		return this.numberOfEdges;
	}
	
	

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
	@Override
	public Collection<node_info> getV() {
		return nodesInGraph.values();
	}

    /**
    *
    * This method returns a Collection containing all the
    * nodes connected to node_id
    * @param node_id the id of the node
    * @return Collection<node_data>
    */
	@Override
	public Collection<node_info> getV(int node_id) {
		if (! edgesInGraph.containsKey(node_id))
			return new ArrayList<node_info>(); // return an empty collection
		Collection<Edge> edges = edgesInGraph.get(node_id).values();
		ArrayList<node_info> answer = new ArrayList<node_info>();
		for (Edge e : edges) {
			answer.add(e.getSecondNode());
		}
		return answer;
	}

    /**
     * Delete the node from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key the key
     */
	@Override
	public node_info removeNode(int key) {
		if (! nodesInGraph.containsKey(key))
			return null;
		node_info ni = nodesInGraph.get(key);
		nodesInGraph.remove(key);
		modeCount++;
		if (edgesInGraph.containsKey(key)) {
			HashMap<Integer, Edge> edges = edgesInGraph.remove(key);
			modeCount += edges.size();
			numberOfEdges -= edges.size();
		}
		Iterator<Entry<Integer, HashMap<Integer, Edge>>> it = edgesInGraph.entrySet().iterator();
		// iterating every set of entry in the HashMap. 
		while (it.hasNext()) {
			Map.Entry<Integer, HashMap<Integer, Edge>> set = it.next();
			HashMap<Integer, Edge> edges = set.getValue();
			if (edges.containsKey(key)) {
				edges.remove(key);
				modeCount++;
			}
		}
		return ni;
	}

    /**
     * Delete the edge from the graph,
     * @param node1 the first node
     * @param node2 the second node
     */
	@Override
	public void removeEdge(int node1, int node2) {

		if (hasEdge(node1, node2)) {
			modeCount++;
			numberOfEdges--;
			HashMap<Integer, Edge> edges = edgesInGraph.get(node1);
			if (edges.containsKey(node2)) {
				edges.remove(node2);
			}
			edges = edgesInGraph.get(node2);
			if (edges.containsKey(node1)) {
				edges.remove(node1);
			}
		}
	}

    /** return the number of nodes in the graph.
     * @return the number of nodes in the graph
     */
	@Override
	public int nodeSize() {
		return nodesInGraph.size();
	}

    /**
     * return the number of edges.
     * @return the number of edges
     */
	@Override
	public int edgeSize() {
		return numberOfEdges;
	}

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph cause an increment in the ModeCount
     * @return the Mode Count - for testing changes in the graph
     */
	@Override
	public int getMC() {
		return modeCount;
	}

	public void setMC(int mc) {
		this.modeCount = mc;
	}

	/**
	 * this class implements the node info
	 * @author Israela
	 *
	 */
	private class NodeInfo implements node_info, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * the key
		 */
		private int key;
		/**
		 * the info
		 */
		private String info = "";
		/**
		 * the tag
		 */
		private double tag;

		/**
		 * empty constructor
		 */
		public NodeInfo() {
		}

		/**
		 * copy constructor
		 */
		public NodeInfo(node_info nd) {
			NodeInfo newNodeInfo = (NodeInfo) nd;
			this.key = newNodeInfo.getKey();
			this.info = newNodeInfo.info;
			this.tag = newNodeInfo.tag;
		}

	    /**
	     * Return the key (id) associated with this node.
	     * @return the key
	     */
		@Override
		public int getKey() {

			return key;
		}

	    /**
	     * return the remark (meta data) associated with this node.
	     * @return the remark (meta data) associated with this node
	     */
		@Override
		public String getInfo() {
			return info;
		}

	    /**
	     * Allows changing the remark (meta data) associated with this node.
	     * @param s the new info
	     */
		@Override
		public void setInfo(String s) {

			this.info = s;

		}

	    /**
	     * Temporal data which can be used be algorithms
	     * @return the tag
	     */
		@Override
		public double getTag() {
			return tag;
		}

	    /**
	     * @param t - the new value of the tag
	     */
		@Override
		public void setTag(double t) {
			this.tag = t;
		}

		/**
		 * set the key
		 * @param key the new key
		 */
		public void setKey(int key) {
			this.key = key;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof NodeInfo))
				return false;
			NodeInfo n = (NodeInfo) obj;
			return (key == n.getKey()) && info.equals(n.getInfo()) && (tag == n.getTag());
		}
	}

	/**
	 * This class represents an edge in the graph
	 * @author Israela
	 *
	 */
	private class Edge implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * the source of the edge
		 */
		private node_info firstNode;
		/**
		 * the target of the edge
		 */
		private node_info secondNode;
		/**
		 * the weight of the edge
		 */
		private double weight;

		/**
		 * @return the source of the edge
		 */
		public node_info getFirstNode() {
			return firstNode;
		}
		/**
		 * set the source of the edge
		 * @param firstNode the source of the edge
		 */
		public void setFirstNode(node_info firstNode) {
			this.firstNode = firstNode;
		}
		
		/**
		 * @return the target of the edge
		 */
		public node_info getSecondNode() {
			return secondNode;
		}
		
		/**
		 * set the target of the edge
		 * @param secondNode the target of the edge
		 */
		public void setSecondNode(node_info secondNode) {
			this.secondNode = secondNode;
		}
		
		/**
		 * @return the weight of the edge
		 */
		public double getWeight() {
			return weight;
		}

		/**
		 * set the weight of the edge
		 * @param weight the weight of the edge
		 */
		public void setWeight(double weight) {
			this.weight = weight;
		}
		@Override
		public boolean equals(Object arg0) {
			if (!(arg0 instanceof Edge))
				return false;
			Edge e = (Edge) arg0;
			return (weight == e.getWeight()) && firstNode.equals(e.getFirstNode()) &&
					secondNode.equals(e.getSecondNode());
		}

	}
}
