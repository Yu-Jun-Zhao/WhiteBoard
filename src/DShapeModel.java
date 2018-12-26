import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class DShapeModel {
	
	private LinkedList<ModelListener> listenersList;
	
	
	private int x, y, width, height;
	private Color color;
	private Integer id;
	
	
	public DShapeModel(){
		this.color = Color.GRAY;
		listenersList = new LinkedList<>();
	}
	
	public void setColor(Color color){
		this.color = color;
		for(ModelListener listener : listenersList){
			listener.modelChanged(this);
		}
	}
	
	public void addToList(ModelListener listener){
		this.listenersList.add(listener);
		for(ModelListener l : listenersList){
			l.modelChanged(this);
		}
	}
	public void removeListener(ModelListener listener){
		this.listenersList.remove(listener);
		for(ModelListener l : listenersList){
			l.modelChanged(this);
		}
	}
	
	// for dragging
	public void setnewXY(int xdelta, int ydelta){
		this.x = this.x + xdelta;
		this.y = this.y + ydelta;
		if(WhiteBoard.Status.equals("Server")){
			Server.update("CHANGE", this);
		}
		notifyListeners();

	}
	
	// for removing
	public void setnewBounds(int xdelta, int ydelta, int width, int height, Point2D anchorP, Point e){
	
		
		// if the mouse is below the anchor 
		// the changing element would be the width and height.
		if(e.x >= anchorP.getX())
			this.x = (int) (anchorP.getX());
		if(e.y >= anchorP.getY())
			this.y = (int) (anchorP.getY());
		
		// if the mouse is above the anchor 
		// the changing element would be the x,y,width,and height
		if(e.x < anchorP.getX())
			this.x = this.x + xdelta;
		if(e.y < anchorP.getY())
			this.y = this.y + ydelta;
		
		this.width = width;
		this.height = height;
		
		if(WhiteBoard.Status.equals("Server")){
			Server.update("CHANGE", this);
		}
		
		notifyListeners();
		
		
	}
	
	protected void notifyListeners(){
		for(ModelListener l : listenersList){
			l.modelChanged(this);
		}
	}
	
	public void mimic(DShapeModel other){
		this.setColor(other.getColor());
		if(other.getId()==1){
			// DLine
		}
		else{
			this.setnewXY(other.getX(), other.getY());
			// setNewBound
		}
	}
	
	// regular setter
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
	//public void setX2(int x){this.x = x;}
	//public void setY2(int y){this.y = y;}

	public void setWidth(int width){this.width = width;}
	public void setHeight(int height){this.height = height;}
	public void setId(Integer id){this.id = id;}

	// getter
	public int getX(){return this.x;}
	public int getY(){return this.y;}
	public int getWidth(){return this.width;}
	public int getHeight(){return this.height;}
	public Integer getId(){return this.id;}
	public Color getColor(){return this.color;}
	
	// only use for DLINE
	public int getX2(){return this.x;} 
	public int getY2(){return this.y;}
}
