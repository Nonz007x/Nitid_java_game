package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import monster.MON_Circle;
import monster.MON_Kite;
import monster.MON_Piramid;
import monster.MON_Square;
import object.OBJ_Key;
import object.SuperObject;
import tile.TileManager;


public class GamePanel extends JPanel implements Runnable{
//	 Screen setting
	final int originaltilesize = 16;//32x32 
	final int scale = 4;
	public final int tilesize = originaltilesize * scale;//64x64 tile
	public final int maxscreencol = 16;
	public final int maxscreenrow = 12;
	public final int screenwidth = tilesize*maxscreencol;//768 pixel
	public final int screenheight = tilesize*maxscreenrow;// 576 pixel
	
	//fps
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler(this);
	Thread gameThread;
	public CollisionChecker cChecker = new CollisionChecker(this);
	public UI ui = new UI(this);
	public SuperObject obj = new SuperObject();
	
	public Player player = new Player(this,keyH);
	public MON_Piramid mon = new MON_Piramid(this,player);
	public MON_Circle mon2 = new MON_Circle(this,player);
	public MON_Kite mon3 = new MON_Kite(this,player);
	public MON_Square mon4 = new MON_Square(this,player,obj);
	
	public int gamestate;
	public final int titlestate = 0;
	public final int playstate = 1;
	public final int deadstate = 2;
	
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenwidth,screenheight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setRestart() {
		player = new Player(this,keyH);
		mon = new MON_Piramid(this,player);
		mon2 = new MON_Circle(this,player);
		mon3 = new MON_Kite(this,player);
		mon4 = new MON_Square(this,player,obj);
		obj = new OBJ_Key(this);
	}
	
	public void setupGame(){
		gamestate = titlestate;
	}
	
	public void setStar() {
//		aSetter.setObject();
		obj = new OBJ_Key(this);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
		setStar();
	}
	
	@Override
//	public void run() {
//		double drawInterval = 1000000000/FPS; // 0.01666s
//		double nextDrawTime = System.nanoTime() + drawInterval;
//		
//		while(gameThread != null) {
//			 
//			update();
//			
//			repaint();
//			
//			
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime = remainingTime/1000000;
//				
//				if(remainingTime < 0) {
//					remainingTime = 0;
//				}
//			
//				Thread.sleep((long) remainingTime);
//				
//				nextDrawTime += drawInterval;
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			if(timer >= 1000000000) {
				drawCount = 0;
				timer = 0;
			}
			
		}
		
	}
	
	public void update() {
		if(gamestate == playstate) {
			player.update();
//			mon4.update();
			mon.update();
//			mon3.update();
			if(player.hasStar >= 10) {
				mon2.update();
			}
			if(player.hasStar >= 20) {
				mon4.update();
			}
			if(player.hasStar >= 30) {
				mon3.update();
			}
		}
		if(gamestate == deadstate) {
			
		}
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		

		//title Screen
		if(gamestate == titlestate) {
			ui.draw(g2);
		}
		else {
			//tile
			if(player.hasStar>=20) {
				tileM.loadMap("/maps/map2.txt");
			}
			if(player.hasStar<20) {
				tileM.loadMap("/maps/map.txt");
			}
			tileM.draw(g2);
			//object
			if(obj!=null) {
				obj.draw(g2,this);
			}
			else if(obj==null) {
				setStar();
			}
			//player
			player.draw(g2);
			mon.draw(g2);
//			mon3.draw(g2);
//			mon4.draw(g2);
			if(player.hasStar >= 10) {
				mon2.draw(g2);
			}
			if(player.hasStar >= 20) {
				mon4.draw(g2);
			}
			if(player.hasStar >= 30) {
				mon3.draw(g2);
			}
			//ui
			ui.draw(g2);
		}
		
		g2.dispose();
	}
}
