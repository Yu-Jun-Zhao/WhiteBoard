import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

// Yu Jun Zhao

// author: Yu Jun Zhao, Zhonglin Liang

// Completed on 12/19/2017


public class WhiteBoard extends JFrame{

	static WhiteBoard frame;
	static String Status = "Normal";
	Canvas canvas;
	JPanel controlPanel, contPart1;
	JScrollPane contPart2;
	
	//panels for storing the widgets
	JPanel shapeGroup, textGroup, colorGroup, 
		editGroup, saveGroup, networkGroup;
	
	// widgets for shapeGroup
	JLabel shapeLabel;
	JButton rect, oval, line, text;
	
	// widgets for textGroup
	JLabel textLabel;
	JTextField textField;
	JComboBox<String> fontList;
	
	// widgets for colorGroup
	JLabel colorLabel;
	JButton colorButton;
	
	// widgets for editGroup
	JLabel editLabel;
	JButton frontButton, backButton, removeButton; 
	
	// widgets for saveGroup
	JLabel saveLabel;
	JButton saveButton, loadButton, asPNGButton;
	
	// widgets for networkGroup
	JLabel networkLabel;
	JLabel statusLabel;
	JButton serverButton, clientButton;
	
	// table widget
	JTable table;
	
	private static final int CONTROLSWIDTH = 550;
	private static final int CONTROLSMAXHEIGHT = 20;
	private static final int TABLEPHEIGHT = 300;
	public WhiteBoard(){
		this.setTitle("WhiteBoard");
		
		this.canvas = new Canvas(this);
		
		// layout for the entire Frame
		this.setLayout(new BorderLayout());
		this.add(this.canvas, BorderLayout.CENTER);
		
		// create JPanel to contain the UI
		this.controlPanel = new JPanel();
		BoxLayout bl = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
		this.controlPanel.setLayout(bl);

		this.contPart1 = new JPanel();
		BoxLayout bl1 = new BoxLayout(contPart1, BoxLayout.Y_AXIS);
		this.contPart1.setLayout(bl1);
		this.contPart1.setMaximumSize(new Dimension(CONTROLSWIDTH,CONTROLSMAXHEIGHT));
		
		// scrollpane that contains a table
		this.contPart2 = new JScrollPane();
		this.contPart2.setPreferredSize(new Dimension(CONTROLSWIDTH, TABLEPHEIGHT));
		
		// create UI buttons
		initiateShapeGroupUI();
		initiateTextGroup();
		initiateShapeGroup();
		initiateEditGroup();
		initiateSaveGroup();
		initiateNetworkGroup();
		initiateTable();

		addListenersToWidgets();
		
		this.controlPanel.add(contPart1);
		this.controlPanel.add(contPart2);
		this.add(controlPanel, BorderLayout.WEST);		
		
		this.pack();		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}
	
	
	// initiate shapeGroup UI and add to controlPanel
	private void initiateShapeGroupUI(){
		this.shapeGroup = new JPanel(getFL());
		shapeLabel = new JLabel("Add: ");
		rect = new JButton("Rect");
		oval = new JButton("Oval");
		line = new JButton("Line");
		text = new JButton("Text");
		this.shapeGroup.add(shapeLabel);
		this.shapeGroup.add(rect);
		this.shapeGroup.add(oval);
		this.shapeGroup.add(line);
		this.shapeGroup.add(text);
		
		//add shapegroup to controlpanel
		this.contPart1.add(this.shapeGroup);

	}
	// initiate textGroup UI and add to control Panel
	private void initiateTextGroup(){
		this.textGroup = new JPanel(getFL());
		textLabel = new JLabel("Edit Text: ");
		textField = new JTextField();
		textField.setColumns(20);
		String str[] = getSupportedFonts();
		fontList = new JComboBox<String>(str);	
		for(int i = 0; i < str.length; i++){
			if(str[i].equals("Dialog")) //then there exists dialog
			fontList.setSelectedItem("Dialog");
		}
		
		enableTextField(false);
		
		this.textGroup.add(textLabel);
		this.textGroup.add(textField);
		this.textGroup.add(fontList);
		
		this.contPart1.add(this.textGroup);
	}
	
	// initiate ShapeGroup UI and add to control panel
	private void initiateShapeGroup(){
		this.shapeGroup = new JPanel(getFL());

		shapeLabel = new JLabel("Set Shape Color: ");
		colorButton = new JButton("Color");
		
		this.shapeGroup.add(shapeLabel);
		this.shapeGroup.add(colorButton);
		
		this.contPart1.add(this.shapeGroup);
	}
	
	// initiate EditGroup UI and add to control panel
	private void initiateEditGroup(){
		this.editGroup = new JPanel(getFL());
		
		editLabel = new JLabel("Edit Shapes: ");
		frontButton = new JButton("Send to Front");
		backButton = new JButton("Send to Back");
		removeButton = new JButton("Remove Shape");
		
		this.editGroup.add(editLabel);
		this.editGroup.add(frontButton);
		this.editGroup.add(backButton);
		this.editGroup.add(removeButton);

		
		this.contPart1.add(this.editGroup);
		
	}
	// initiate SaveGroup UI and add to control panel
	private void initiateSaveGroup(){
		this.saveGroup = new JPanel(getFL());

		saveLabel = new JLabel("Edit/Save Content");
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		asPNGButton = new JButton("Save as PNG");
		
		this.saveGroup.add(saveLabel);
		this.saveGroup.add(saveButton);
		this.saveGroup.add(loadButton);
		this.saveGroup.add(asPNGButton);
		
		this.contPart1.add(this.saveGroup);

	}
	
