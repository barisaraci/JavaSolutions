
public class AlgorithmOne {
	
	class Node {
		public int value;
		public Node leftChild;
		public Node rightChild;
	}
	
	public static void main(String[] args) {
        AlgorithmOne instance = new AlgorithmOne();
        instance.run();
	}
	
	private void run() {
		Node root = new Node();
        root.value = 4;
        
        root.leftChild = new Node();
        root.leftChild.value = 2;
        root.rightChild = new Node();
        root.rightChild.value = 5;
        
        root.leftChild.leftChild = new Node();
        root.leftChild.leftChild.value = 1;
        root.leftChild.rightChild = new Node();
        root.leftChild.rightChild.value = 3;
        
        root.rightChild.leftChild = new Node();
        root.rightChild.leftChild.value = 4;
        root.rightChild.rightChild = new Node();
        root.rightChild.rightChild.value = 6;
        
		System.out.println(isBinaryTree(root));
	}
	
	private boolean isBinaryTree(Node node) {
		if (node == null)
			return false;
		else
			return isBinaryTreeHelper(node);
	}
	
	private boolean isBinaryTreeHelper(Node node) { // I created the second method to return null if the root is null
		if (node == null)
			return true;
		
		if ((node.leftChild != null && node.leftChild.value > node.value) || (node.rightChild != null && node.rightChild.value < node.value))
			return false;
		
		if (node.leftChild != null && !isBinaryTree(node.leftChild))
			return false;
		
		if (node.rightChild != null && !isBinaryTree(node.rightChild))
			return false;
				
		return true;
	}
	
}
