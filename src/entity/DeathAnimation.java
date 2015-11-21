package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class DeathAnimation {
	
	private int x,y,xmap,ymap,width,height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public DeathAnimation(int x, int y){
		this.x=x;
		this.y=y;
		
		width=165/5;
		height=40;
		
		try{
			BufferedImage spritesheet=ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/blue1.png"));
			sprites= new BufferedImage[10];
			for(int i=6;i<sprites.length;i++){
				sprites[i]=spritesheet.getSubimage(i*width,35,width,height);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		animation=new Animation();
		animation.setFrames(sprites);
		animation.setDelay(10);
	}
	public void update(){
		animation.update();
		if(animation.hasplayedonce()){
			remove=true;
		}
	}
	
	public boolean shouldRemove(){return remove;}
	public void setMapPosition(int x, int y){
		xmap=x;
		ymap=y;
	}
	public void draw(Graphics2D g){
		g.drawImage(animation.getImage(),x+xmap-width/2,y+ymap-height/2,null);
	}
}
