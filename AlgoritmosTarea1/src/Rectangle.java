import java.io.Serializable;

// serializable para luego obtener su tamaño
public class Rectangle implements Serializable{

	/* rectangulo representado por 2 puntos:
	 * esquina inferior izquierda (x1,y1)
	 * esquina superior derecha (x2,y2)
	 */
	
	private static final long serialVersionUID = 1L;
	double x1, y1, x2, y2;
	Node node;
	
	public Rectangle(double x1, double x2, double y1, double y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/*
	 * Rectangulos que comparten solo una arista no se concidera
	 * que se intersecten
	 */
	boolean intersect(Rectangle r){
		return (x1 <= r.x1 && r.x1 < x2) && ( y1 < r.y2 && r.y2 <= y2)
		|| (x1 <= r.x1 && r.x1 < x2) && ( y1 <= r.y1 && r.y1 < y2)
		|| (x1 < r.x2 && r.x2 <= x2) && ( y1 <= r.y1 && r.y1 < y2)
		|| (x1 < r.x2 && r.x2 <= x2) && ( y1 < r.y2 && r.y2 <= y2);
	}
	
	double area(){
		return Math.abs(x2-x1)*Math.abs(y2-y1);
	}
	
	@Override
	public String toString(){
		return "x1="+x1+"\n"+"x2="+x2+"\n"+"y1="+y1+"\n"+"y2="+y2+"\n";
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Rectangle){
			Rectangle r = (Rectangle)o;
			return r.x1 == x1 && r.x2 == x2 && r.y1 == y1 && r.y2 == y2;
		}
		return false;
	}

}
