import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		RTree t = new RTree(1);
		Rectangle r1 = new Rectangle(0,2,0,2);
		Rectangle r2 = new Rectangle(1,3,1,3);
		Rectangle r3 = new Rectangle(0,1,0,1);
		Rectangle r4 = new Rectangle(7,8,7,8);
		Rectangle r5 = new Rectangle(100,102,100,103);
		Rectangle r6 = new Rectangle(2,4,2,4);
		t.insert(r1,true);
		t.insert(r2,true);
		t.insert(r3,true);
		t.insert(r4,true);
		t.insert(r5,true);
		t.insert(r6,true);
		ArrayList<Rectangle> lst = t.search(r3);
		for(Rectangle r : t.root.rectangles){
			System.out.println(r);
			System.out.println("HIJOS: \n");
			for(Rectangle rr : r.node.rectangles){
				System.out.println(rr);
			}
			System.out.println("---------------------");
		}

	}

}
