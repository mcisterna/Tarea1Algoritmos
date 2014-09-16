import java.util.ArrayList;


public class RTree {
	Node[] root; // puntero
	
	
	//genero un arbol con raiz nula
	public RTree(int t){
		root = new Node[1];
		root[0] = new Node(t,null,true);
	}
	// inserto en el arbol un nuevo nuevo nodo (rectangulo)
	public void insert(Rectangle r, boolean variante){
		root[0].insertRectangle(r, variante,root);
	}
	// busco en el arbol el rectangulo y retorno una lista de rectangulos
	public ArrayList<Rectangle> search(Rectangle r){
		ArrayList<Rectangle> lst = new ArrayList<Rectangle>();
		root[0].searchRectangle(r, lst);
		return lst;
	}
}
