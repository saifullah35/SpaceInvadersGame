import java.awt.Point;
import java.awt.*;
import java.util.*;

public class  EnemyPlayer extends Thread {
    protected Component panel;
    protected Point upperLeftOfEnemyPlayer;
    protected  Point centerOfEnemyPlayer;
    protected static Image enemyPlayerImage;
    protected final static int ENEMYPLAYERYPOS = 50;
    protected final static int ENEMEYHEALTH = 5;
    protected int hits;
    protected static int fireRate = 51;
    protected static int point = 50;
    protected static ArrayList<EnemyBullet> enemiesBulletsList = new ArrayList<>();

    public  EnemyPlayer(Component panel, Point startPos) {
        this.panel = panel;
        this.hits = 0;
        this.upperLeftOfEnemyPlayer = startPos;
        this.centerOfEnemyPlayer = new Point(startPos.x + (Player.PLAYERSIZE / 2),
                upperLeftOfEnemyPlayer.y + (Player.PLAYERSIZE / 2));
    }

    /**
     * Set the Image to be used by the Enemy player Object
     */
    public static void loadEnemyPic() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        enemyPlayerImage = toolkit.getImage("alienshipPlayerTwo.png");
    }

    /**
     * paint this object onto the given Graphics area
     * 
     * @param g the Graphics object where the shape should be drawn
     */
    public void paint(Graphics g) {
        g.drawImage(enemyPlayerImage, upperLeftOfEnemyPlayer.x, upperLeftOfEnemyPlayer.y, Player.PLAYERSIZE,
                Player.PLAYERSIZE, null);
        centerOfEnemyPlayer = new Point(upperLeftOfEnemyPlayer.x + (Player.PLAYERSIZE / 2),
                upperLeftOfEnemyPlayer.y + (Player.PLAYERSIZE / 2));
    }

    /**
     * A relative move of this object.
     * 
     * @param dx amount to translate in x
     */
    public void translate(int dx) {
        upperLeftOfEnemyPlayer.translate(dx, 0);
        if (upperLeftOfEnemyPlayer.x < 0) {
            upperLeftOfEnemyPlayer.x = 0;
        }
        if (upperLeftOfEnemyPlayer.x > SinglePlayer.GAME_PANEL_WIDTH - Player.PLAYERSIZE ) {
            upperLeftOfEnemyPlayer.x = SinglePlayer.GAME_PANEL_WIDTH - Player.PLAYERSIZE;
        }
        centerOfEnemyPlayer = new Point(upperLeftOfEnemyPlayer.x + (Player.PLAYERSIZE / 2),
                upperLeftOfEnemyPlayer.y + (Player.PLAYERSIZE / 2));
    }

    /**
     * Returns the center of the Enemy at any given call.
     * 
     */
    public Point getEnemyCenter() {
        return centerOfEnemyPlayer;
    }


     /**
     * Returns the upperleft of the Enemy at any given call.
     * 
     */
    public Point getUpperLeft(){
        return  upperLeftOfEnemyPlayer;
    }
    
    @Override
    public void run() {
        Random rand = new Random();
        int movementSpeedx = 0;
        movementSpeedx += rand.nextInt(21) - 10;
        while (hits != 5) {
            try {
                sleep(150);
            } catch (InterruptedException e) {
            }
            movementSpeedx += rand.nextInt(21) - 10;
            if (upperLeftOfEnemyPlayer.x == 0) {
                movementSpeedx += rand.nextInt(11);
            }
            if (upperLeftOfEnemyPlayer.x == SinglePlayer.GAME_PANEL_WIDTH -  Player.PLAYERSIZE) {
                movementSpeedx -= rand.nextInt(11);
            }
            translate(movementSpeedx);
            
            int fireBullet =  rand.nextInt(fireRate);
            if( fireBullet % fireRate == 0 ){
                fireBullet();
            }
           
        }
    }

     /**
     * Returns the if the enemy is hit;
     * 
     */
    public void killEnemyPlayer(){
        hits = 5;
    }


     /**
     * Returns the if the enemy is hit;
     * 
     */
    public int getEnemyHitCount() {
        return hits;
    }
    
    
     /**
     * Increamts hits by 1 each time the enemy is hit
     * 
     */
    public void hitEnemy(){
        hits++;
    }
        /**
     * Run method to define the life of this bullet.
     */
    public void fireBullet(){
        EnemyBullet bullet = new EnemyBullet(panel ,
        new Point (upperLeftOfEnemyPlayer.x + (Player.PLAYERSIZE / 2) - (Bullet.bulletWidth / 2), 
        ENEMYPLAYERYPOS + Player.PLAYERSIZE + 5) );
        bullet.start();
        enemiesBulletsList.add(bullet);
    }

}
