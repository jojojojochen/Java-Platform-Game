package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import tilemap.TileMap;

public class Ultimate extends MapObject {
	private boolean hit;
	private boolean remove;
	private BufferedImage sprites[];
	private BufferedImage hitSprites[];
	public Ultimate(TileMap tm,boolean right) {
		super(tm);
		facingRight=right;
		moveSpeed=3.0;
		
		if(right){dx=moveSpeed;}
		else{dx=-moveSpeed;}
		
		width=40;
		height=40;
		cwidth=14;
		cheight=14;
			
		try{
			BufferedImage spritesheet=ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/moveR.png"));
		
			sprites=new BufferedImage[4];
			for(int i=0;i<sprites.length;i++){
				sprites[i]=spritesheet.getSubimage(i*width, 0, width, height);
			}
			hitSprites=new BufferedImage[3];
			for(int i=0;i<hitSprites.length;i++){
				hitSprites[i]=spritesheet.getSubimage(i*width, height,width, height);
			}
			animation=new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
			}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setHit(){
		if (hit){return;}
		hit=true;
		animation.setFrames(hitSprites);
		animation.setDelay(50);
		dx=0;
	}
	public boolean shouldRemove(){return remove;}
	public void update(){
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		if(dx==0&&!hit){
			setHit();
		}
		
		animation.update();
		if(hit &&animation.hasplayedonce()){
			remove=true;
		}
			
	}
	public void draw(Graphics2D g){
		setMapPosition();
		super.draw(g);
	}

}
