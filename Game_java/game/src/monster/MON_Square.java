package monster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.Entity;
import entity.Player;
import main.GamePanel;

import object.SuperObject;

public class MON_Square extends Entity{
	
	SuperObject star;
	GamePanel gp;
	Player player;

	public MON_Square(GamePanel gp,Player player,SuperObject star) {
		this.gp = gp;
		this.player = player;
		speed = 1;
		solidArea = new Rectangle();
		solidArea.x = 14;
		solidArea.y = 0;
		solidArea.width = 35;
		solidArea.height = 64;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		getImage();
		setDefaultValues();
	}
	private void drawHitbox(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawRect(x + (int) solidArea.x, y + (int) solidArea.y, (int) solidArea.width, (int) solidArea.height);
    }
	public void setDefaultValues() {
		x = (1*gp.tilesize);
		y = (1*gp.tilesize);
		speed = 3;
		imagedirection = "move";
	}
	public void getImage() {
		try {
			walkL1 = ImageIO.read(getClass().getResourceAsStream("/monster/enemySQ1.png"));
			walkL2 = ImageIO.read(getClass().getResourceAsStream("/monster/enemySQ2.png"));
			walkL3 = ImageIO.read(getClass().getResourceAsStream("/monster/enemySQ3.png"));
			walkL4 = ImageIO.read(getClass().getResourceAsStream("/monster/enemySQ4.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		if (gp.obj != null) {
	        int deltaX = gp.obj.worldX - x;
	        int deltaY = gp.obj.worldY - y;
	        
	        // Calculate the angle between the monster and the object
	        double angle = Math.atan2(deltaY, deltaX);
	        
	        // Calculate the new position based on the angle and speed
	        int newX = (int) (x + speed * Math.cos(angle));
	        int newY = (int) (y + speed * Math.sin(angle));
	        
	        // Update the monster's position
	        x = newX;
	        y = newY;
	        
	        // Check for collision with the object
	        boolean objmon = gp.cChecker.checkEntity(this, true);
	        boolean objBool = gp.cChecker.checkObject(this, true);
	        
	        if (objBool) {
	            decreaseStar();
	        }
	        if (objmon) {
	            gp.gamestate = gp.deadstate;
	            System.out.println("oh");
	        }
	    }
			
		spriteCounter++;
			if(spriteCounter > 10) {
				if(spriteNum == 1) {
					spriteNum++;
				}
				else if(spriteNum == 2) {
					spriteNum++;
				}
				else if(spriteNum == 3) {
					spriteNum++;
				}
				else if(spriteNum == 4) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
	}
	
	public void decreaseStar() {
		player.hasStar--;
		gp.obj = null;
		System.out.println(player.hasStar);	
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		
		switch(imagedirection) {
		case "move":
			if(spriteNum == 1) {
				image = walkL1;
			}
			if(spriteNum == 2) {
				image = walkL2;
			}
			if(spriteNum == 3) {
				image = walkL3;
			}
			if(spriteNum == 4) {
				image = walkL4;
			}
			break;
		}
		g2.drawImage(image,x,y,gp.tilesize,gp.tilesize,null);
		drawHitbox(g2);
	}
}
