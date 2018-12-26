import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;

public class DRect extends DShape{
	
	
	public DRect(){
		
	}
	
	// VIEW
	public void draw(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(model.getColor());
		g2.fillRect(model.getX(), model.getY(), model.getWidth(), model.getHeight());
		g2.setColor(Color.black);

		
	}
	
}
