package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class QWER {
	
	private Player player;
	private BufferedImage image;
	
	public QWER(){
		try{
			image=ImageIO.read(getClass().getResourceAsStream("/HUD/MoveKeys.png"));
		}
		catch(Exception e){
			e.printStackTrace();	
		}
	}
	
	public void draw(Graphics2D g){
		g.drawImage(image, 100, 214, null);
	}
}
