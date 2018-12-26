
public class Pkg {
	
	private DShapeModel model;
	private String command;
	
	/*
	 * XMLencoder looks at getter and setter. NAME MATTERS
	 * 
	 */
	public void setModel(DShapeModel model){
		this.model = model;
	}
	public void setCommand(String command){
		this.command = command;
	}
	
	public DShapeModel getModel(){
		return this.model;
	}
	
	public String getCommand(){
		return this.command;
	}
}
