import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;


public class DText extends DShape{
	
	
	
	public void draw(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(model.getColor());
		Shape clip = g2.getClip();
		if(clip != null)
			g.setClip(clip.getBounds().createIntersection(
				new Rectangle2D.Double(model.getX(), model.getY(), model.getWidth(), model.getHeight())));
		computeFont();
		g2.setFont(((DTextModel)model).getFont());
		g2.drawString(((DTextModel)model).getText(), model.getX(), (int) (model.getY()+model.getHeight()*.85));
		g.setClip(clip);
		g2.setColor(Color.black);
	
	}
	
	public void computeFont(){
		
		if(((DTextModel)model).getFontSize() < model.getHeight()){
				float size = ((DTextModel)model).getFontSize();
				((DTextModel)model).setFontSize((size*1.10f)+1f);
				
		}else{
			
			
			((DTextModel)model).setFontSize((model.getHeight()));
			
			
		}
		
		
	}
}
