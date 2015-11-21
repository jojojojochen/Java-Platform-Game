package tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileMap {

	public int getNumRows() { return r; }
	public int getNumCols() { return c; }
	private double x,y,tween;
	private int xmin,xmax,ymin,ymax,tilesize,r,c,w,h;
	private int map[][];
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile tiles[][];
	private int rowoffset,coloffset,rowtodraw,coltodraw;
	public TileMap(int tilesize){
		this.tilesize=tilesize;
		rowtodraw = GamePanel.HEIGHT/tilesize+2;
		coltodraw = GamePanel.WIDTH/tilesize+2;
		tween = 0.07;
	}
	public void loadTiles(String s){
		try{
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross=tileset.getWidth()/tilesize;
			tiles = new Tile[2][numTilesAcross];
			BufferedImage subimage;
			for(int i=0; i<numTilesAcross; i++){
				subimage=tileset.getSubimage(i*tilesize,0,tilesize,tilesize);
				tiles[0][i]=new Tile(subimage,Tile.NORMAL);
				subimage=tileset.getSubimage(i*tilesize,tilesize,tilesize,tilesize);
				tiles[1][i]=new Tile(subimage,Tile.BLOCKED);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void loadMap(String s){
		try{
			InputStream in= getClass().getResourceAsStream(s);
			BufferedReader read=new BufferedReader(new InputStreamReader(in));
			c=Integer.parseInt(read.readLine());
            r=Integer.parseInt(read.readLine());
			map = new int[r][c];
			w=c*tilesize;
			h=r*tilesize;
			xmin=GamePanel.WIDTH-w;
			xmax=0;
			ymin=GamePanel.HEIGHT-h;
			ymax=0;
			String delims="\\s+";
			for(int i=0; i<r; i++){
				String line=read.readLine();
				String tokens[] = line.split(delims);
				for(int j=0; j<c; j++){
					map[i][j]=Integer.parseInt(tokens[j]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getTileSize(){return tilesize;}
	public double getx(){return x;}
	public double gety(){return y;}
	public int getWidth(){return w;}
	public int getHeight(){return h;}
	public int getType(int row, int col){
		int rc=	map[row][col];
		int rr=rc/numTilesAcross;
		int cc=rc%numTilesAcross;
		return tiles[rr][cc].getType();
	}
	public void setTween(double d){tween=d;}	
	public void setPosition (double x, double y){
		this.x+=(x-this.x)*tween; //make camera follow smoothly
		this.y+=(y-this.y)*tween;
		
		fixBounds();
		coloffset=(int)-this.x/tilesize; //where to start drawing 
		rowoffset=(int)-this.y/tilesize;
	}
	private void fixBounds(){
		if(x<xmin) x=xmin;
		if(y<ymin) y=ymin;
		if(x>xmax) x=xmax;
		if(y>ymax) y=ymax;
	}
	public void draw(Graphics2D g){
		for(int row=rowoffset;row<rowoffset+rowtodraw;row++){
			if(row>=r){break;}
			for(int col=coloffset;col<coloffset+coltodraw;col++){
				if(col>=c){break;}
                if(map[row][col]==0){continue;} //not drawing the first tile
				int rc=map[row][col];
				int rr=rc/numTilesAcross;
				int cc=rc%numTilesAcross;
				g.drawImage(tiles[rr][cc].getImage(),(int)x+col*tilesize,(int)y+row*tilesize,null);//position we draw our image
			}
		}
	}
}
