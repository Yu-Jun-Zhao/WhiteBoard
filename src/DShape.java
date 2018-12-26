import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

// Controller
public class DShape implements ModelListener{
	
	protected DShapeModel model;
	public static final double KNOBSIZE = 9;
	
	public void draw(Graphics g){
		
	}
	
	public Bounds getBounds(){
		return new Bounds(model.getX(), model.getY(), model.getWidth(), model.getHeight());
	}
	
	@Override
	public void modelChanged(DShapeModel model) {
		this.model = model;
	}

	public LinkedList<Rectangle2D> getKnobs(){
		LinkedList<Rectangle2D> points = new LinkedList<>();
		Rectangle2D upLeft = new Rectangle2D.Double(
				(double)model.getX() - KNOBSIZE/2, (double)model.getY() - KNOBSIZE/2, KNOBSIZE, KNOBSIZE);
		Rectangle2D upRight = new Rectangle2D.Double(
				(double)model.getX() - KNOBSIZE/2 + model.getWidth(), (double)model.getY() - KNOBSIZE/2, KNOBSIZE, KNOBSIZE);
		Rectangle2D downLeft = new Rectangle2D.Double(
				(double)model.getX() - KNOBSIZE/2, (double)model.getY() - KNOBSIZE/2 + model.getHeight(), KNOBSIZE, KNOBSIZE);
		Rectangle2D downRight = new Rectangle2D.Double(
				(double)model.getX() - KNOBSIZE/2 + model.getWidth(), (double)model.getY() - KNOBSIZE/2 + model.getHeight(), KNOBSIZE, KNOBSIZE);


		points.add(upLeft);
		points.add(upRight);
		points.add(downLeft);
		points.add(downRight);

		return points;
	} 
	
	/*
	public void setModel(DShapeModel model){
		this.model = model;
	}
	*/
	public DShapeModel getModel(){
		return this.model;
	}
}


class Bounds{
	protected double x, y, width, height;
	public Bounds(double x, double y, double width, double height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
