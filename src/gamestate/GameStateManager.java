package gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager {

	private GameState gs[];
	private int currentstate;
	public int previousstate;
	private PauseScreen p;
	public static final int NUMGAMESTATES=6;
	public static final int MENUSTATE=0;
	public static final int LEVEL1STATE=1;
	public static final int LEVEL2STATE=2;
	public static final int LEVEL3STATE=3;
	public static final int LEVEL4STATE=4;
	public static final int PAUSESCREEN=5;
	private boolean paused;
	public GameStateManager(){
		gs = new GameState[NUMGAMESTATES];
		p=new PauseScreen(this);
		paused=false;
		currentstate=MENUSTATE;
		previousstate=MENUSTATE;
		loadState(currentstate);
	}
	
	public void loadState(int state){
		if(state==MENUSTATE){gs[state]=new MenuState(this);}
		if(state==LEVEL1STATE){gs[state]=new Level1State(this);}
		if(state==LEVEL2STATE){gs[state]=new Level2State(this);}
		if(state==LEVEL3STATE){gs[state]=new Level3State(this);}
		if(state==LEVEL4STATE){gs[state]=new Level4State(this);}
		if(state==PAUSESCREEN){gs[state]=new PauseScreen(this);}
	}
	
	private void unloadState(int state){
		gs[state]=null;
	}
	
	public void setState(int state){
		previousstate=currentstate;
		unloadState(currentstate);
		currentstate=state;
		loadState(currentstate);
		//gs[currentstate].init();
	}
	public void setPaused(boolean b){
		paused=b;
	}
	public void update(){
		if(paused){
			p.update();
			return;
		}
		if(gs[currentstate]!=null){gs[currentstate].update();}
	}
	public void draw(Graphics2D g){
		if(paused){
			p.draw(g);
			return;
		}
		if(gs[currentstate]!=null){	gs[currentstate].draw(g);}
		
	}
	public void keyPressed(int k){
		gs[currentstate].keyPressed(k);
	}
	public void keyReleased(int k){
		gs[currentstate].keyReleased(k);
	}
}
