import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
    public static JComboBox featureTypeCBox;
    public static JTable table;
    public static JTextField text;
    
    
    public static void main(String[] args) throws Exception {
    	connect(new ShapefileDataStoreFactory());
     	queryFeatures();
    }

    
    private static void connect(DataStoreFactorySpi format) throws Exception {
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
        
        String typeName = "tl_2011_06_prisecroads";
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
