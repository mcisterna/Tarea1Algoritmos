import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;


public class Main {

	public static void main(String[] args) throws IOException{
		int t = getT();
		RTree tree1, tree2;
		int cnt1, cnt2, n;
		PrintWriter writer1 = new PrintWriter("insertion_quadratic_data.txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter("insertion_linear_data.txt", "UTF-8");
		PrintWriter writer3 = new PrintWriter("search_quadratic_data.txt", "UTF-8");
		PrintWriter writer4 = new PrintWriter("search_linear_data.txt", "UTF-8");
		// listas para guardar cantidad de I/Os y tiempo al hacer busquedas, con el fin de luego calcular promedios
		ArrayList<Number> IO_quadratic = new ArrayList<Number>();
		ArrayList<Number> IO_linear = new ArrayList<Number>();
		ArrayList<Number> time_quadratic = new ArrayList<Number>();
		ArrayList<Number> time_linear = new ArrayList<Number>();
		ArrayList<Rectangle> rec_lst = new ArrayList<Rectangle>();
		writer1.println("Tamano (2^n)	Cantidad I/Os	Tiempo (milisegundos)");
		writer2.println("Tamano (2^n)	Cantidad I/Os	Tiempo (milisegundos)");
		writer3.println("Tamano (2^n)	Cantidad promedio I/Os	d.e. I/Os	Tiempo promedio (milisegundos)	d.e. tiempo");
		writer4.println("Tamano (2^n)	Cantidad promedio I/Os	d.e. I/Os	Tiempo promedio (milisegundos)	d.e. tiempo");
		StopWatch timer1 = new StopWatch();
		StopWatch timer2 = new StopWatch();
		for(int i=9;i<=21;i+=3){
			IO_linear.clear();
			IO_quadratic.clear();
			time_quadratic.clear();
			time_linear.clear();
			n = (int)Math.pow(2,i);
			cnt1 = 0;
			cnt2 = 0;
			tree1 = new RTree(t);
			tree2 = new RTree(t);
			timer1.reset();
			timer2.reset();
			Rectangle r;
			for(int j=0;j<n;j++){
				r = randomRectangle();
				timer1.start();
				cnt1 += tree1.insert(r,true);
				timer1.pause();
				timer2.start();
				cnt2 += tree2.insert(r,false);
				timer2.pause();
			}
			writer1.println(i+"	"+cnt1+"	"+timer1.getElapsedTime());
			writer2.println(i+"	"+cnt2+"	"+timer2.getElapsedTime());
			System.out.println("para 1 2^"+i+": "+cnt1);
			System.out.println("para 2 2^"+i+": "+cnt2);
			timer1.reset();
			timer2.reset();
			// ahora se calcula tiempo y cantidad de I/Os de n/10 busquedas
			int c1 = 0, c2 = 0;
			for(int j=0;j<n/10;j++){
				r = randomRectangle();
				timer1.start();
				c1 = tree1.search(r,rec_lst);
				timer1.pause();
				rec_lst.clear();
				timer2.start();
				c2 = tree2.search(r,rec_lst);
				timer2.pause();
				IO_quadratic.add(c1);
				IO_linear.add(c2);
				time_quadratic.add(timer1.getElapsedTime());
				time_linear.add(timer2.getElapsedTime());
				timer1.reset();
				timer2.reset();
			}
			writer3.println(i+"	"+promedio(IO_quadratic)+"	"+desviacion_estandar(IO_quadratic)+"	"+promedio(time_quadratic)+"	"+desviacion_estandar(time_quadratic));
			writer4.println(i+"	"+promedio(IO_linear)+"	"+desviacion_estandar(IO_linear)+"	"+promedio(time_linear)+"	"+desviacion_estandar(time_linear));
		}
		writer1.close();
		writer2.close();
		writer3.close();
		writer4.close();
	}
	
	static double promedio(ArrayList<Number> lst){
		double suma = 0.0;
		for(Number n : lst)
			suma += n.doubleValue();
		return suma/lst.size();
	}
	
	static double desviacion_estandar(ArrayList<Number> lst){
		double result = 0.0;
		double promedio = promedio(lst);
		for(Number n : lst)
			result += Math.pow(promedio-n.doubleValue(),2);
		return Math.sqrt(result/lst.size());
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
