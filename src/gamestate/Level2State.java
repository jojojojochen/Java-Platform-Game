
package gamestate;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.*;

import audio.AudioPlayer;
import main.GamePanel;
import entity.Damage;
import entity.Enemy;
import entity.DeathAnimation;
import entity.Hud;
import entity.Player;
import entity.QWER;
import entity.enemies.SecondBoss;
import entity.enemies.Snake;
import tilemap.Background;
import tilemap.TileMap;

public class Level2State extends GameState{

	private TileMap map;
	private Background bg;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<DeathAnimation> explosions;
	private Hud hud;
	private QWER qwer;
	private Damage dmg;
	private AudioPlayer bgMusic;
	private boolean eventDead;
	private long Start=0,Duration;
	
	public Level2State(GameStateManager gsm){
		this.gsm=gsm;
		init();
	}
	
	public void init() {
		map=new TileMap(30);
		map.loadTiles("/Tilesets/Deserttile.png");
		map.loadMap("/Maps/level2map");
		map.setPosition(0,0);
		map.setTween(0.07);
		bg=new Background("/bg/Desert24.jpg",0.0005);//Tile map speed 
		player=new Player(map);
		player.setPosition(100,450); //100,450
		
		populateEnemies();
		
		explosions=new ArrayList<DeathAnimation>();
		
		hud = new Hud(player);
		qwer =new QWER();
		dmg = new Damage(player);
		//bgMusic=new AudioPlayer("/Music/DesertFlower.mp3");
		//bgMusic.play();
		}
	
	private void populateEnemies(){
		enemies=new ArrayList<Enemy>();
		
		Snake s;
		SecondBoss boss2;
		Point[] points=new Point[]{
				//new Point(840,200),
				new Point(800,200),
				//new Point(750,200),
				new Point(1525,200),
				new Point(1680,200),
				new Point(1900,200),
				new Point(2000,200),
				new Point(2200,200),
				new Point(2300,200),
				new Point(2550,200),
				new Point(3400,370),
				new Point(3500,370)
				}; 
		for(int i=0;i<points.length;i++){
			s=new Snake(map);
			s.setPosition(points[i].x,points[i].y);
			enemies.add(s);
		}
		
		Point[] p=new Point[]{
				new Point(2950,300)
			};
			for(int i=0; i<p.length; i++){
				boss2=new SecondBoss(map);
				boss2.setPosition(p[i].x,p[i].y);
				enemies.add(boss2);
		}
			
	}
	public void update() {
		//update dead
		if(player.getHealth()==0 || player.gety()>map.getHeight()){
			eventDead=true;
		}
		if(eventDead){eventDead();}
		//update player
		player.update();
		map.setPosition(GamePanel.WIDTH/2-player.getx(),GamePanel.HEIGHT/2-player.gety());
		
		//set background
		bg.setPosition(map.getx(),map.gety());
		
		//attack enemies
		player.checkAttack(enemies);
				
		//update all enemies
		for(int i=0; i<enemies.size();i++){
			Enemy e=enemies.get(i);
			e.update();
			if(e.isDead()){
				enemies.remove(i);
				i--;
				explosions.add(new DeathAnimation(e.getx(),e.gety()));
				if(e.getx()<=3500 && e.getx()>=2700 && e.gety()<370){
					//bgMusic.stop();
					gsm.setState(GameStateManager.LEVEL3STATE);
				}
			}
		}
		
		//update explosions
			for(int i=0;i<explosions.size();i++){
				explosions.get(i).update();		
				if(explosions.get(i).shouldRemove()){
					explosions.remove(i);
					i--;
				}
			}
	}
	public void draw(Graphics2D g) {
		//draw bg
		bg.draw(g);
		//draw tile map
		map.draw(g);
		//draw player
		player.draw(g);
		//draw enemies
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).draw(g);
		}
		//draw explosion
		for(int i=0;i<explosions.size();i++){
			explosions.get(i).setMapPosition((int)map.getx(), (int)map.gety());
			explosions.get(i).draw(g);
		}
		//draw hud
		hud.draw(g);
		qwer.draw(g);
		if(player.DAMAGE!=0){Start = System.nanoTime();}
		Duration=System.nanoTime()-Start;
		if(Duration<=1500000000){dmg.draw(g, player.DAMAGE);}
	}
	public void keyPressed(int k) {
		if(k==KeyEvent.VK_LEFT){player.setLeft(true);}
		if(k==KeyEvent.VK_RIGHT){player.setRight(true);}
		if(k==KeyEvent.VK_UP){player.setJumping(true);}
		if(k==KeyEvent.VK_DOWN){player.setDown(true);}
		if(k==KeyEvent.VK_SPACE){player.setGliding(true);}
		if(k==KeyEvent.VK_Q){player.setScratching();}
		if(k==KeyEvent.VK_W){player.setFiring();}
		if(k==KeyEvent.VK_E){player.setHealing();}
		if(k==KeyEvent.VK_R){player.setUlting();}
		if(k==KeyEvent.VK_ESCAPE){gsm.setState(GameStateManager.PAUSESCREEN);}
	}
	public void keyReleased(int k) {
		if(k==KeyEvent.VK_LEFT){player.setLeft(false);}
		if(k==KeyEvent.VK_RIGHT){player.setRight(false);}
		if(k==KeyEvent.VK_UP){player.setJumping(false);}
		if(k==KeyEvent.VK_DOWN){player.setDown(false);}
		if(k==KeyEvent.VK_SPACE){player.setGliding(false);}
	}
	public void eventDead(){
		//bgMusic.stop();
		gsm.setState(GameStateManager.MENUSTATE);
	}
}

