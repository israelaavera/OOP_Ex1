package ex1.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;

class WGraph_DSTest {
	private static Random _rnd = null;
    @Test
    void nodeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(1);

        g.removeNode(2);
        g.removeNode(1);
        g.removeNode(1);
        int s = g.nodeSize();
        assertEquals(1,s);
        g.addNode(0);
        s = g.nodeSize();
        assertEquals(1,s);

        g = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
        	g.addNode(i);
        }
        s = g.nodeSize();
        assertEquals(10,s);
        // remove one node
        g.removeNode(1);
        s = g.nodeSize();
        // make sure that it was removed
        assertEquals(9,s);
        // try to remove the same node again
        g.removeNode(1);
        s = g.nodeSize();
        // make sure that the number of nodes wasn't changed
        assertEquals(9,s);
        g.addNode(11);
        s = g.nodeSize();
        // make sure that the node was added
        assertEquals(10,s);
        g.addNode(11);
        s = g.nodeSize();
        // make sure that no node was added becasue we have already node 11
        assertEquals(10,s);
    }

    @Test
    void edgeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(0,1,1);
        int e_size =  g.edgeSize();
        assertEquals(3, e_size);
        double w03 = g.getEdge(0,3);
        double w30 = g.getEdge(3,0);
        assertEquals(w03, w30);
        assertEquals(w03, 3);

        // test that the removeNode also removes the edges
        g.removeNode(1);
        e_size =  g.edgeSize();
        assertEquals(2, e_size);
        g.removeNode(1);
        e_size =  g.edgeSize();
        assertEquals(2, e_size);
        g.removeNode(0);
        e_size =  g.edgeSize();
        assertEquals(0, e_size);
    }

    @Test
    void getV() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(0,1,1);
        Collection<node_info> v = g.getV();
        Iterator<node_info> iter = v.iterator();
        while (iter.hasNext()) {
            node_info n = iter.next();
            assertNotNull(n);
        }
        int v_size =  v.size();
        assertEquals(4, v_size);
        // remove of a non exist node
        g.removeNode(8);
        v_size =  v.size();
        assertEquals(4, v_size);
        // remove of an existing node
        g.removeNode(2);
        v_size =  v.size();
        assertEquals(3, v_size);
    }

    @Test
    void hasEdge() {
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
	                boolean b = g.hasEdge(i,j);
	                assertTrue(b);
	                b = g.hasEdge(j,i);
	                assertTrue(b);
            	}
            	else {
	                boolean b = g.hasEdge(i,j);
	                assertFalse(b);
	                b = g.hasEdge(j,i);
	                assertFalse(b);
            	}
            }
        }
    }

    @Test
    void connect() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.removeEdge(0,1);
        assertFalse(g.hasEdge(1,0));
        g.removeEdge(2,1);
        g.connect(0,1,1);
        double w = g.getEdge(1,0);
        assertEquals(w,1);
        g.removeEdge(1, 0);
        w = g.getEdge(1,0);
        // no edge so the weight should be -1
        assertEquals(w,-1);
    }


    @Test
    void removeNode() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.removeNode(4);
        g.removeNode(0);
        assertFalse(g.hasEdge(1,0));
        int e = g.edgeSize();
        assertEquals(0,e);
        assertEquals(3,g.nodeSize());
    }

    @Test
    void removeEdge() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.removeEdge(0,3);
        double w = g.getEdge(0,3);
        assertEquals(w,-1);
    }
    @Test
    void testTime() {
    	Random rnd = new Random();
        long start = new Date().getTime();
        weighted_graph g = new WGraph_DS();
        int nodesNum = 1000000;
        for(int i=0;i<nodesNum;i++) {
        	g.addNode(i);
        }
      
        int edgesNum = 1000;
        for(int i=0;i<edgesNum;i++) {
            for(int j=0;j<edgesNum;j++) {
            	g.connect(i, j, rnd.nextDouble());
            }
        }
        long end = new Date().getTime();
        double dt = (end-start)/1000.0;
        boolean t = dt<5;
        assertTrue(t);
    }
    ///////////////////////////////////
    /**
     * Generate a random graph with v_size nodes and e_size edges
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for(int i=0;i<v_size;i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
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
    
}
