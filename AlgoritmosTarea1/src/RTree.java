import java.util.ArrayList;


public class RTree {
	Node root;
	
	public RTree(int t){
		root = new Node(t,null,true);
	}
	
	public void insert(Rectangle r, boolean variante){
		int before_sz = root.rectangles.size();
		root.insertRectangle(r, variante);
		/* si la cantidad de rectangulos que tenia disminuyo despues de 
		 * insertar significa que ocurrio un split y la raiz esta ahora
		 * en el padre del nodo "root"
		 */
		root = before_sz > root.rectangles.size() ? root.father : root;
	}
	
	public ArrayList<Rectangle> search(Rectangle r){
		ArrayList<Rectangle> lst = new ArrayList<Rectangle>();
		root.searchRectangle(r, lst);
		return lst;
	}
}
