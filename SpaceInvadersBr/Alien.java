import java.awt.*;
import java.util.Random;

/**
 * This is an alien object which a player can shoot and earn points.
 * This alien will also be a a sheild for the enemy ship.
 * 
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio
 * @version Spring 2022
 */
public abstract class Alien extends Thread {
    protected static Image alienImage;
    protected Point upperLeftOfAlien;
    protected Point centerOfAlien;
    protected boolean alienHit;
    protected static int movementSpeedx = 1;
    protected static boolean hitLeftBorder = false;
    protected static boolean hitRighttBorder = false;
    protected static int point = 50;
    protected static final int ALIENSIZE = 50;
    protected static final int ALIENYPOS1 = 400;
    protected static final int ALIENYPOS2 = 300;
    protected static final int ALIENYPOS3 = 200;

    public Alien(Point upperLeft) {
        this.upperLeftOfAlien = upperLeft;
        this.centerOfAlien = new Point(upperLeft.x + (ALIENSIZE / 2),
                upperLeft.y + (ALIENSIZE / 2));

        this.alienHit = false;
    }

    public void paint(Graphics g) {
        if (!alienHit) {
            g.drawImage(alienImage, upperLeftOfAlien.x, upperLeftOfAlien.y, ALIENSIZE, ALIENSIZE, null);
            centerOfAlien = new Point(upperLeftOfAlien.x + (Alien.ALIENSIZE / 2),
                    upperLeftOfAlien.y + (Alien.ALIENSIZE / 2));
        }
    }

    /**
     * Returns the center of the alien at any given call.
     * 
     */
    public boolean isAlienHit() {
        return alienHit;
    }

    /**
     * Returns if the alien has been hit or not.
     * 
     */
    public Point getPlayerCenter() {
        return centerOfAlien;
    }

    /**
     * Set the alienHit to true so we stop drawing it
     * 
     */
    public void hitAlien() {
        alienHit = true;
    }

    /**
     * Return upperleft of the bullet
     * 
     * @return the upperleft of bullet
     */
    public Point getUpperLeft() {
        return upperLeftOfAlien;
    }

   /**
     * A relative move of this object.
     * 
     * @param dx amount to translate in x
     */
    public void translate(int dx) {
        upperLeftOfAlien.translate(dx, 0);
        if (upperLeftOfAlien.x < 0) {
            upperLeftOfAlien.x = 0;
        }
        if (upperLeftOfAlien.x > SinglePlayer.GAME_PANEL_WIDTH - Alien.ALIENSIZE ) {
            upperLeftOfAlien.x = SinglePlayer.GAME_PANEL_WIDTH - Alien.ALIENSIZE;
        }
    }

    @Override
    public void run() {
        Random rand = new Random();
        int movementSpeedx = 0;
        movementSpeedx += rand.nextInt(21) - 10;
        while (!alienHit) {
            try {
                sleep(150);
            } catch (InterruptedException e) {
            }
            movementSpeedx += rand.nextInt(21) - 10;
            if (upperLeftOfAlien.x == 0) {
                movementSpeedx += rand.nextInt(11);
            }
            if (upperLeftOfAlien.x == SinglePlayer.GAME_PANEL_WIDTH -  Alien.ALIENSIZE) {
                movementSpeedx -= rand.nextInt(11);
            }
            translate(movementSpeedx);
            
            
           
        }
    }

}
