package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class Zombie extends Enemy{
	private BufferedImage sprites[];
	public Zombie(TileMap tm) {
		super(tm);
		moveSpeed=0.2;
		maxSpeed=0.5;
		fallSpeed=0.4;
		maxFallSpeed=10.0;
		
        width=164/4;
        height=46;
        cwidth=20;
        cheight=20;
		
		health=maxHealth=10;
		damage=5;
		
		//load sprites
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/zombie.png"));
			sprites= new BufferedImage[4];
			for(int i=0;i<sprites.length;i++){
				sprites[i]=spritesheet.getSubimage(i*width,0,width,height);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		animation=new Animation();
		animation.setFrames(sprites);
		animation.setDelay(500);
		
		right=true;
		facingRight=true;
	}
	private void getNextPosition(){
		if(left){
			dx-=moveSpeed; 
			if(dx<-maxSpeed){
				dx=-maxSpeed;
				}
		}
		else if(right){
			dx+=moveSpeed; 
			if(dx>maxSpeed){
				dx=maxSpeed;
				}
			}
		if(falling){
			dy+=fallSpeed;
		}
		
	}
	public void update(){
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);	
		
		//check flinching
		if(flinching){
			long elapsed=(System.nanoTime()-flinchTimer)/1000000; //check long long it have been flinching
			if(elapsed>400){
				flinching=false;
			}
		}
		
		//if it hits the wall it will go the other direction
		if(right && dx==0){
			right =false;
			left=true;
			facingRight=false;
		}
		else if(left && dx==0){
			right=true;
			left=false;
			facingRight=true;
		}
		//update animation
		animation.update();
	}
	public void draw(Graphics2D g){
		//if(notOnScreen())return; //don't draw the ones that is not on screen
		setMapPosition();
		super.draw(g);
	}

}
