import java.awt.Point;
import java.awt.geom.Point2D;


public class DLineModel extends DShapeModel{
	
	
	private int p1x, p1y, p2x, p2y;
	
	public DLineModel(){
		this.setX(10);
		this.setY(10);
		this.setX2(30);
		this.setY2(30);
		this.setWidth(Math.abs(p2x-p1x));
		this.setHeight(Math.abs(p2y-p1y));

	}
	
	// for dragging
	@Override
	public void setnewXY(int xdelta, int ydelta){
		this.p1x = this.p1x + xdelta;
		this.p1y = this.p1y + ydelta;
		this.p2x = this.p2x + xdelta;
		this.p2y = this.p2y + ydelta;
		
		if(WhiteBoard.Status.equals("Server")){
			Server.update("CHANGE", this);
		}
		
		notifyListeners();

	}
	
	// For simply overriding the method, i would just keep the xdelta ... to height.
	@Override
	public void setnewBounds(int xdelta, int ydelta, int width, int height, Point2D anchorP, Point e){
	
		this.p1x = (int) anchorP.getX();
		this.p1y = (int) anchorP.getY();
		this.p2x = (int) e.getX();
		this.p2y = (int) e.getY();
		this.setWidth(width);
		this.setHeight(height);
		
		if(WhiteBoard.Status.equals("Server")){
			Server.update("CHANGE", this);
		}
		
		notifyListeners();
	}

	
	public int getX(){
		return p1x;
	
	}
	
	public int getY(){
		return p1y;
	
	}

	
	@Override
	public int getX2(){
		return p2x;
		
	}	
	@Override
	public int getY2(){
		return p2y;
	}

	@Override 
	public void setX(int x){
		this.p1x = x;
	}
	@Override 
	public void setY(int y){
		this.p1y = y;
	}
	public void setX2(int x){
		this.p2x = x;
	}
	
	public void setY2(int y){
		this.p2y = y;

	}
	
}
