package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class ThirdBoss extends Enemy{
	private BufferedImage sprites1[];
	private BufferedImage sprites2[];
	public ThirdBoss(TileMap tm) {
		super(tm);
		
		fallSpeed=0;
		maxFallSpeed=0;
		
        width=800/11;  
        height=75;
        cwidth=35;
        cheight=35;
		
		health=maxHealth=50;
		damage=5;
		
		moveSpeed=3.0;
		maxSpeed=6.0;
		
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/lycanthrope1.png"));
			sprites1= new BufferedImage[11];
			sprites2= new BufferedImage[11];
			for(int i=0;i<sprites1.length;i++){
				sprites1[i]=spritesheet.getSubimage(i*width,0,width,height);
				sprites2[i]=spritesheet.getSubimage(i*width,height+3,width,height);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		animation=new Animation();
		animation.setFrames(sprites1);
		animation.setDelay(150);
		
		animation1=new Animation();
		animation1.setFrames(sprites2);
		animation1.setDelay(50);
		right=true;
		facingRight=true;
	}
	private void getNextPosition(){
		if(left){
			dx-=moveSpeed; 
			if(dx<-maxSpeed){dx=-maxSpeed;}
		}
		else if(right){
			dx+=moveSpeed; 
			if(dx>maxSpeed){dx=maxSpeed;}
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
		if(health<maxHealth/2){
			damage=15;
			moveSpeed=6.0;
			maxSpeed=15.0;
			animation1.update();
		}
		else{
			animation.update();
		}
	}
	public void draw(Graphics2D g){
		//if(notOnScreen())return; //don't draw the ones that is not on screen
		setMapPosition();
		if(health<maxHealth/2){super.draw1(g);}
		else{super.draw(g);}
	}
}
