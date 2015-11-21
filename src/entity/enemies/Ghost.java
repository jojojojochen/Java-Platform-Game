package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class Ghost extends Enemy{
	private BufferedImage sprites[];
	public Ghost(TileMap tm) {
		super(tm);
		moveSpeed=0.2;
		maxSpeed=1.0;
		fallSpeed=0.1;
		maxFallSpeed=2.0;
		
        width=170/6;
        height=60;
        cwidth=8;
        cheight=15;
		
		health=maxHealth=3;
		damage=20;
		
		//load sprites
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/ghost.png"));
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
