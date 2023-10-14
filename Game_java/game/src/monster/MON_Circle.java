package monster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import entity.Entity;
import entity.Player;
import main.GamePanel;
import java.util.Random;
public class MON_Circle extends Entity {
	Random random;
    GamePanel gp;
    Player player;
    double angle = Math.toRadians(45);
    int speedx = 10;
    int speedy = 10;
    public MON_Circle(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
        random = new Random();
        solidArea = new Rectangle();
        solidArea.x = 12;
		solidArea.y = 12;
        solidArea.width = 40;
        solidArea.height = 40;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        setDefaultValues();
    }

    private void drawHitbox(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawOval(x + (int) solidArea.x, y + (int) solidArea.y, (int) solidArea.width, (int) solidArea.height);
    }

    public void setDefaultValues() {
//        x = (gp.getWidth()*0);
    	x = (1+random.nextInt((int)(gp.maxscreencol - 2)))*gp.tilesize;
        y = 64;
        imagedirection = "move";
    }

    public void getImage() {
        try {
            walkL1 = ImageIO.read(getClass().getResourceAsStream("/monster/enemyC1.png"));
            walkL2 = ImageIO.read(getClass().getResourceAsStream("/monster/enemyC2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
    	 int newX = (int) (x + Math.cos(Math.PI/4)*speedx);
    	 int newY = (int) (y + Math.sin(Math.PI/4)*speedy);

    	    // Update the angle for the next frame
    	 angle += Math.toRadians(5); // Adjust this value to control the speed of the oval path

    	    // Check if the new position would go beyond the screen boundaries and adjust it
    	 if (newX < 64 || newX + solidArea.width > gp.getWidth()-64) {
    	     speedx = -speedx; // Reverse the x-direction when hitting left or right boundary
    	 }

    	 if (newY < 64 || newY + solidArea.height > gp.getHeight()-64) {
    	     speedy = -speedy; // Reverse the y-direction when hitting top or bottom boundary
    	 }

    	    // Update the monster's position
    	    x = newX;
    	    y = newY;

    	    // Ensure the monster stays within the screen boundaries
    	    x = Math.max(0, Math.min(x, gp.getWidth() - (int) solidArea.width));
            y = Math.max(0, Math.min(y, gp.getHeight() - (int) solidArea.height));

        collisionOn = false;
        // gp.cChecker.checkTile(this);
        boolean objmon = gp.cChecker.checkEntity(this, true);
        if (objmon) {
            gp.gamestate = gp.deadstate;
            System.out.println("oh");
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum++;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (imagedirection) {
            case "move":
                if (spriteNum == 1) {
                    image = walkL1;
                }
                if (spriteNum == 2) {
                    image = walkL2;
                }
                break;
        }
        g2.drawImage(image, x, y, gp.tilesize, gp.tilesize, null);
        drawHitbox(g2);
    }
}
