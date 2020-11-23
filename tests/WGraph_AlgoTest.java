package ex1.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ex1.src.WGraph_DS;
import ex1.src.WGraph_Algo;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;

class WGraph_AlgoTest {

	 private static Random _rnd = null;

	
	    private static int nextRnd(int min, int max) {
	        double v = nextRnd(0.0+min, (double)max);
	        int ans = (int)v;
	        return ans;
	    }
	    private static double nextRnd(double min, double max) {
	        double d = _rnd.nextDouble();
	        double dx = max-min;
	        double ans = d*dx+min;
	        return ans;
	    }
	    /**
	     * Simple method for returning an array with all the node_data of the graph,
	     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
	     * @param g
	     * @return
	     */
	    private static int[] nodes(weighted_graph g) {
	        int size = g.nodeSize();
	        Collection<node_info> V = g.getV();
	        node_info[] nodes = new node_info[size];
	        V.toArray(nodes); // O(n) operation
	        int[] ans = new int[size];
	        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
	        Arrays.sort(ans);
	        return ans;
	    }
	
	
	
    @Test
    void isConnected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(0,0,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
  
        g0 = WGraph_DSTest.graph_creator(1,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

         g0 = WGraph_DSTest.graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());
        
         g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.init(g0);
        boolean b = ag0.isConnected();
        assertTrue(b);
    	Random rnd = new Random();
        weighted_graph g = new WGraph_DS();
        int nodesNum = 20;
        for(int i=0;i<nodesNum;i++) {
        	g.addNode(i);
        }
        for(int i=0;i<nodesNum;i++) {
            for(int j=0;j<nodesNum;j++) {
            	g.connect(i, j, rnd.nextDouble());
            }
        }
        for(int i=0;i<nodesNum;i++) {
            for(int j=0;j<nodesNum;j++) {
            	if (i != j) {
	                b = g.hasEdge(i,j);
	                assertTrue(b);
	                b = g.hasEdge(j,i);
	                assertTrue(b);
            	}
            	else {
	                b = g.hasEdge(i,j);
	                assertFalse(b);
	                b = g.hasEdge(j,i);
	                assertFalse(b);
            	}
            }
        }
        ag0 = new WGraph_Algo();
        ag0.init(g);
        b = ag0.isConnected();
        assertTrue(b);
        // remove a node will remain connected
        g.removeNode(0);
        b = ag0.isConnected();
        assertTrue(b);
        // remove a non exist edge will remain connected
        g.removeEdge(10,  39);
        b = ag0.isConnected();
        assertTrue(b);
        // remove all edges from a specific node will cause not be connected
        Collection<node_info> nodes = g.getV(10);
        ArrayList<Integer> nodesKeys = new ArrayList<Integer>();
        for (node_info n : nodes) {
        	nodesKeys.add(n.getKey());
        }
        for (int key : nodesKeys) {
        	g.removeEdge(10, key);
        }
        b = ag0.isConnected();
        assertFalse(b);
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        double d = ag0.shortestPathDist(0,10);
        assertEquals(d, 5.1);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {
        	assertEquals(n.getKey(), checkKey[i]);
        	i++;
        }
    }
    
    @Test
    void copy() throws IOException {
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        weighted_graph g1 = ag0.copy();
    	assertEquals(g0, g1);
    }
    @Test
    void save_load() throws IOException {
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        ag0.load(str);
        weighted_graph g1 = ag0.getGraph();
        // the object is in another place in the memory
        assertFalse(g0 == g1);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }

    private weighted_graph small_graph() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(11,0,1);
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);

        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);

        return g0;
    }
    
    
    
}
