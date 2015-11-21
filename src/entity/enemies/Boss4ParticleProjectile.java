package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class Boss4ParticleProjectile extends Enemy{
	private BufferedImage sprites[];
	private boolean start,permanent;
	private int type=0,bounceCount=0;
	public static int VECTOR=0,GRAVITY=1,BOUNCE=2;
	
	public Boss4ParticleProjectile(TileMap tm) {
		super(tm);
		
		health=maxHealth=1;
		width=19;
		height=18;
		cwidth=9;
		cheight=9;
		
		damage=5;
		moveSpeed=3;
		
		try{
			BufferedImage spritesheet= ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/fireparticle.png"));
			sprites = new BufferedImage[1];
			sprites[0]=spritesheet.getSubimage(0,0,width,height);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		animation=new Animation();
		animation.setFrames(sprites);
		animation.setDelay(2);
		
		start=true;
		flinching=true;
		permanent=false;
	}
	public void setType(int i){type=i;}
	public void setPermanent(boolean b){permanent=b;}
	public void update(){
		if(start){
			if(animation.hasplayedonce()){
				animation.setFrames(sprites);
				animation.setDelay(2);
				start=false;
			}
		}
		//checkTileMapCollision();
		if(type==VECTOR){
			x+=dx;
			y+=dy;
		}
		else if(type==GRAVITY){
			dy+=0.2;
			x+=dx;
			y+=dy;
		
		}
		else if(type==BOUNCE){
			double dx2=dx;
			double dy2=dy;
			checkTileMapCollision();
			if(dx==0){
				dx=-dx2;
				bounceCount++;
			}
			if(dy==0){
				dy=-dy2;
				bounceCount++;
			}
			x+=dx;
			y+=dy;
		}
		animation.update();
		
		if(permanent==false){
			if(x<0||x>tileMap.getWidth()||y<0||y>tileMap.getHeight()){
				remove=true;
			}
			if(bounceCount==1){
				remove=true;
			}
		}
	}
	public void draw(Graphics2D g){
		setMapPosition();
		super.draw(g);
	}
}
