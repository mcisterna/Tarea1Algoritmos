import java.util.ArrayList;


public class RTree {
	Node[] root; // puntero
	
	public RTree(int t){
		root = new Node[1];
		root[0] = new Node(t,null,true);
	}
	
	public int insert(Rectangle r, boolean variante){
		return root[0].insertRectangle(r,variante,root);
	}
	
	public int search(Rectangle r, ArrayList<Rectangle> lst){
		return root[0].searchRectangle(r,lst);
	}
}
