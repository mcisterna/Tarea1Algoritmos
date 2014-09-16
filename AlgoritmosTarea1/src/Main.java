import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		
		RandomData random = new RandomData();
		
		System.out.println(random.generateRandom((int) Math.pow(2, 24)));
		
//		RTree t = new RTree(1);
//		Rectangle r1 = new Rectangle(0,2,0,2);
//		Rectangle r2 = new Rectangle(1,3,1,3);
//		Rectangle r3 = new Rectangle(0,1,0,1);
//		Rectangle r4 = new Rectangle(7,8,7,8);
//		Rectangle r5 = new Rectangle(100,102,100,103);
//		Rectangle r6 = new Rectangle(2,4,2,4);
//		boolean var = false;
//		t.insert(r1,var);
//		t.insert(r2,var);
//		t.insert(r3,var);
//		t.insert(r4,var);
//		t.insert(r5,var);
//		t.insert(r6,var);
//		ArrayList<Rectangle> lst = t.search(r3);
//		for(Rectangle r : t.root[0].rectangles){
//			System.out.println(r);
//			System.out.println("HIJOS: \n");
//			if(r.node != null)
//			for(Rectangle rr : r.node.rectangles){
//				System.out.println(rr);
//			}
//			System.out.println("---------------------");
//		}

	}

}
