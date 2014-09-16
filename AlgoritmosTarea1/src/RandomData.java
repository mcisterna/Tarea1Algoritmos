import java.util.ArrayList;
import java.util.Random;


public class RandomData {

	static ArrayList<Rectangle> lst;
	 
	public RandomData()
	{
		lst = new ArrayList<Rectangle>();
	}
	
	public int generateRandom(int n)
	{
		Random random = new Random();
		
		for(int i = 0; i<n; i++ )
		{
	        int x1 = random.nextInt(500001);
	        int x2 = random.nextInt(500001);
	        int y1 = random.nextInt(500001);
	        int y2 = random.nextInt(500001);
	        Rectangle r1 = new Rectangle(x1,x2,y1,y2);
	        lst.add(r1);
		}


        return         lst.size();
	}
	
}
