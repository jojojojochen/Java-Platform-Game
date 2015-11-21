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
import entity.enemies.ThirdBoss;
import entity.enemies.Zombie;

public class Level3State extends GameState {
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
	
	public Level3State(GameStateManager gsm){
		this.gsm=gsm;
		init();
	}
	
	public void init() {
		map=new TileMap(30);
		map.loadTiles("/Tilesets/parktileset.png");
		map.loadMap("/Maps/level3map");
		map.setPosition(0,0);
		map.setTween(0.07);
		bg=new Background("/bg/park1.jpg",0.0005);//Tile map speed 
		player=new Player(map);
		player.setPosition(100,450); //100,450
		
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
		Zombie z;
		ThirdBoss boss3;
		Point[] points=new Point[]{
				new Point(840,200),
				//new Point(800,200),
				//new Point(1039,400),
				//new Point(1680,100),
				new Point(1766,1300),
				//new Point(1900,100),
				new Point(2200,100),
				new Point(2450,1400),
				//new Point(1950,100),
				new Point(3446,1400),
				new Point(3500,370),
				new Point(3710,500)
				}; 
		for(int i=0;i<points.length;i++){
			z=new Zombie(map);
			z.setPosition(points[i].x,points[i].y);
			enemies.add(z);
		}
		
		Point[] p=new Point[]{
				new Point(4136,543)
			};
			for(int i=0; i<p.length; i++){
				boss3=new ThirdBoss(map);
				boss3.setPosition(p[i].x,p[i].y);
				enemies.add(boss3);
		}
			
	}
	public void update() {
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
			if(e.isDead()){
				enemies.remove(i);
				i--;
				explosions.add(new DeathAnimation(e.getx(),e.gety()));
				if(e.getx()<=4585 && e.getx()>=3906 && e.gety()<600){
					//bgMusic.stop();
					gsm.setState(GameStateManager.LEVEL4STATE);
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