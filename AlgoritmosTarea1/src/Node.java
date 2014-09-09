import java.util.ArrayList;


public class Node{
	
	ArrayList<Rectangle> rectangles;
	int t;
	Node father;
	boolean isLeaf;
	
	public Node(int t, Node father, boolean isLeaf){
		this.t = t;
		this.father = father;
		this.isLeaf = isLeaf;
		rectangles = new ArrayList<Rectangle>();
	}
	
	public void searchRectangle(Rectangle r, ArrayList<Rectangle> lst){
		for(Rectangle nr : rectangles){
			if(!r.equals(nr) && r.intersect(nr)){
				if(isLeaf)
					lst.add(nr);
				else
					nr.node.searchRectangle(r,lst);
			}
		}
	}

	void insertRectangle(Rectangle r, boolean variante, Node[] root){
		if(isLeaf)
			insertOverflowedRectangle(r,variante,root);
		else{
			Rectangle newr = rectangles.get(0);
			ArrayList<Rectangle> lst = new ArrayList<Rectangle>();
			lst.add(0,r);
			lst.add(1,newr);
			Rectangle newMBR = makeMBR(lst);
			double incremento = newMBR.area() - newr.area();
			for(Rectangle nr : rectangles){
				lst.remove(1);
				lst.add(1,nr);
				newMBR = makeMBR(lst);
				if((newMBR.area() - nr.area()) < incremento){
					incremento = newMBR.area() - nr.area();
					newr = nr;
				}else if((newMBR.area() - nr.area()) == incremento)
					newr = newr.area() < nr.area() ?  newr : nr;
			}
			newr.node.insertRectangle(r,variante,root);
		}
	}
	
	void insertOverflowedRectangle(Rectangle r, boolean variante, Node[] root){
		rectangles.add(r);
		if(rectangles.size() > 2*t){ //overflow
			if(father == null){
				father = new Node(t,null,false);
				root[0] = father; // sa cambia referencia a la raiz del arbol si hay overflow en esta
			}
			else{ // se elimina este nodo porque se insertara nuevamente
				for(Rectangle raux : father.rectangles){
					if(raux.node == this){
						father.rectangles.remove(raux);
						break;
					}
				}
			}
			// 2 listas para separar grupos
			ArrayList<Rectangle> g1 = new ArrayList<Rectangle>();
			ArrayList<Rectangle> g2 = new ArrayList<Rectangle>();
			if(variante)
				quadraticSplit(g1,g2);
			else
				linearSplit(g1,g2);
			Node node_brother = new Node(t,father,isLeaf);
			rectangles = g1;
			node_brother.rectangles = g2;
			Rectangle thisrectangle = makeMBR(g1);
			Rectangle rectangle_brother = makeMBR(g2);
			thisrectangle.node = this;
			rectangle_brother.node = node_brother;
			father.insertOverflowedRectangle(thisrectangle,variante,root);
			father.insertOverflowedRectangle(rectangle_brother,variante,root);
			father.update(node_brother);
		}
		if(father != null)
			father.update(this);
	}
	
	public void update(Node child){
		for(Rectangle r : rectangles){
			if(r.node == child){
				Rectangle newrectangle = makeMBR(child.rectangles);
				newrectangle.node = child;
				rectangles.remove(r);
				rectangles.add(newrectangle);
				break;
			}
		}
		if(father != null)
			father.update(this);
	}
	
	public Rectangle makeMBR(ArrayList<Rectangle> rectangles){
		double x1 = rectangles.get(0).x1;
		double x2 = rectangles.get(0).x2;
		double y1 = rectangles.get(0).y1;
		double y2 = rectangles.get(0).y2;
		for(Rectangle r : rectangles){
			x1 = x1 < r.x1 ? x1 : r.x1;
			x2 = x2 > r.x2 ? x2 : r.x2;
			y1 = y1 < r.y1 ? y1 : r.y1;
			y2 = y2 > r.y2 ? y2 : r.y2;
		}
		return new Rectangle(x1,x2,y1,y2);
	}
	
