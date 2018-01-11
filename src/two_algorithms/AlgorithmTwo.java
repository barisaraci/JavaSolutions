
public class AlgorithmTwo {
	
	// The person who passed data structures course should solve this questions easily.
	// In our final exam of data structures course, undirected graph cycle version of this question was asked.
	// For this question, I tried to keep the class as you give it
	// but my first implementation was O(n!) so I removed it and add a new variable to class to make it O(n).
	// It is a known algorithm, it simply marks the nodes which visited before so if the next node of the tail node refers to a node visited before then there is a cycle.
	// Before uploading the code to github, I checked stackoverflow to find a better implementation out of curiosity and found Floyd's cycle-finding algorithm,
	// which two nodes move on the list, one jumping twice among nodes and if that node refers to the other node moving one by one(slower), then it means there is a cycle.
	// It is the best one I guess since it has a lower space complexity but I will not use it since it is not my implementation.
	
	class Node {
		public int value;
		public Node next;
		
		public boolean isVisited;
	}
	
	public static void main(String[] args) {
        AlgorithmTwo instance = new AlgorithmTwo();
        instance.run();
	}
	
	private void run() {
		Node root = new Node();
        root.next = new Node();
        root.next.next = new Node();
        root.next.next.next = new Node();
        root.next.next.next.next = root.next;
		
        System.out.println(isListCycle(root));
	}
	
	private boolean isListCycle(Node node) {
		if (node.next == null) {
			return false;
		}
		
		if (node.next != null && node.next.isVisited)
			return true;
		
		node.isVisited = true;
		if (isListCycle(node.next))
			return true;
		
		return false;
	}

}
