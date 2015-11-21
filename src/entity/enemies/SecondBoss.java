package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class SecondBoss extends Enemy {
	private BufferedImage sprites[];
	public SecondBoss(TileMap tm) {
		super(tm);
		moveSpeed=6.0;
		maxSpeed=12.0;
		fallSpeed=0;
		maxFallSpeed=0;
		
        width=739/9;  //638/8,655
        height=80; //79
        cwidth=35;
        cheight=35;
		
		health=maxHealth=35;
		damage=12;
		
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Boss2.png"));
			sprites= new BufferedImage[9];
			for(int i=0;i<sprites.length;i++){
				sprites[i]=spritesheet.getSubimage(i*width,0,width,height);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		animation=new Animation();
		animation.setFrames(sprites);
		animation.setDelay(50);
		
		right=true;
		facingRight=true;
	}
	private void getNextPosition(){
		Random rand=new Random();
		int max=30,min=-30;
		int randNum=rand.nextInt((max-min)+1)+min;
		if(left){
			dx-=moveSpeed; 
			if(dx<-maxSpeed){dx=-maxSpeed;}
			dy=randNum;
		}
		else if(right){
			dx+=moveSpeed; 
			if(dx>maxSpeed){dx=maxSpeed;}
			dy=randNum;
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
