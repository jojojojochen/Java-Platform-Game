package entity.enemies;

import entity.Animation;
import entity.Enemy;
import tilemap.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BlueMushroom extends Enemy {
	private BufferedImage sprites[];
	public BlueMushroom(TileMap tm){
		super(tm);
		
		moveSpeed=0.8;
		maxSpeed=1.0;
		fallSpeed=0.4;
		maxFallSpeed=10.0;
		
        width=88/4;
        height=30;
        cwidth=14;
        cheight=14;
		
		health=maxHealth=4;
		damage=5;
		
		//load sprites
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/blue1.png"));
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
		animation.setDelay(300);
		
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
