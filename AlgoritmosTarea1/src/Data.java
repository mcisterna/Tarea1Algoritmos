import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class Data {
    public static DataStore dataStore;
    
    public static void main(String[] args) throws Exception {
    	//connect2(new ShapefileDataStoreFactory());
    	connect();
     	queryFeatures();
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

    
    private static void queryFeatures() throws Exception {
        
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
                 //System.out.println( feature.getDefaultGeometryProperty() );
                 //System.out.println( "------------" );
                 BoundingBox bounds = feature.getBounds();
                 System.out.println( feature.getBounds());
                 System.out.println( bounds.getMaxX()); // max coordenada en x
                 System.out.println( bounds.getMaxY()); // max coordenada en y
                 System.out.println( bounds.getMinX()); // min coordenada en x
                 System.out.println( bounds.getMinY()); // min coordenada en y
                 System.out.println( bounds.getHeight()); // obtengo la altura del rectangulo
                 System.out.println( bounds.getWidth()); // obtengo el ancho del rectangulo
                 System.out.println( "++++++++++" );
            }
        }
        finally {
            iterator.close();
        }
           
    }
    
    
	
	
	
	
	
	
	
	
	
	
	
}
