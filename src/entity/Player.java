package entity;

import gamestate.GameStateManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

import audio.AudioPlayer;
import tilemap.TileMap;

public class Player extends MapObject{

	public int DAMAGE;
	//player stuff
	private int health,maxHP,fire,maxFire;
	private boolean dead,flinching;
	private long flinchTimer;
	
	//fireball
	private boolean firing;
	private int fireCost,fireBallDamage;
	private ArrayList<FireBall>fireBalls;
	
	//healing
	private boolean healing;
	private int healCost,healAmount;
	
	//Ultimate
	private boolean ulting;
	private int ultCost,ultDamage;
	public ArrayList<Ultimate> ultimate;
	
	//scratch
	private boolean scratching;
	private int scratchDamage,scratchRange;
	
	//gliding 
	private boolean gliding;
	
	//animations
	private ArrayList<BufferedImage[]>sprites;
	private final int numFrames[]={2,8,2,4,4,2,5,2,8};
	
	//animation actions
	private static final int IDLE=0;
	private static final int WALKING=1;
	private static final int JUMPING=2;
	private static final int FALLING=3;
	private static final int GLIDING=4;
	private static final int FIREBALL=5;
	private static final int SCRATCHING=6;
	private static final int ULTING=7;
	private static final int HEALING=8;
	
	private HashMap<String,AudioPlayer> sfx; 
	
	public boolean ghostlevel=false;
	
