import java.awt.Point;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.util.*;

public class MultiPlayer2 {
    protected Point upperLeftOfPlayer2;
    protected  Point centerOfPlayer2;
    protected static BufferedImage enemyPlayerImage;
    protected static BufferedImage originalEnemyPlayerImage;
    protected final static int PLAYER2YPOS = 50;
    protected int lives;
    protected static ArrayList<MultiPlayerBullet> player2BulletsList = new ArrayList<>();
    protected int rotation = 180;
    protected double speed;
    private int shotCooldown;

    public MultiPlayer2(Point startPos) {
        this.lives = 3;
        this.upperLeftOfPlayer2 = new Point(startPos.x, startPos.y);
        this.centerOfPlayer2 = new Point(startPos.x + ((Player.PLAYERSIZE) / 2),
                upperLeftOfPlayer2.y + ((Player.PLAYERSIZE) / 2));
        shotCooldown = 0;
    }

    /**
     * Set the Image to be used by the MultiPlayer2 Object
     */
    public static void loadEnemyPic() {
        try {
            enemyPlayerImage = ImageIO.read(new File("MultiPlayerTwo.png"));
            originalEnemyPlayerImage = enemyPlayerImage;
        } catch (IOException e) {}
    }

    /**
     * paint this object onto the given Graphics area
     * 
     * @param g the Graphics object where the shape should be drawn
     */
    public void paint(Graphics g) {
        g.drawImage(enemyPlayerImage, upperLeftOfPlayer2.x, upperLeftOfPlayer2.y, Player.PLAYERSIZE,
                Player.PLAYERSIZE, null);
        if(DoublePlayer.debugMode) {
            g.setColor(Color.WHITE);
            g.drawString("X: " + Integer.toString(centerOfPlayer2.x) + ",  Y: " + Integer.toString(centerOfPlayer2.y), 700, 715);
            g.drawString("Rotation: " + Integer.toString(rotation), 700, 730);
            g.drawString("Blue Bullets: " + player2BulletsList.size(), 700, 745);
            g.drawRect(upperLeftOfPlayer2.x, upperLeftOfPlayer2.y, Player.PLAYERSIZE, Player.PLAYERSIZE);
        }
    }

    /*
     * Creates a new image by rotating the original image
     * This is to prevent distorting the image by changing a changed image many times
     * Uses a Graphics2D Object to use the rotate() method
     */
    public void rotate(boolean clockwise) {
        // Get Dimensions of image
        int width = enemyPlayerImage.getWidth();
        int height = enemyPlayerImage.getHeight();
        // Create a new buffered image with the same dimentions and type
        BufferedImage newImage = new BufferedImage(
            enemyPlayerImage.getWidth(), enemyPlayerImage.getHeight(), enemyPlayerImage.getType());
        // create Graphics in buffered image
        Graphics2D g2 = newImage.createGraphics();
        if(clockwise) {
            rotation += 3;
            if(rotation >= 360)
                rotation -= 360;
        }
        else {
            rotation -= 3;
            if(rotation < 0)
                rotation += 360;
        }
        g2.rotate(Math.toRadians(rotation-180), width / 2, height / 2);
        g2.drawImage(originalEnemyPlayerImage, 0, 0, null);
        // Return rotated buffer image
        enemyPlayerImage = newImage;
        return;
    }
    
    // Returns current speed
    public double getSpeed(){
        return speed;
    }

    // There is a max speed of 3 / -3
    // Also, due to rounding errors with doubles, speed above +-3 and under +-0.1 are accounted for
    public void modifySpeed(double mod) {
        if(speed > -3 && speed < 3)
            speed += mod;
        if(speed >= 3)
            speed = 2.9;
        if(speed <= -3)
            speed = -2.9;
        if(speed < 0.1 && speed > -0.1)
            speed = 0;
    }

    // Resets the player to original values, at the specified point
    public void reset(Point p) {
        speed = 0;
        rotation = 180;
        enemyPlayerImage = originalEnemyPlayerImage; // resets to original
        upperLeftOfPlayer2 = p;
        lives = 3;
    }

    /**
     * A relative move of this object.
     * 
     * @param dx amount to translate in x
     */
    public void translate(int dx) {
        upperLeftOfPlayer2.translate((int)(speed*dx*Math.cos(Math.toRadians(rotation-90))), (int)(speed*dx*Math.sin(Math.toRadians(rotation-90))));
        if (upperLeftOfPlayer2.x < 0) {
            upperLeftOfPlayer2.x = 0;
        }
        else if (upperLeftOfPlayer2.x > DoublePlayer.GAME_PANEL_WIDTH - Player.PLAYERSIZE) {
            upperLeftOfPlayer2.x = DoublePlayer.GAME_PANEL_WIDTH - Player.PLAYERSIZE;
        }
        if (upperLeftOfPlayer2.y < 0)
            upperLeftOfPlayer2.y = 0;
        else if(upperLeftOfPlayer2.y > DoublePlayer.GAME_PANEL_HEIGHT-Player.PLAYERSIZE)
            upperLeftOfPlayer2.y = DoublePlayer.GAME_PANEL_HEIGHT-Player.PLAYERSIZE;
        centerOfPlayer2 = new Point(upperLeftOfPlayer2.x + ((Player.PLAYERSIZE) / 2),
                upperLeftOfPlayer2.y + ((Player.PLAYERSIZE) / 2));
    }

    /**
     * Returns the center of the Enemy at any given call.
     * 
     */
    public Point getPlayer2Center() {
        return centerOfPlayer2;
    }


     /**
     * Returns the upperleft of the Enemy at any given call.
     * 
     */
    public Point getUpperLeft(){
        return  upperLeftOfPlayer2;
    }

     /**
     * Returns the number of lives the player has
     * 
     */
    public int getPlayer2Lives() {
        return lives;
    }
    
    
     /**
     * Decrements lives if lives is greater than 0
     */
    public void hitPlayer(){
        if(lives > 0)
            lives--;
    }

    /* These methods relate to the shot cooldown
     * The shot cooldown prevent each player from 'spamming' the shoot button
     */
    public int getShotCooldown() {
        return shotCooldown;
    }
    // This is reset when the player shoots
    public void setCooldown(int n) {
        shotCooldown = n;
    }
    // The cooldown is decremented down to zero every repaint
    public void cooldown() {
        if(shotCooldown > 0)
            shotCooldown--;
    }

    /**
     * Creates a bullet at the current rotation about 40 pixels out from the center of the player
     */
    public void fireBullet(Component c){
        if(lives > 0) {
            MultiPlayerBullet bullet = new MultiPlayerBullet(c ,
            new Point (centerOfPlayer2.x + (int)(40*Math.cos(Math.toRadians(rotation-90)))
                , centerOfPlayer2.y + (int)(40*Math.sin(Math.toRadians(rotation-90)))), rotation);
            bullet.start();
            MultiPlayer2.player2BulletsList.add(bullet);
        }
    }

}
