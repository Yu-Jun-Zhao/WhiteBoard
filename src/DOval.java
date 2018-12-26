import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class DOval extends DShape{

	
	public DOval(){
		
	}
	
	public void draw(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(model.getColor());
		g2.fillOval(model.getX(), model.getY(), model.getWidth(), model.getHeight());
		g2.setColor(Color.black);

		
	}
}