	public Player (TileMap tm){
		super(tm);
		
		width=30;
		height=30;
		cwidth=14;
		cheight=14;
		
		moveSpeed=0.3*1.5;
		maxSpeed=1.6*1.5;
		stopSpeed=0.4*1.5;
		fallSpeed=0.25*1.5;
		maxFallSpeed=4.0*1.5;
		jumpStart=-7;
		stopJumpSpeed=0.3*1.5;
        
		facingRight=true;
        
		health=maxHP=100;
		fire=maxFire=1000*10;
		fireBalls = new ArrayList<FireBall>();
		
		fireCost=50*10;
		fireBallDamage=1;
		
		healCost=100*10;
		healAmount=10;
		
		ultCost=500*10;
		ultDamage=fireBallDamage*11;
		ultimate=new ArrayList<Ultimate>();
		
		
		scratchDamage=3;
		scratchRange=55;
		
		//load sprites
		try{
			BufferedImage spritesheet=ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites4.png"));
            sprites=new ArrayList<BufferedImage[]>();
			for(int i=0;i<9;i++){
				BufferedImage[] bi=new BufferedImage[numFrames[i]];
				for(int j=0;j<numFrames[i];j++){
					if(i==6)
					{
						bi[j]=spritesheet.getSubimage(j*width*2,i*height,width*2,height);
					}
					else if(i==1)
					{
						bi[j]=spritesheet.getSubimage(j*width,i*height,width-1,height);
					}
					else if(i==7){
						bi[j]=spritesheet.getSubimage(j*width,5*height,width,height);
					}
					else if(i==8){
						bi[j]=spritesheet.getSubimage(j*width,7*height,width,height);
					}
					else	
					{
						bi[j]=spritesheet.getSubimage(j*width,i*height,width,height);
					}
				}
				sprites.add(bi);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		animation = new Animation();
		currentAction=IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx=new HashMap<String,AudioPlayer>();
		sfx.put("jump",new AudioPlayer("/SoundEffect/jump.mp3"));
		sfx.put("scratch",new AudioPlayer("/SoundEffect/scratch.mp3"));
		sfx.put("fire",new AudioPlayer("/SoundEffect/fireball.mp3"));
		sfx.put("healing",new AudioPlayer("/SoundEffect/healing.mp3"));
	}
	
	public int getHealth(){return health;}
	public int getmaxHP(){return maxHP;}
	public int getFire(){return fire;}
	public int getMaxFire(){return maxFire;}
	public void setGhostLevelTrue(){ghostlevel=true;}
	public void setGhostLevelFalse(){ghostlevel=false;}
	public void setFiring(){
		firing=true;
	}
	public void setHealing(){
		healing=true;
	}
	public void setUlting(){
		ulting=true;
	}
	public void setScratching(){
		scratching=true;
	}
	public void setGliding(boolean b){
		gliding=b;
	}
	
	public void checkAttack(ArrayList<Enemy>enemies){
		if(ghostlevel){scratchDamage=0;}
		else{scratchDamage=3;}
		DAMAGE=-1;
		//loop through enemies
		for(int i=0;i<enemies.size();i++){
			Enemy e=enemies.get(i);
			//scratch attack
			if(scratching){
				if(facingRight){
					if((e.getx()>x && e.getx()<x+scratchRange && e.gety()>y-height/2 && e.gety()<y+height/2) || intersects(e))//check if enemies are in attack range and right in front of us
					{
						DAMAGE=scratchDamage;
						e.hit(scratchDamage);
					}
				}
				else {
					if((e.getx()<x && e.getx()>x-scratchRange && e.gety()>y-height/2 && e.gety()<y+height/2) || intersects(e))
					{
						DAMAGE=scratchDamage;
						e.hit(scratchDamage);
					}
				}
			}
			//fireballs
			for(int j=0;j<fireBalls.size();j++){
				if(fireBalls.get(j).intersects(e)){
					DAMAGE=fireBallDamage;
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			//ultimate
			for(int j=0; j<ultimate.size(); j++){
	            if(ultimate.get(j).intersects(e)){
	            	DAMAGE=ultDamage;
	                e.hit(ultDamage);
	                ultimate.get(j).setHit();
	                break;
	            }
			}	
			//check for enemy collision
			if(intersects(e)){
				hit(e.getDamage());
			}
		}
		
	}
	public void hit(int damage){
		if(flinching) return;
		health-=damage;
		if(health<0) health=0;
		if(health==0) dead=true;
		flinching=true;
		flinchTimer=System.nanoTime();
	}
	private void getNextPosition() {
		//movement
		if(left){dx-=moveSpeed; if(dx<-maxSpeed){dx=-maxSpeed;}}
		else if(right){dx+=moveSpeed; if(dx>maxSpeed){dx=maxSpeed;}}
		else
		{
			if(dx>0){
				dx-=stopSpeed;
				if(dx<0){dx=0;}
			}
			else if(dx<0){
				dx+=stopSpeed;
				if(dx>0){dx=0;}
			}
		}
		//cannot move while attacking, except in air
		if((currentAction==SCRATCHING || currentAction==FIREBALL || currentAction==ULTING ||currentAction==HEALING) && !(jumping || falling)){
			dx=0;
		}
		//jumping, not gliding
		if(jumping && !falling){
			sfx.get("jump").play();
			dy=jumpStart;
			falling=true;
		}
		//falling
		if(falling){
			if(dy>0 && gliding){dy+=fallSpeed*0.1;} //gliding
			else{dy+=fallSpeed;} //falling
			if(dy>0){jumping=false;}
			if(dy<0 && !jumping){dy+=stopJumpSpeed;}
			if(dy>maxFallSpeed){dy=maxFallSpeed;}
		}	
	}
	
	public void update(){
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		//check attack has stopped
		if(currentAction==SCRATCHING){
			if(animation.hasplayedonce()){scratching=false;}
		}
		if(currentAction==FIREBALL){
			if(animation.hasplayedonce()){firing=false;}
		}
		if(currentAction==HEALING){
			if(animation.hasplayedonce()){
				if(fire>=healCost){fire-=healCost;if(health+10>=100){health=100;}else{health+=10;}}
				healing=false;
			}
		}
        if(currentAction==ULTING){
            if(animation.hasplayedonce()){ulting=false;}
        }
		
		//fireball attack
		fire+=5; //Continuously regenerate the fireball
		if(fire>maxFire){fire=maxFire;}
		if(firing&&currentAction!=FIREBALL){
			if(fire>=fireCost){
				fire-=fireCost;
				FireBall fb=new FireBall(tileMap,facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
        if(ulting&&currentAction!=ULTING){
            if(fire>=ultCost){
                fire-=ultCost;
                Ultimate u=new Ultimate(tileMap,facingRight);
                u.setPosition(x,y);
                ultimate.add(u);
            }
        }
        
		//update fireballs
		for(int i=0;i<fireBalls.size();i++){
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()){
				fireBalls.remove(i);
				i--;
			}
		}
		//update ultiamte
		for(int i=0; i<ultimate.size(); i++){
			ultimate.get(i).update();
			if(ultimate.get(i).shouldRemove()){
				ultimate.remove(i);
				i--;
			}
		}
		
		//check done flinching
		if(flinching){
			long elapsed=(System.nanoTime()-flinchTimer)/500000;
			if(elapsed>1000){
				flinching=false;
			}
		}
		
		//set animation
		if(scratching){
			if(currentAction!=SCRATCHING){
				sfx.get("scratch").play();
				currentAction=SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(20);
				width=60;
			}
		}
		else if(firing){
			if(currentAction!=FIREBALL){
				currentAction=FIREBALL;
				if(fire>=fireCost){
					sfx.get("fire").play();
					}
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(50);
				width=30;
			}
		}
		else if(healing){
			if(currentAction!=HEALING){
				currentAction=HEALING;
				if(fire>=healCost){
				sfx.get("healing").play();
				animation.setFrames(sprites.get(HEALING));
				}
				animation.setDelay(50);
				width=30;
			}
		}
        else if(ulting){
            if(currentAction!=ULTING){
                currentAction=ULTING;
                animation.setFrames(sprites.get(ULTING));
                animation.setDelay(50);
                width=30;
            }
        }
		else if(dy>0){
			if(gliding){
				if(currentAction!=GLIDING){
					currentAction=GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width=30;
				}
			}
			else if(currentAction!=FALLING){
				currentAction=FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width=30;
			}
		}
		else if(dy<0){
			if(currentAction!=JUMPING){
				currentAction=JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width=30;
			}
		}
		else if(left||right){
			if(currentAction!=WALKING){
				currentAction=WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width=30;
			}
		}
		else
		{
			if(currentAction!=IDLE){
				currentAction=IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width=30;
			}
		}
		animation.update();
		//set direction
		if(currentAction!=SCRATCHING && currentAction!=FIREBALL && currentAction!=ULTING && currentAction!=HEALING){
			if(right){facingRight=true;}
			if(left){facingRight=false;}
		}
	}

	public void draw(Graphics2D g){
		setMapPosition();
		
		//draw fireballs
		for(int i=0;i<fireBalls.size();i++){
			fireBalls.get(i).draw(g);
		}
		//draw ultimate
		for(int i=0; i<ultimate.size(); i++){
			ultimate.get(i).draw(g);
		}
		//draw player
		if(flinching){
			long elapsed=(System.nanoTime() - flinchTimer)/1000000;
			if(elapsed/100%2==0){return;}
		}
		super.draw(g);
	}
}
