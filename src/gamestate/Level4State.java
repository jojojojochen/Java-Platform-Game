package gamestate;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.GamePanel;
import tilemap.Background;
import tilemap.TileMap;
import audio.AudioPlayer;
import entity.Damage;
import entity.DeathAnimation;
import entity.Enemy;
import entity.Hud;
import entity.Player;
import entity.QWER;
import entity.enemies.FourthBoss;
import entity.enemies.Ghost;
import entity.enemies.ThirdBoss;
import entity.enemies.Zombie;

public class Level4State extends GameState{
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
	
	public Level4State(GameStateManager gsm){
		this.gsm=gsm;
		init();
	}
	
	public void init() {
		map=new TileMap(30);
		map.loadTiles("/Tilesets/spookytileset.png");
		map.loadMap("/Maps/level4map");
		map.setPosition(0,0);
		map.setTween(0.07);
		bg=new Background("/bg/limbo1.jpg",0.0005);//Tile map speed 
		player=new Player(map);
		player.setGhostLevelTrue();
		player.setPosition(100,1750);
		
		populateEnemies();
		
		explosions=new ArrayList<DeathAnimation>();
		
		hud = new Hud(player);
		qwer =new QWER();
		dmg = new Damage(player);
		//bgMusic=new AudioPlayer("/Music/Park.mp3");
		//bgMusic.play();
		}
	
	private void populateEnemies(){
		enemies=new ArrayList<Enemy>();
		Ghost ghost;
		FourthBoss boss4;
		Point[] points=new Point[]{
				new Point(500,1950),
				new Point(520,1900),
				new Point(550,1970),
				new Point(600,1980),
				//new Point(620,1700),
				new Point(600,1700),
				//new Point(600,1850),
				new Point(650,1900),
				new Point(700,2000),
				new Point(1680,100),
				new Point(1766,1300),
				new Point(1750,100),
				new Point(1800,1300),
				new Point(1900,1000),
				//new Point(2100,500),
				new Point(2200,1000),
				//new Point(2210,2000),
				new Point(2220,2000),
				//new Point(2250,1900),
				new Point(2300,2000),
				//new Point(2350,1800),
				new Point(2400,2000),
				//new Point(2450,1900),
				new Point(2500,2000),
				//new Point(2550,1800),
				new Point(2600,1700),
				//new Point(2650,1800),
				new Point(2750,2000),
				new Point(2800,1900),
				}; 
		for(int i=0;i<points.length;i++){
			ghost=new Ghost(map);
			ghost.setPosition(points[i].x,points[i].y);
			enemies.add(ghost);
		}
		
		Point[] p=new Point[]{
				new Point(3250,1830),
			};
			for(int i=0; i<p.length; i++){
				boss4=new FourthBoss(map,enemies);
				boss4.setPosition(p[i].x,p[i].y);
				enemies.add(boss4);
			}
	}
	
	public void update(){
		System.out.println(player.getx()+" "+player.gety());
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
			if(e.isDead()||e.shouldRemove()){
				enemies.remove(i);
				i--;
				explosions.add(new DeathAnimation(e.getx(),e.gety()));
				if(e.getx()<=4000 && e.getx()>=3000 && e.gety()<2000){
					//bgMusic.stop();
					player.setGhostLevelFalse();
					gsm.setState(GameStateManager.LEVEL1STATE);
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
