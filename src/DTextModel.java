import java.awt.Font;


public class DTextModel extends DShapeModel{
	
	private float fontSize;
	private Font font; 
	private String text;
	
	public DTextModel(){
		this.setX(10);
		this.setY(10);
		this.setWidth(20);
		this.setHeight(20);
		
		this.fontSize = 30f;
		this.font = new Font("Dialog", Font.PLAIN, (int) this.fontSize);
		
		this.text = new String("hello");
	}
	
	// notify listeners when text is changed
	public void setText(String text){
		this.text = text;
		notifyListeners();
	}
	public void setFont(String font){
		this.font = new Font(font, Font.PLAIN, (int) this.fontSize);
		
		notifyListeners();

	}

	
	public void setFontSize(float newSize){
		this.fontSize = newSize;
		this.font = this.font.deriveFont(newSize);
		notifyListeners();
	}
	

	public String getText(){
		return this.text;
	}
	public Font getFont(){
		return this.font;
	}
	public float getFontSize(){
		return this.fontSize;
	}
}