	public void quadraticSplit(ArrayList<Rectangle> g1, ArrayList<Rectangle> g2){
		Rectangle newMBR;
		// indices finales de los rectangulos elegidos para separar en 2 grupos
		int final_i = 0, final_j = 1;
		double incremento = 0.0;
		ArrayList<Rectangle> lst = new ArrayList<Rectangle>();
		// separando en grupos
		for(int i=0;i<rectangles.size()-1;i++){
			lst.add(rectangles.get(i));
			for (int j=i+1; j<rectangles.size();j++){
				lst.add(rectangles.get(j));
				newMBR = makeMBR(lst);
				double newincremento = newMBR.area() - rectangles.get(i).area() - rectangles.get(j).area();
				if(incremento < newincremento){
					incremento = newincremento;
					final_i = i;
					final_j = j;
				}
				lst.remove(rectangles.get(j));
			}
			lst.remove(rectangles.get(i));
		}
		// asignamos a R1 y R2 a grupos distintos
		Rectangle R1 = rectangles.get(final_i);
		Rectangle R2 = rectangles.get(final_j);
		g1.add(R1);
		g2.add(R2);
		rectangles.remove(R1);
		rectangles.remove(R2);
		// una vez elegidos los 2 rectangulos mas separados, se comienzan a agregar de a uno a los grupos
		while(!rectangles.isEmpty()){
			/* primero se verifica si alguno de los grupos tiene tan pocos rectangulos
			 * que alcanzaria t solo si se agregan todos los que quedan sin grupo
			 */
			if(g1.size()+rectangles.size() == t){
				for(Rectangle nr : rectangles)
					g1.add(nr);
				break;
			}else if(g2.size()+rectangles.size() == t){
				for(Rectangle nr : rectangles)
					g2.add(nr);
				break;
			}else{
				/* incrementos calculados para cada rectangulo si se agregaran al grupo 1 o 2,
				 * y tambien los incrementos finales, que serviran para decidir en cual grupo ira
				 */
				double incremento1 = 0, incremento2 = 0, incremento1_final = 0, incremento2_final = 0;
				double max = 0;
				Rectangle chosen = rectangles.get(0);
				for(Rectangle nr : rectangles){
					g1.add(nr);
					newMBR = makeMBR(g1);
					g1.remove(nr);
					incremento1 = newMBR.area() - makeMBR(g1).area();
					g2.add(nr);
					newMBR = makeMBR(g2);
					g2.remove(nr);
					incremento2 = newMBR.area() - makeMBR(g2).area();
					
					if(Math.abs(incremento1-incremento2) > max){
						max = Math.abs(incremento1-incremento2);
						chosen = nr;
						incremento1_final = incremento1;
						incremento2_final = incremento2;
					}
				}
				// 	eligiendo a que grupo ira el rectangulo elegido
				if(incremento1_final < incremento2_final)
					g1.add(chosen);
				else if(incremento1_final > incremento2_final)
					g2.add(chosen);
				else{
					double area1 = makeMBR(g1).area();
					double area2 = makeMBR(g2).area();
					if(area1 < area2)
						g1.add(chosen);
					else if(area1 > area2)
						g2.add(chosen);
					else{
						if(g1.size() < g2.size())
							g1.add(chosen);
						else if(g1.size() > g2.size())
							g2.add(chosen);
						else{
							if(Math.random() <= 0.5)
								g1.add(chosen);
							else
								g2.add(chosen);
						}
					}
				}
				rectangles.remove(chosen);
			}
		}
	}
	
	void linearSplit(ArrayList<Rectangle> g1, ArrayList<Rectangle> g2){
		Rectangle R1, R2;
		/* para ambas dimensiones buscamos el rectangulo Ad cuyo lado mayor ad es minimo
		 * y el rectangulo Bd cuyo lado menor bd es maximo
		 */
		Rectangle Ax = rectangles.get(0);
		double ax = Ax.x2;
		Rectangle Ay = rectangles.get(0);
		double ay = Ay.y2;
		Rectangle Bx = rectangles.get(0);
		double bx = Bx.x1;
		Rectangle By = rectangles.get(0);
		double by = By.y1;
		for(Rectangle nr : rectangles){
			if(nr.x2 < ax){
				ax = nr.x1;
				Ax = nr;
			}
			if(nr.y2 < ay){
				ay = nr.y1;
				Ay = nr;
			}
			if(nr.x1 > bx){
				bx = nr.x2;
				Bx = nr;
			}
			if(nr.y1 > by){
				by = nr.y2;
				By = nr;
			}
		}
		double anchox = ( Bx.x2 < Ax.x2 ? Ax.x2 : Bx.x2) - (Bx.x1 > Ax.x1 ? Ax.x1 : Bx.x1);
		double anchoy = ( By.y2 < Ay.y2 ? Ay.y2 : By.y2) - (By.y1 > Ay.y1 ? Ay.y1 : By.y1);
		double wx = (bx - ax)/anchox;
		double wy = (by - ay)/anchoy;
		if(wx > wy){
			if(Ax != Bx){
				R1 = Ax;
				R2 = Bx;
			}else{
				R1 = rectangles.get(0);
				R2 = rectangles.get(1);
			}
		}else{
			if(Ay != By){
				R1 = Ay;
				R2 = By;
			}else{
				R1 = rectangles.get(0);
				R2 = rectangles.get(1);
			}
		}
		g1.add(R1);
		g2.add(R2);
		rectangles.remove(R1);
		rectangles.remove(R2);
		// una vez elegidos los 2 rectangulos, se comienzan a agregar de a uno a los grupos
		while(!rectangles.isEmpty()){
			/* primero se verifica si alguno de los grupos tiene tan pocos rectangulos
			 * que alcanzaria t solo si se agregan todos los que quedan sin grupo
			 */
			Rectangle r = rectangles.get(0);
			if(g1.size()+rectangles.size() == t){
				for(Rectangle nr : rectangles)
					g1.add(nr);
				break;
			}else if(g2.size()+rectangles.size() == t){
				for(Rectangle nr : rectangles)
					g2.add(nr);
				break;
			}else{
				double incremento1 = 0;
				double incremento2 = 0;
				Rectangle newMBR;
				g1.add(r);
				newMBR = makeMBR(g1);
				g1.remove(r);
				incremento1 = newMBR.area() - makeMBR(g1).area();
				g2.add(r);
				newMBR = makeMBR(g2);
				g2.remove(r);
				incremento2 = newMBR.area() - makeMBR(g2).area();
				if(incremento1 < incremento2)
					g1.add(r);
				else if(incremento1 > incremento2)
					g2.add(r);
				else{
					double area1 = makeMBR(g1).area();
					double area2 = makeMBR(g2).area();
					if(area1 < area2)
						g1.add(r);
					else if(area1 > area2)
						g2.add(r);
					else{
						if(g1.size() < g2.size())
							g1.add(r);
						else if(g1.size() > g2.size())
							g2.add(r);
						else{
							if(Math.random() <= 0.5)
								g1.add(r);
							else
								g2.add(r);
						}
					}
				}
			}
			rectangles.remove(r);
		}
	}
}
