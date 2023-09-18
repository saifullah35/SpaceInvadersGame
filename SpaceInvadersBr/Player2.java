import java.awt.Point;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.util.*;

public class  Player2 {
    protected Point upperLeftOfPlayer2;
    protected  Point centerOfPlayer2;
    protected static Image enemyPlayerImage;
    protected final static int PLAYER2YPOS = 50;
    protected int lives;
    protected static ArrayList<EnemyBullet> enemiesBulletsList = new ArrayList<>();
    protected boolean rotatingClockwise = false;

    public Player2(Point startPos) {
        this.lives = 5;
        this.upperLeftOfPlayer2 = startPos;
        this.centerOfPlayer2 = new Point(startPos.x + (Player.PLAYERSIZE / 2),
                upperLeftOfPlayer2.y + (Player.PLAYERSIZE / 2));
    }

    /**
     * Set the Image to be used by the Enemy player Object
     */
    public static void loadEnemyPic() {

        BufferedImage img = null;
        try {
            enemyPlayerImage = ImageIO.read(new File("alientshipPlayerTwo.png"));
        } catch (IOException e) {}
    }

    /**
     * paint this object onto the given Graphics area
     * 
     * @param g the Graphics object where the shape should be drawn
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if(rotatingClockwise)
            g2d.rotate(Math.toRadians(5));
        g.drawImage(enemyPlayerImage, upperLeftOfPlayer2.x, upperLeftOfPlayer2.y, Player.PLAYERSIZE,
                Player.PLAYERSIZE, null);
    }

    public void rotate(boolean clockwise) {
        if(clockwise) {
            
        }
    }

    /**
     * A relative move of this object.
     * 
     * @param dx amount to translate in x
     */
    public void translate(int dx) {
        upperLeftOfPlayer2.translate(dx, 0);
        if (upperLeftOfPlayer2.x < 0) {
            upperLeftOfPlayer2.x = 0;
        }
        if (upperLeftOfPlayer2.x > SinglePlayer.GAME_PANEL_WIDTH - 5 * (Player.PLAYERSIZE / 2)) {
            upperLeftOfPlayer2.x = SinglePlayer.GAME_PANEL_WIDTH - 5 * (Player.PLAYERSIZE / 2);
        }
        centerOfPlayer2 = new Point(upperLeftOfPlayer2.x + (Player.PLAYERSIZE / 2),
                upperLeftOfPlayer2.y + (Player.PLAYERSIZE / 2));
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
     * Returns the if the enemy is hit;
     * 
     */
    public int getPlayer2Lives() {
        return lives;
    }
    
    
     /**
     * Increamts hits by 1 each time the enemy is hit
     * 
     */
    public void hitPlayer(){
        lives--;
    }
        /**
     * Run method to define the life of this bullet.
     */
    public void fireBullet(Component c){
        EnemyBullet bullet = new EnemyBullet(c ,
        new Point (centerOfPlayer2.x, centerOfPlayer2.y + 50));
        bullet.start();
        EnemyPlayer.enemiesBulletsList.add(bullet);
    }

}
