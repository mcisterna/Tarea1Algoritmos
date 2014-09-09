import java.util.ArrayList;


public class RTree {
	Node[] root; // puntero
	
	public RTree(int t){
		root = new Node[1];
		root[0] = new Node(t,null,true);
	}
	
	public void insert(Rectangle r, boolean variante){
		root[0].insertRectangle(r, variante,root);
	}
	
	public ArrayList<Rectangle> search(Rectangle r){
		ArrayList<Rectangle> lst = new ArrayList<Rectangle>();
		root[0].searchRectangle(r, lst);
		return lst;
	}
}
