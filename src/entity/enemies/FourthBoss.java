package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class FourthBoss extends Enemy{
	private BufferedImage sprites[];
	private ArrayList<Enemy>enemies;
	private int stepCount;
	public FourthBoss(TileMap tm,ArrayList<Enemy>enemies) {
		super(tm);
		this.enemies=enemies;
		moveSpeed=1.0;
		maxSpeed=2.0;
		fallSpeed=0;
		maxFallSpeed=0;
		
        width=465/6;  //638/8,655
        height=42; //79
        cwidth=20;
        cheight=20;
		
		health=maxHealth=35;
		damage=8;
		
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Boss4.png"));
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
		animation.setDelay(5000);
		
		right=true;
		facingRight=true;
		
		stepCount=0;
	}
	private void getNextPosition(){
		Random rand=new Random();
		int max=1,min=-1;
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
		stepCount++;
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
		if(stepCount%10==0){
			Boss4ParticleProjectile proj=new Boss4ParticleProjectile(tileMap);
			proj.setType(Boss4ParticleProjectile.GRAVITY);
			proj.setPosition(x,y+15);
			int dir=Math.random()<0.5?1:-1;
			proj.setVector(dir, 0);
			enemies.add(proj);
		}
		animation.update();
	}
	public void draw(Graphics2D g){
		//if(notOnScreen())return; //don't draw the ones that is not on screen
		setMapPosition();
		super.draw(g);
	}
}
