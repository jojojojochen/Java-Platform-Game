package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class SnowEnemy extends Enemy{
	private BufferedImage sprites[];
	public SnowEnemy(TileMap tm) {
		super(tm);
		moveSpeed=1.0;
		maxSpeed=1.5;
		fallSpeed=1.0;
		maxFallSpeed=10.0;
		
        width=176/6;
        height=27;
        cwidth=14;
        cheight=14;
		
		health=maxHealth=4;
		damage=5;
		
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/white1.png"));
			sprites= new BufferedImage[6];
			for(int i=0;i<sprites.length;i++){
				sprites[i]=spritesheet.getSubimage(i*width,0,width,height);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		animation=new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);
		
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
