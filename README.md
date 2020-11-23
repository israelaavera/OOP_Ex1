This project describes two class:

 One -
External class WGraph_DS that implements the graph_weighted interface by implementing the interface of info_node (as an internal class) which describes the properties of a vertex in the graph and another internal class Edge which describes the properties of a weighted side.

the second-
WGraph_Algo class that implements the weighted_graph_algorithms interface.

The graph_weighted interface is a basic API that describes a weighted graph - so how is a weighted graph actually represented?
To do this I used the HashMap data structure, each weighted graph is represented by two HashMaps one for the vertices of the graph and the other for his weighted ribs and two more variables:
ModeCount-that counts the number of changes made to the graph.
numberOfEdges-which counts the number of ribs in the graph.

The WGraph_DS class provides weighted graph creation and methods such as:

- The vertex with the ID is returned if it exists, otherwise null will be returned (getNode (int key)).
-Testing the existence of a weighted side between two vertices (hasEdge (int node1, int node2)).
- The weight of the rid is returned, if the rid does not exist it will be returned 1- (getEdge (int node1, int node2)).
-Add a vertex to the graph only if it does not exist (addNode (int key)).
-Creating a rib between two vertices when the rib weight> = 0,if there is already a rib between them, the weight will only be updated (connect (int node1, int node2, double w)).
-Returns a collection that contains all the vertices of the graph (getV ()).
-Returns the collection of all neighbors of the vertex with the resulting ID (getV (int node_id)).
-Delete the vertex from the graph and remove all its corresponding ribs (removeNode (int key)).
-Deleting a rib from the graph (removeEdge (int node1, int node2)).
- The amount of vertices in the graph is returned (nodeSize ())
- The amount of ribs in the graph is returned (edgeSize ()).
- The amount of changes made to the graph is returned (getMC ()).

On the other hand, the weighted_graph_algorithms interface describes special algorithms that are executed on a weighted graph and is represented as follows:
For the performance of the algorithms I again chose to use the HashMap data structure which represented the distances of the vertices.

The Algo_WGraph class performs operations on an existing graph and provides methods such as:

- Initialized graph (init (weighted_graph g)).
- The graph on which the operations are performed is returned (getGraph ()).
-Deep copying of the graph using temporary collection, creation of new vertices and method connect(copy()).
-Return true if the entire graph is connected otherwise it will be returned false using the Dijkstra algorithm which solves the problem of finding the easiest route from a point in the graph to a destination in a weighted graph while mapping the entire graph which will help check its binding. (IsConnected ())
- The distance of the requested route is returned after using the Dijkstra algorithm. If there is no route at all, it will be returned -1(shortestPathDist (int src, int dest)).
- After using the Dijkstra algorithm a list is returned containing the requested route(shortestPath (int src, int dest)).
-Save a weighted graph into a file (save (String file)).
-Restore a graph from a file (load (String file)).

Enjoy!
