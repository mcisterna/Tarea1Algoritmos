import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.swing.data.JDataStoreWizard;
import org.geotools.swing.wizard.JWizard;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.geometry.BoundingBox;

/*Clase que obtiene los datos necesarios para hacer el analisis sobre los datos reales
 * 
 * 
 */


public class Data {
    public static DataStore dataStore;
    public static int count;
    
    /*Metodo main que ejectuda el codigo y retorna 4 archivos .txt con los datos necesarios
     * 
     */
    public static void main(String[] args) throws Exception {
    	//connect2(new ShapefileDataStoreFactory()); // funcion que hace conexion con el archivo .shp pero este 
    												 // desplega venta la cual uno puede elegir arbitrariamente cual cargar
    	connect(); // funcion que hace conexion con el archivo .shp
     	getCount(); // obtiene la cantidad de datos disponibles
	
     	System.out.println("count:" +count);
     	int t = getT();
		RTree tree1, tree2;
		int cnt1, cnt2;
		PrintWriter writer1 = new PrintWriter("insertion_quadratic_data_real.txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter("insertion_linear_data_real.txt", "UTF-8");
		PrintWriter writer3 = new PrintWriter("search_quadratic_data_real.txt", "UTF-8");
		PrintWriter writer4 = new PrintWriter("search_linear_data_real.txt", "UTF-8");
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
			IO_linear.clear();
			IO_quadratic.clear();
			time_quadratic.clear();
			time_linear.clear();
			cnt1 = 0;
			cnt2 = 0;
			tree1 = new RTree(t);
			tree2 = new RTree(t);
			timer1.reset();
			timer2.reset();
			Rectangle r;
			
			///////////////////////////////Datos reales////////////////////////////////////
			String typeName = dataStore.getTypeNames()[0];
	        SimpleFeatureSource source = dataStore.getFeatureSource(typeName);

	        FeatureType schema = source.getSchema();
	        String name = schema.getGeometryDescriptor().getLocalName();

	        Filter filter = CQL.toFilter("include");

	        Query query = new Query(typeName, filter, new String[] { name });

	        SimpleFeatureCollection features = source.getFeatures(query);

	        SimpleFeatureIterator iterator = features.features();
	        try {
	            while( iterator.hasNext() ){
	                 SimpleFeature feature = iterator.next();
	                 BoundingBox bounds = feature.getBounds();
	                 
	                 {
	     				r = new Rectangle(bounds.getMinX(), bounds.getMaxX(),
	     								  bounds.getMinY(), bounds.getMaxY());
	     				timer1.start();
	     				cnt1 += tree1.insert(r,true);// quadratic split
	     				timer1.pause();
	     				timer2.start();
	     				cnt2 += tree2.insert(r,false); // linear split
	     				timer2.pause();
	     			}
	            }
	        }
	        finally {
	            iterator.close();
	        }
	        
	        
	        ///////////////////////////////Datos reales////////////////////////////////////
			
			
			
			writer1.println("	"+cnt1+"	"+timer1.getElapsedTime());
			writer2.println("	"+cnt2+"	"+timer2.getElapsedTime());
			System.out.println("para 1 "+": "+cnt1);
			System.out.println("para 2 "+": "+cnt2);
			timer1.reset();
			timer2.reset();
			// ahora se calcula tiempo y cantidad de I/Os de n/10 busquedas
			int c1 = 0, c2 = 0;
			for(int j=0;j<count/10;j++){
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
			writer3.println("	"+promedio(IO_quadratic)+"	"+desviacion_estandar(IO_quadratic)+"	"+promedio(time_quadratic)+"	"+desviacion_estandar(time_quadratic));
			writer4.println("	"+promedio(IO_linear)+"	"+desviacion_estandar(IO_linear)+"	"+promedio(time_linear)+"	"+desviacion_estandar(time_linear));
		
		writer1.close();
		writer2.close();
		writer3.close();
		writer4.close();
		tree1 = new RTree(t);
     	
     	
     	
    }

    private static void connect () throws IOException
    {
    	File file = new File("AlgoritmosTarea1/maps/tl_2011_06_prisecroads.shp");
    	Map map = new HashMap();
    	map.put( "url", file.toURL() );
    	dataStore = DataStoreFinder.getDataStore( map );
    }
    
    private static void connect2(DataStoreFactorySpi format) throws Exception {
        JDataStoreWizard wizard = new JDataStoreWizard(format);
        int result = wizard.showModalDialog();
        if (result == JWizard.FINISH) {
            Map<String, Object> connectionParameters = wizard.getConnectionParameters();
            dataStore = DataStoreFinder.getDataStore(connectionParameters);
            if (dataStore == null) {
                JOptionPane.showMessageDialog(null, "Could not connect - check parameters");
            }
        }
    }

private static Rectangle randomRectangle() throws Exception {
        
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
        Rectangle r = null;
        FeatureType schema = source.getSchema();
        String name = schema.getGeometryDescriptor().getLocalName();

        Filter filter = CQL.toFilter("include");

        Query query = new Query(typeName, filter, new String[] { name });

        SimpleFeatureCollection features = source.getFeatures(query);
        Random rand = new Random();

        int  randomNum = rand.nextInt(count) + 1;
        int count2 = 0;
        SimpleFeatureIterator iterator = features.features();
        try {
            while( iterator.hasNext() ){
            	 SimpleFeature feature = iterator.next();
                 BoundingBox bounds = feature.getBounds();
  				 r = new Rectangle(bounds.getMinX(), bounds.getMaxX(),
						  bounds.getMinY(), bounds.getMaxY());
  				 count2++;
  				 if (count2 == randomNum)
  				 {
  					 break;
  				 }
            }
        }
        finally {
            iterator.close();
        }
        
        return r;
    }
    
    private static void getCount() throws Exception {
        
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource source = dataStore.getFeatureSource(typeName);

        FeatureType schema = source.getSchema();
        String name = schema.getGeometryDescriptor().getLocalName();

        Filter filter = CQL.toFilter("include");

        Query query = new Query(typeName, filter, new String[] { name });

        SimpleFeatureCollection features = source.getFeatures(query);

        SimpleFeatureIterator iterator = features.features();
        try {
            while( iterator.hasNext() ){
                 SimpleFeature feature = iterator.next();
                 count++;
            }
        }
        finally {
            iterator.close();
        }
           
    }
    
	static public int getT(){
		// Tamaño de bloque = 4 KB
		int block_sz = 4096;
		// rectangulos para calcular tamaño de nodo
		Rectangle r0 = new Rectangle(0.0,4.0,3.0,4.0);
		Rectangle r1 = new Rectangle(1.0,2.0,3.0,4.0);
		Rectangle r2 = new Rectangle(1.0,2.0,3.0,4.0);
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
	
	
	
	
	
	
	
	
	
	
	
}
