package entity;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;
	private long start,delay;
	private boolean playedonce;
	public Animation(){
		playedonce=false;
	}
	public void setFrames(BufferedImage frames[]){
		this.frames=frames;
		currentFrame=0;
		start=System.nanoTime();
		playedonce=false;
	}
	public void setDelay(long d){delay=d;}
	public void setFrame(int i){currentFrame=i;}
	public void update(){
		if(delay==-1){return;}
		long elapsed = (System.nanoTime()-start)/1000000;
		if(elapsed > delay){
			currentFrame++;
			start=System.nanoTime();
		}
		if(currentFrame==frames.length){
			currentFrame=0;
			playedonce=true;
		}
	}
	public int getFrame(){return currentFrame;}
	public BufferedImage getImage(){return frames[currentFrame];}
	public boolean hasplayedonce(){return playedonce;}
}
