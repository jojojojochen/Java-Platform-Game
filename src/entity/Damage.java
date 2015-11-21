package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

public class Damage {
	private Player player;
	private Font font;
	private BufferedImage image,image1,image2,image0;
	
	public Damage(Player p){
		player=p;	
		try{
			image=ImageIO.read(getClass().getResourceAsStream("/HUD/eleven.png"));
			image1=ImageIO.read(getClass().getResourceAsStream("/HUD/one.png"));
			image2=ImageIO.read(getClass().getResourceAsStream("/HUD/three.png"));
			image0=ImageIO.read(getClass().getResourceAsStream("/HUD/zero.png"));
			font=new Font("Arial", Font.PLAIN,20);
		}
		catch(Exception e){
			e.printStackTrace();	
		}
	}
	
	public void draw(Graphics2D g,int dmg){
		Random rand=new Random();
		int max=30,min=0;
		int randNum=rand.nextInt((max-min)+1)+min;
		if(dmg==11){g.drawImage(image, player.getx()+player.getxmap()+20, player.gety()+player.getymap()-25-randNum, null);}
		if(dmg==1){g.drawImage(image1, player.getx()+player.getxmap()+20, player.gety()+player.getymap()-25-randNum, null);}
		if(dmg==3){g.drawImage(image2, player.getx()+player.getxmap()+20, player.gety()+player.getymap()-25-randNum, null);}
		if(dmg==0){g.drawImage(image0, player.getx()+player.getxmap()+20, player.gety()+player.getymap()-25-randNum, null);}
		//g.setFont(font);
		//g.setColor(Color.RED);
		//if(player.DAMAGE!=0){g.drawString(previous+"",player.getx()+player.getxmap(),player.gety()+player.getymap()-randNum);}
	}
}
