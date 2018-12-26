import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;


public class DLine extends DShape{
	
	public DLine(){
		
	}
	
	public void draw(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(model.getColor());
		g2.drawLine(model.getX(), model.getY(), model.getX2(), model.getY2());
		g2.setColor(Color.black);

	}
	
	@Override
	public LinkedList<Rectangle2D> getKnobs(){
		LinkedList<Rectangle2D> points = new LinkedList<>();
		Rectangle2D knob1 = new Rectangle2D.Double(
				(double)model.getX() - KNOBSIZE/2, (double)model.getY() - KNOBSIZE/2, KNOBSIZE, KNOBSIZE);
		
		Rectangle2D knob2 = new Rectangle2D.Double(
				(double)model.getX2() - KNOBSIZE/2 , (double)model.getY2() - KNOBSIZE/2 , KNOBSIZE, KNOBSIZE);


		points.add(knob1);
		
		points.add(knob2);

		return points;
	} 
	
	@Override
	public Bounds getBounds(){
		// compute the upper left hand corner
		int leftX = model.getX() <= model.getX2() ? model.getX() : model.getX2();
		int leftY = model.getY() <= model.getY2() ? model.getY() : model.getY2();
		
		
		return new Bounds(leftX, leftY, model.getWidth(), model.getHeight());
	}
	
	
	@Override
	public void modelChanged(DShapeModel model) {
		this.model = (DLineModel)model;
	}
}
