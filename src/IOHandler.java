import java.awt.image.BufferedImage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class IOHandler {
	
	public void save(String name){
		String path = "./" + name + ".xml";
		XMLEncoder encoder = null;
		File file = new File(path);
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
			encoder = new XMLEncoder(bos);
			encoder.writeObject(Canvas.modelMap);
			encoder.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, DShapeModel> load(String path){
		HashMap<Integer, DShapeModel> models = new HashMap<Integer, DShapeModel>();
		XMLDecoder decoder = null;
		if(path == null || !new File(path).isFile()) {
			return null;
		}
		
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path))){
			decoder = new XMLDecoder(bis);
			models = (HashMap<Integer, DShapeModel>) decoder.readObject();
			decoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return models;
	}
	
	public void saveAsPng(String name){
		String path = "./" + name +".png";
		BufferedImage bi = Canvas.paintImage;
		try {
			ImageIO.write(bi, "PNG", new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