	// initiate NetworkGroup UI and add to control panel
	private void initiateNetworkGroup(){
		this.networkGroup = new JPanel(getFL());

		networkLabel = new JLabel("Networking");
		serverButton = new JButton("Start Server");
		clientButton = new JButton("Start Client");
		statusLabel = new JLabel("Mode: " + Status);
		this.networkGroup.add(networkLabel);
		this.networkGroup.add(serverButton);
		this.networkGroup.add(clientButton);
		this.networkGroup.add(statusLabel);
		this.contPart1.add(this.networkGroup);
	}
	
	// initiate TableGroup UI and add to control panel

	private void initiateTable(){
		// for now
	    WhiteBoardTableModel model = new WhiteBoardTableModel(this.canvas);
	    
		table = new JTable(model);

		table.getColumnModel().getColumn(0).setHeaderValue("x");
		table.getColumnModel().getColumn(1).setHeaderValue("y");
		table.getColumnModel().getColumn(2).setHeaderValue("WIDTH");
		table.getColumnModel().getColumn(3).setHeaderValue("HEIGHT");

		this.canvas.setTableModel(model);
		
		this.contPart2.setViewportView(table);;
	}
	
	// method to add listeners to widgets
	private void addListenersToWidgets(){
		this.rect.addActionListener(e -> {
			this.canvas.addShape(new DRectModel());
			
			});
		
		this.oval.addActionListener(e -> {
			this.canvas.addShape(new DOvalModel());
			
			});
		
		this.line.addActionListener(e -> {
			this.canvas.addShape(new DLineModel());
		});
		
		this.text.addActionListener(e ->{
			this.canvas.addShape(new DTextModel());
		});
		
		this.colorButton.addActionListener(e -> {
			if(this.canvas.getPointer() != null){
				Color color = JColorChooser.showDialog(null, "Change Button Background",Color.GRAY);
				this.canvas.changePointerColor(color);
			}
		});
		
		this.textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				WhiteBoard.this.canvas.pointerTextChanged(WhiteBoard.this.textField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				WhiteBoard.this.canvas.pointerTextChanged(WhiteBoard.this.textField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}
		});
		
		this.fontList.addActionListener(e -> {this.canvas.pointerFontChanged((String)this.fontList.getSelectedItem());});
		
		
		
		this.removeButton.addActionListener(e -> {
			this.canvas.removeShape();
		});
		
		this.frontButton.addActionListener(e->{
			this.canvas.moveToFront();
		});
		
		this.backButton.addActionListener(e->{
			this.canvas.moveToBack();

		});
		this.saveButton.addActionListener(e->{
			String name = JOptionPane.showInputDialog(null, "Please enter a name");
			IOHandler handler = new IOHandler();
			handler.save(name);
		});
		this.loadButton.addActionListener(e ->{
			String path = JOptionPane.showInputDialog(null, "Please enter a path and add .xml at the end of the file"); 
			IOHandler handler = new IOHandler();
			HashMap<Integer, DShapeModel> temp = handler.load(path);
			if(temp != null){
				canvas.LoadNewData(temp);
			}
		});
		this.asPNGButton.addActionListener(e ->{
			String name = JOptionPane.showInputDialog(null, "Please enter a name");
			IOHandler handler = new IOHandler();
			canvas.drawPNG();
			handler.saveAsPng(name);
		});
		this.serverButton.addActionListener(e ->{
			String port = JOptionPane.showInputDialog("Please enter a port number","39588");
			try{
				
				// if exit button not clicked
				if(port != null){
					int p = Integer.parseInt(port);
					Thread t = new Thread(new Server(p));
					t.start();	
					
					Status = "Server";
					this.statusLabel.setText("Mode: " + Status);
					this.clientButton.setEnabled(false);

				}
				
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			
		});
		this.clientButton.addActionListener(e ->{
			
			String hostPort = JOptionPane.showInputDialog("Please enter a host and a port number", "127.0.0.1:39588");
			try{
				if(hostPort != null){
					String host = hostPort.substring(0,hostPort.indexOf(":"));
					int port = Integer.parseInt(hostPort.substring(hostPort.indexOf(":")+1, hostPort.length()));
					Thread t = new Thread(new Client(host, port, canvas));
					t.start();	
					
					Status = "Client";
					this.statusLabel.setText("Mode: " + Status);
					disableAllButtons();

				}
				
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		});
		
	}
	
	public void enableTextField(boolean bool){
		this.textField.setEnabled(bool);
		this.fontList.setEnabled(bool);
	}
	
	public void setTextField(String text){
		this.textField.setText(text);
		
	}
	
	private static String[] getSupportedFonts(){
	    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    String[] allFonts = e.getAvailableFontFamilyNames();
	    
	    return allFonts;
	}
	
	// create the flowlayout for the groups
	private static FlowLayout getFL(){
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		return fl;
	}
	
	private void disableAllButtons(){
		
		
		rect.setEnabled(false);
		oval.setEnabled(false);
		line.setEnabled(false);
		text.setEnabled(false);
		colorButton.setEnabled(false);
		frontButton.setEnabled(false);
		backButton.setEnabled(false);
		removeButton.setEnabled(false);
		saveButton.setEnabled(false);
		loadButton.setEnabled(false);
		asPNGButton.setEnabled(false);
		serverButton.setEnabled(false);
		clientButton.setEnabled(false);
	}
	
	
	public static void main(String[] args) {
		frame = new WhiteBoard();
		
	}

}
