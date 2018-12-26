import java.awt.Color;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JPanel;


public class Canvas extends JPanel implements ModelListener{
	
	static int IDCounter = 1 ;
	private LinkedList<DShape> shapeList;
	static HashMap<Integer,DShape> shapeMap = new HashMap<Integer,DShape>();
	static HashMap<Integer, DShapeModel> modelMap = new HashMap<Integer, DShapeModel>();
	static BufferedImage paintImage;
	private DShape pointer;
	//public static final double KNOBSIZE = 9;
	private Point2D anchorPoint;
	private Rectangle2D movingKnob;
	private WhiteBoard whiteBoard;
	private WhiteBoardTableModel tableModel;
	
	// basically the old x and y
	private int mousePressedx; 
	private int mousePressedy;
	
	private boolean gotKnob;
	
	
	
	public Canvas(WhiteBoard board){
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(400,400));
		this.shapeList = new LinkedList<>();
		this.whiteBoard = board;
		
		// initialize mouse Listener
		initMouseListener();
		
	}
	
	public void addShape(DShapeModel shapeModel){
		if(shapeModel instanceof DRectModel){
			DRect rect = new DRect();
			shapeModel.addToList(rect);
			shapeModel.addToList(this);
			shapeModel.addToList(this.tableModel);
			shapeList.add(rect);
			shapeMap.put(IDCounter,rect);
			modelMap.put(IDCounter, shapeModel);
			shapeModel.setId(IDCounter);
			IDCounter++;
			changePointer(rect);
		}else if(shapeModel instanceof DOvalModel){
			DOval oval = new DOval();
			shapeModel.addToList(oval);
			shapeModel.addToList(this);
			shapeModel.addToList(this.tableModel);
			shapeList.add(oval);
			shapeMap.put(IDCounter,oval);
			modelMap.put(IDCounter, shapeModel);
			shapeModel.setId(IDCounter);
			IDCounter++;
			changePointer(oval);
		}else if(shapeModel instanceof DLineModel){
			DLine line = new DLine();
			shapeModel.addToList(line);
			shapeModel.addToList(this);
			shapeModel.addToList(this.tableModel);
			shapeList.add(line);
			shapeMap.put(IDCounter,line);
			modelMap.put(IDCounter, shapeModel);
			shapeModel.setId(IDCounter);
			IDCounter++;
			changePointer(line);
			
		}else if(shapeModel instanceof DTextModel){
			DText text = new DText();
			shapeModel.addToList(text);
			shapeModel.addToList(this);
			shapeModel.addToList(this.tableModel);
			shapeList.add(text);
			shapeMap.put(IDCounter, text);
			modelMap.put(IDCounter, shapeModel);
			shapeModel.setId(IDCounter);
			IDCounter++;
			changePointer(text);
		}
		
		if(WhiteBoard.Status.equals("Server")){
			Server.update("ADD", shapeModel);
			//System.out.println("here");
		}
		
	}
	
	public void pointerTextChanged(String text){
		// pointer check
		if(this.pointer != null){
			DTextModel model = (DTextModel) this.pointer.model;
			model.setText(text);
			if(WhiteBoard.Status.equals("Server")){
				Server.update("CHANGE", this.pointer.model);
			}
		
		}
		
	}
	
	public void pointerFontChanged(String text){
		// pointer check
		if(this.pointer != null){
			DTextModel model = (DTextModel) this.pointer.model;
			model.setFont(text);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for(DShape shape : shapeList){
			shape.draw(g);	
		}
		if(this.pointer != null){
			LinkedList<Rectangle2D> points = this.pointer.getKnobs();
			
			g2.fill(points.get(0));
			g2.fill(points.get(1));
			if(!(this.pointer instanceof DLine)){
				g2.fill(points.get(2));
				g2.fill(points.get(3));
			}
		}
	}
	
	public void drawPNG(){
		Rectangle r = this.getBounds();
		int h = r.height;
		int w = r.width;
		paintImage = new BufferedImage(h,w,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graph = paintImage.createGraphics();
		graph.setColor(Color.WHITE);
		graph.fillRect(0,0,h,w);
		for(DShape shape : shapeList){
			shape.draw(graph);
		}
		graph.dispose();
	}
	
	public void LoadNewData(HashMap<Integer, DShapeModel> temp) {
		this.shapeList.clear();
		modelMap = new HashMap<Integer, DShapeModel>();
		for(DShapeModel model : temp.values()) {
			this.addShape(model);
		}
	}
	
	private void initMouseListener(){
		MouseListeners listeners = new MouseListeners();   
		this.addMouseListener(listeners); //for mouse pressed
		this.addMouseMotionListener(listeners); // for mouse dragged
			
	}
	// for changing pointer and colors
	private void changePointer(DShape newShape){
		
		if(newShape == null){
			if(pointer != null){
				this.pointer = null;
				this.repaint();

			}
			//disabling textField if pointer == null	
			this.whiteBoard.setTextField("");
			this.whiteBoard.enableTextField(false);
			return;
		}
		
		this.pointer = newShape;
		if(this.pointer instanceof DText){
			this.whiteBoard.enableTextField(true);
			this.whiteBoard.setTextField(((DTextModel)this.pointer.model).getText());
		}else{
			this.whiteBoard.enableTextField(false);
		}
		this.repaint();
	}
	
	//changing the pointer color
	public void changePointerColor(Color color){
		this.pointer.model.setColor(color);
		if(WhiteBoard.Status.equals("Server")){
			Server.update("CHANGE", this.pointer.model);
		}
	}
	
	// for checking bounds
	private boolean compareTwoPoints(Point e, Bounds b){
		
		// in the fourth quadrant
		if(e.getX() >= b.x && e.getY() >= b.y){
			
			double widthCord = b.x + b.width;
			double heightCord = b.y + b.height;
			if(e.getX() <= widthCord && e.getY() <= heightCord){
				return true;
			}
		}
		return false;
	}
	
	public void removeShape(){
		if(WhiteBoard.Status.equals("Server")){
			Server.update("REMOVE", this.pointer.model);
		}
		
		if(this.pointer != null){
			this.pointer.model.removeListener(this.pointer);
			this.pointer.model.removeListener(this);
			this.shapeList.remove(this.pointer);
			shapeMap.remove(this.pointer.model.getId());
			modelMap.remove(this.pointer.model.getId());
			this.pointer = null;
			//this.repaint();
		}
		
	}
	
	public void removeByModel(DShapeModel model){
		DShape shape = shapeMap.get(model.getId());
		
		this.shapeList.remove(shape);
		this.pointer = null;
		this.repaint();
		
	}
	
	public void removeAll(){
		for(DShape s : shapeList){
			this.shapeList.remove(s);			
		}
		this.repaint();
	}
	
	public DShape getPointer(){
		return this.pointer;
	}

	@Override
	public void modelChanged(DShapeModel model) {
		this.repaint();
	}
	
	//method to find the anchor knob
	private void setAnchorKnob(LinkedList<Rectangle2D> knobs, int movingKnobIndex){
		if((this.pointer instanceof DLine)){
			switch(movingKnobIndex){
			case 0: anchorPoint = new Point2D.Double(knobs.get(1).getCenterX(),
					knobs.get(1).getCenterY());
					break; 
			case 1: anchorPoint = new Point2D.Double(knobs.get(0).getCenterX(),
					knobs.get(0).getCenterY());
					break;  
			default: break;
			}	
		}
		else{
			switch(movingKnobIndex){
			case 0: anchorPoint = new Point2D.Double(knobs.get(3).getCenterX(),// upLeft to downRight
					knobs.get(3).getCenterY());
					break; 
			case 1: anchorPoint = new Point2D.Double(knobs.get(2).getCenterX(),
					knobs.get(2).getCenterY());
					break;  
			case 2: anchorPoint = new Point2D.Double(knobs.get(1).getCenterX(),// upLeft to downRight
					knobs.get(1).getCenterY());
					break; 
			case 3: anchorPoint = new Point2D.Double(knobs.get(0).getCenterX(),// upLeft to downRight
					knobs.get(0).getCenterY());
					break; 
			default: break;
			}
		}
	}
	
	//moving shape front
	public void moveToFront(){
		this.shapeList.remove(this.pointer);
		this.shapeList.add(this.pointer);
	
		this.tableModel.modelChanged(null); // break listener ??

		this.repaint();
		if(WhiteBoard.Status.equals("Server")){
			Server.update("FRONT", this.pointer.model);
		}
		
	}
	
	public void moveToFrontbyId(DShapeModel model){
		DShape temp = shapeMap.get(model.getId());
		this.shapeList.remove(temp);
		this.shapeList.add(temp);
		this.tableModel.modelChanged(null);
		this.repaint();
	}
	
	//moving shape back
	public void moveToBack(){
		this.shapeList.remove(this.pointer);
		this.shapeList.addFirst(this.pointer);
		
		this.tableModel.modelChanged(null); // break listener model??
		
		this.repaint();
		if(WhiteBoard.Status.equals("Server")){
			Server.update("BACK", this.pointer.model);
		}
	}
	
	public void moveToBackbyId(DShapeModel model){
		DShape temp = shapeMap.get(model.getId());
		this.shapeList.remove(temp); 
		this.shapeList.addFirst(temp);
		this.tableModel.modelChanged(null);
		this.repaint();
	}
	
	public void movebyId(DShapeModel model){
		
		DShape temp = shapeMap.get(model.getId());
			//temp.model = model;
		if(temp != null){
			/*
			if(temp instanceof DLine){
				temp.model = (DLineModel)model;

			}else{
				temp.model = model;

			}
			*/
			temp.model = model;

				
		}
		this.tableModel.modelChanged(null);
		this.repaint();
			
		
	}
	
	public LinkedList<DShape> getShapeList(){
		return this.shapeList;
	}
	
	public void setTableModel(WhiteBoardTableModel model){
		this.tableModel = model;
	}
	
	
	private class MouseListeners extends MouseAdapter {
	    @Override
	    public void mousePressed(MouseEvent e){
	    	
	    	if(pointer != null){
				LinkedList<Rectangle2D> knobs = pointer.getKnobs();
				for(int j = 0; j < knobs.size(); j++){
					if(compareTwoPoints(e.getPoint(),
							new Bounds(knobs.get(j).getX(), //creating a new bound to check knobs
									knobs.get(j).getY(),
									DShape.KNOBSIZE,DShape.KNOBSIZE))){
						mousePressedx = e.getX();
						mousePressedy = e.getY();
						// setting moving knob and anchor knob
						movingKnob = knobs.get(j);
						setAnchorKnob(knobs, j);
						gotKnob = true;
						
						return; 
					}
				}
			}
	    	gotKnob = false;
	    	
	    	//only for changing pointer
	    	int i = shapeList.size()-1;
	    	
			for(; i >= 0 ; i--){
				
				if(compareTwoPoints(e.getPoint(), shapeList.get(i).getBounds())){
					mousePressedx = e.getX();
					mousePressedy = e.getY();
					changePointer(shapeList.get(i));						
					
					break;
				}
			}
			
			if(i == -1){
				changePointer(null);
			}
		}

	    @Override
	    public void mouseDragged(MouseEvent e){
	    	
	    	
	    	if(getPointer() != null){ 
	    		// if knob is selected, dont drag
	    		if(gotKnob){
	    			int xDelta = e.getX() - mousePressedx; //change in x direction
	    			int yDelta = e.getY() - mousePressedy; // changes in the y direction
	    			
	    			int newWidth = (int) Math.abs((anchorPoint.getX() - e.getX()));
	    			int newHeight =  (int) Math.abs(anchorPoint.getY() - e.getY());
	    			
	    			mousePressedx = e.getX(); 
	    			mousePressedy = e.getY();
	    			
	    			getPointer().model.setnewBounds(xDelta, yDelta, newWidth, newHeight, anchorPoint, e.getPoint());
	    		}else{
	    			int xDelta;
		    		int yDelta;
		    		if(e.getX() != mousePressedx || e.getY() != mousePressedy){
		    			xDelta = e.getX() - mousePressedx; // for dragging
		    			yDelta = e.getY() - mousePressedy;
		    			mousePressedx = e.getX();
		    			mousePressedy = e.getY();
			    		getPointer().model.setnewXY(xDelta, yDelta);
		    		}
		    		
		    		
	    		}
	    		
	    		
	    	}
	    	
	    }
	}
	
	
	
}
