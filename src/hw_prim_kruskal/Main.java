package hw_prim_kruskal;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
	/*The following square matrix represents a weighted undirected graph.
	    the value in (i,j) position indicates the cost between i and j node.
	    Zeros indicate no connection*/
	
		static int[][] matrix = { { 0, 3, 0, 2, 0, 0, 0, 0, 4 }, // 0
								  { 3, 0, 0, 0, 0, 0, 0, 4, 0 }, // 1
								  { 0, 0, 0, 6, 0, 1, 0, 2, 0 }, // 2
								  { 2, 0, 6, 0, 1, 0, 0, 0, 0 }, // 3
								  { 0, 0, 0, 1, 0, 0, 0, 0, 8 }, // 4
								  { 0, 0, 1, 0, 0, 0, 8, 0, 0 }, // 5
								  { 0, 0, 0, 0, 0, 8, 0, 0, 0 }, // 6
								  { 0, 4, 2, 0, 0, 0, 0, 0, 0 }, // 7
								  { 4, 0, 0, 0, 8, 0, 0, 0, 0 } // 8	
		};
		/*static int[][] matrix = { { 0, 2, 3, 0, 0 }, // 0
				{ 2, 0, 15, 2, 0 }, // 1
				{ 3, 15, 0, 0, 13}, // 2
				{ 0, 2, 0, 0, 9}, // 3
				{ 0, 0, 13, 9, 0} // 4
		};*/
	
	static int Node = matrix.length;
	static int[][] Edge = new int[Node][Node];
	static int NotConnected = 999999;
	
	static ArrayList<Edge> edges = new ArrayList<>();
	
	static class Edge {
		public int v1, v2, weight;
		
		Edge (int v1, int v2, int weight) {
			this.v1 = v1;
			this.v2 = v2;
			this.weight = weight;
		}
	}
	
	static class Vertex {
		public int v;
		public boolean isMarked;
		
		Vertex (int v) {
			this.v = v;
			isMarked = false;
		}
	}
	
	public static void MakeGraph() {
		for (int i = 0; i < Node; i++) {
			for (int j = 0; j < Node; j++) {
				Edge[i][j] = matrix[i][j];
				if (Edge[i][j] == 0) {// If Node i and Node j are not connected
					Edge[i][j] = NotConnected;
				} else {
					if (i <= j) // do not add duplicates
						edges.add(new Edge(i, j, Edge[i][j]));
				}
			}
		}
		
		// Print the graph representation matrix.
		for (int i = 0; i < Node; i++) {
			for (int j = 0; j < Node; j++)
				if (Edge[i][j] != NotConnected)
					System.out.print(" " + Edge[i][j] + " ");
				else // when there is no connection
					System.out.print(" * ");
			System.out.println();
		}
	}
	
	public static void Prim() {
		System.out.println("OUPUT OF PRIM'S ALGORITHM:");
		// Write the code for Prim algorithm to find the minimum cost spanning tree here
		// and print the result in console with the following format:
		/*==========================OUTPUT FORMAT===========================
                Minimum Cost of Spanning Tree = "....... "       
                
				Edges of the minimum cost spanning tree:
				".........................................................."
				(for example:
				Edges of the minimum cost spanning tree:
     			"0 -- 1
     			 7 -- 2
     			 0 -- 3
     			 3 -- 4
     		 	 2 -- 5
     			 5 -- 6
     			 1 -- 7
     			 0 -- 8)"
		================================================================== */
		
		ArrayList<Edge> mst = new ArrayList<>();
		ArrayList<Vertex> vertices = new ArrayList<>();
		int parent[] = new int[Node];
		for (int i = 0; i < Node; i++) vertices.add(new Vertex(Integer.MAX_VALUE));
		vertices.get(0).v = 0;
		
		for(int i = 0; i < Node - 1; i++) {
			int smallest = Integer.MAX_VALUE;
			int smallestIndex = -1;
			
			for (int j = 0; j < vertices.size(); j++) {
				if (vertices.get(j).v < smallest && !vertices.get(j).isMarked) {
					smallest = vertices.get(j).v;
					smallestIndex = j;
				}
			}
			
			vertices.get(smallestIndex).isMarked = true;
			
			for (int j = 0; j < Node; j++) {
                if (Edge[smallestIndex][j] != 0 && !vertices.get(j).isMarked && Edge[smallestIndex][j] < vertices.get(j).v) {
                    parent[j] = smallestIndex;
                    vertices.get(j).v = Edge[smallestIndex][j];
                }
        	}
		}
		
		for (int i = 0; i < parent.length; i++) {
			for (Edge e : edges) {
				if ((e.v1 == i && e.v2 == parent[i]) || (e.v2 == i && e.v1 == parent[i]))  {
					mst.add(e);
				}
			}
		}
		
		int minimumCost = 0;
		for (Edge e : mst) {
			minimumCost += e.weight;
		}
		System.out.println("Minimum Cost of Spanning Tree = " + minimumCost);
		System.out.println("Edges of the minimum cost spanning tree: ");
		for (Edge e : mst) {
			System.out.println(e.v1 + " -- " + e.v2);
		}
	}
	
	public static void Kruskal(){
		System.out.println("OUPUT OF KRUSKAL'S ALGORITHM:");
		// Write the code for Kruskal algorithm to find the minimum cost spanning tree here
		// and print the result in console with the following format:	
		/*==========================OUTPUT FORMAT===========================
                Minimum Cost of Spanning Tree = "....... "       
                
				Edges of the minimum cost spanning tree:
				".........................................................."
		================================================================== */
		ArrayList<Edge> mst = new ArrayList<>();
		
		Collections.sort(edges, (Edge e1, Edge e2) -> e1.weight - e2.weight);
		
		for (Edge edge : edges) {
			mst.add(edge);
			
			ArrayList<Integer> vertices = new ArrayList<>(); 
			
		    for (Edge e : mst) {
		    	boolean isV1Found = false, isV2Found = false;
		    	for (int v : vertices) {
		    		if (e.v1 == v) {
		    			isV1Found = true;
		    		}
		    		if (e.v2 == v) {
		    			isV2Found = true;
		    		}
		    	}
		    	if (!isV1Found) vertices.add(e.v1);
		    	if (!isV2Found) vertices.add(e.v2);
		    }
		    
		    if (mst.size() > vertices.size() - 1) { // check for a cycle
		    	mst.remove(mst.size() - 1);
		    }
		}
		
		int minimumCost = 0;
		for (Edge e : mst) {
			minimumCost += e.weight;
		}
		System.out.println("Minimum Cost of Spanning Tree = " + minimumCost);
		System.out.println("Edges of the minimum cost spanning tree: ");
		for (Edge e : mst) {
			System.out.println(e.v1 + " -- " + e.v2);
		}
	}

	public static void  main(String[] args) {
		MakeGraph();
		Prim();
		Kruskal();
	}

}
