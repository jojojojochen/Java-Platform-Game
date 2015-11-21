package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Hud {
	
	private Player player;
	private BufferedImage image;
	private Font font;
	
	public Hud(Player p){
		player=p;	
		try{
			image=ImageIO.read(getClass().getResourceAsStream("/HUD/HPMana.png"));
			font=new Font("Arial", Font.PLAIN,9);
		}
		catch(Exception e){
			e.printStackTrace();	
		}
	}
	
	public void draw(Graphics2D g){
		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(player.getHealth()+"/"+player.getmaxHP(),35,23);
		g.drawString(player.getFire()/100+"/"+player.getMaxFire()/100,35,33);
	}
}
