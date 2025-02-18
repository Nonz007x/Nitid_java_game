package entity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import static util.constant.Confix.*;
public class Player extends Entity{
	private boolean isDashing = false;
    private long lastDashTime = 0;
    private final long dashCooldown = 1000;
	GamePanel gp;
	KeyHandler keyH;
	public int hasStar = 0;
	
	public Player(GamePanel gp,KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		solidArea = new Rectangle();
		solidArea.x = 24;
		solidArea.y = 28;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 16;
		solidArea.height = 16;
		setDefaultValues();
		getPlayerImage();
	}
	private void drawHitbox(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawRect(x + (int) solidArea.x, y + (int) solidArea.y, (int) solidArea.width, (int) solidArea.height);
    }
	public void setDefaultValues() {
		x = screenwidth/2;
		y = screenheight/2;
		speed = 6;
		direction = "stayR";
		imagedirection = "stayR";
	}
	public void getPlayerImage() {
		try {	
			stayR = ImageIO.read(getClass().getResourceAsStream("/player/stayright.png"));
			stayL = ImageIO.read(getClass().getResourceAsStream("/player/playerStay.png"));
			walkL1 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkleft1.png"));
			walkL2 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkleft2.png"));
			walkL3 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkleft3.png"));
			walkL4 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkleft4.png"));
			walkR1 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkright1.png"));
			walkR2 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkright2.png"));
			walkR3 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkright3.png"));
			walkR4 = ImageIO.read(getClass().getResourceAsStream("/player/playerWalkright4.png"));
			titlepict = ImageIO.read(getClass().getResourceAsStream("/player/boss.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		int X=0;
		int Y=0;

		gp.cChecker.checkTile(this);
		if(keyH.upPressed || keyH.downPressed ||
				keyH.leftPressed || keyH.rightPressed) {
			if(keyH.upPressed && !isCollisionUp) {
				direction = "up";
				Y -= speed;
				if("stayL".equals(imagedirection)) {
					imagedirection = "left";
				}
				if("stayR".equals(imagedirection)) {
					imagedirection = "right";
				}
				
			}
			if(keyH.downPressed && !isCollisionDown) {
				direction = "down";
				Y += speed;
				if("stayL".equals(imagedirection)) {
					imagedirection = "left";
				}
				if("stayR".equals(imagedirection)) {
					imagedirection = "right";
				}
				
			}
			if(keyH.leftPressed && !isCollisionLeft) {
				direction = "left";
				X -= speed;
				imagedirection = "left";
				
			}
			if(keyH.rightPressed && !isCollisionRight) {
				direction = "right";
				X += speed;
				imagedirection = "right";
				
			}

			if (keyH.shiftPressed && !isDashing && (System.currentTimeMillis() - lastDashTime) >= dashCooldown) {
	            isDashing = true;
	            lastDashTime = System.currentTimeMillis();
	            speed = 10; // Set a higher speed for dashing
	        } else {
	            if (!isDashing) {
	                speed = 6; // Set the regular speed
	            }
	        }

	        if (isDashing && (System.currentTimeMillis() - lastDashTime) >= dashCooldown) {
	            isDashing = false;
	            speed = 6; // Reset the speed after dashing cooldown
	        }

			boolean objBool = gp.cChecker.checkObject(this,true);
			if(objBool) {
				pickStar();
			}
			//if collision is false can move
			x += X;
			y += Y;

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
		else {
			if(imagedirection=="left"||imagedirection=="up") {
				imagedirection = "stayL";
			}
			if(imagedirection=="right"||imagedirection=="down") {
				imagedirection = "stayR";
			}
		}
		
	}
	
	public void pickStar() {
			hasStar++;
			gp.obj = null;
			System.out.println(hasStar);	
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		switch(imagedirection) {
		case "left":
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
		case "right":
			if(spriteNum == 1) {
				image = walkR1;
			}
			if(spriteNum == 2) {
				image = walkR2;
			}
			if(spriteNum == 3) {
				image = walkR3;
			}
			if(spriteNum == 4) {
				image = walkR4;
			}
			break;
		case "stayR":
			image = stayR;
			break;
		case "stayL":
			image = stayL;
			break;
		}
		g2.drawImage(image,x,y,tilesize,tilesize,null);
//		drawHitbox(g2);
	}
}
