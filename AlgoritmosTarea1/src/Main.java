import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;


public class Main {

	public static void main(String[] args) throws IOException{
		int t = getT();
		RTree tree1, tree2;																																																															
		for(int i=9;i<=24;i+=3){
			tree1 = new RTree(t);
			tree2 = new RTree(t);																																																														
			for(int j=0;j<Math.pow(2,i);j++){
				tree1.insert(randomRectangle(),false);
			}
			System.out.println(i);																																																																																																																							
		}
		

	}
	
	static public int getT(){
		// Tamaño de bloque = 4 KB
		int block_sz = 4096;
		// rectangulos para calcular tamaño de nodo
		Rectangle r0 = new Rectangle(0.5,4,3,4);
		Rectangle r1 = new Rectangle(1,2,3,4);
		Rectangle r2 = new Rectangle(1,2,3,4);
		Node n = new Node(2,null,true);
		n.rectangles.add(r0);
		// tamaños con 1, 2 y 3 rectangulos
		int with1rec = 0, with2rec = 0, with3rec = 0;
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos) ;
			out.writeObject(n);
			out.close();
			with1rec = bos.toByteArray().length;
			n.rectangles.add(r1);
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos) ;
			out.writeObject(n);
			out.close();
			with2rec = bos.toByteArray().length;
			n.rectangles.add(r2);
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos) ;
			out.writeObject(n);
			out.close();
			with3rec = bos.toByteArray().length;
		}catch (IOException e){}
		/* primero esta el tamaño de un nodo con un rectangulo
		 * luego al agregar consecutivamente rectangulos se suma un tamaño
		 * constante. se despeja t de la formula:
		 * block_size = size(nodo + rectangulo) + (delta de agregar 1 rectangulo)*(2*t-1)
		 */
		return (block_sz - with1rec)/(2*(with3rec - with2rec)) - (with3rec - with2rec);
	}
	
	static public Rectangle randomRectangle(){
		Random r = new Random();
		/* (x1,y1) es la esquina inferior izquierda del rectangulo
		 *  la que se obtiene de forma uniformemente aleatoria
		 */
		double x1 = randomDouble(0,500000,r);
		double y1 = randomDouble(0,500000,r);
		double x2 = x1 + randomDouble(0,100, r);
		double y2 = y1 + randomDouble(0,100,r)/(x2-x1);
		return new Rectangle(x1,x2,y1,y2);
		
	}
	
	static public double randomDouble(double min, double max, Random r){
		return min + (max - min)*r.nextDouble();
	}

}
