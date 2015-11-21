package tilemap;

import java.awt.image.BufferedImage;

public class Tile {
	private BufferedImage image;
	private int type;
	public  static final int NORMAL=0, BLOCKED=1;
	public Tile(BufferedImage image, int type){
		this.image=image;
		this.type=type;
	}
	public BufferedImage getImage(){return image;}
	public int getType(){return type;}
	
}
