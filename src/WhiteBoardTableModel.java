import java.util.LinkedList;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;


public class WhiteBoardTableModel extends AbstractTableModel implements ModelListener{

	
	private LinkedList<DShape> shapeList;
	
	public WhiteBoardTableModel(Canvas canvas){
		this.shapeList = canvas.getShapeList();
	}
	
	@Override
	public int getRowCount() {
		return shapeList.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		DShape shape = shapeList.get(rowIndex);
		
		switch(columnIndex){
		case 0: return shape.model.getX(); // x
		case 1: return shape.model.getY(); // y
		case 2: return shape.model.getWidth();
		case 3: return shape.model.getHeight();
		default: break;
		}
		
		return null;
	}

	@Override
	public void modelChanged(DShapeModel model) {

		for(int i = 0; i < shapeList.size(); i++){
			if(model != null && model.equals(shapeList.get(i).model)){
				// feel like it doesnt this method
				fireTableRowsUpdated(i, i);
				
			}
		}
		fireTableChanged(new TableModelEvent(this));
	}

}
