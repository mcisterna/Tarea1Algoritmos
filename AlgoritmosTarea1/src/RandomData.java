import java.util.ArrayList;
import java.util.Random;


public class RandomData {

	static ArrayList<Rectangle> lst;
	int a[];
	Rectangle r1;
	double recSize;
	
	public RandomData()
	{
		lst = new ArrayList<Rectangle>();
	}
	
	public ArrayList<Rectangle> generateRandom(int n)
	{
		Random random = new Random();
		
		double size = 100.0;

		
		for(int i = 0; i<n; i++ )
		{

			
			recSize = 101.0;
			
			while (size<recSize || recSize <= 0)
			{
			
		        int x1 = random.nextInt(500001);
		        
		        int x2 = random.nextInt(500001);
		        
		        int y1 = random.nextInt(500001);
		        int y2 = random.nextInt(500001);
		        r1 = new Rectangle(x1,x2,y1,y2);
		        recSize = r1.area();
		        
		        System.out.println("x1: " + x1);
		        System.out.println("x2: " + x2);
		        System.out.println("y1: " + y1);
		        System.out.println("y2: " + y2);
		        System.out.println("recSize: " + recSize);
		        System.out.println("add: " + i);
			}
	        
	        lst.add(r1);
		}

		//return lst.size();
        return lst;
	}

	public double generateRandom2(int n)
	{
		Random random = new Random();
		
		double x1,x2,y1,y2;
		
        x1 = random.nextInt(500001);
        x2 = x1 + random.nextInt(101);
        
       	System.out.println("x1-x2: " + (x2-x1));

        y1 = random.nextInt(500001);
        
        y2 = y1 + random.d(101)/(x2-x1) ;
         
    	System.out.println("y1-y2: " + (y2-y1));

     
        r1 = new Rectangle(x1,x2,y1,y2);
        recSize = r1.area();
        
        return recSize;
		
	}
}
