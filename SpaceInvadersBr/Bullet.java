import java.awt.*;


/**
 * This is a bullet object that is shot from a player or
 * an enemey ship. An abstract class that will hold an 
 * object.
 * 
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio
 * @version Spring 2022
 */
public abstract class Bullet extends Thread {
   // the Component where the bullet is being fired
   protected  Component panel;
   protected Point upperLeft;
   protected boolean  offPanel;
   protected boolean hit;
   protected static final int bulletHeight = 10;
   protected static final int bulletWidth = 6;
   protected final int bulletSPEED = 10;
   protected final int DELAY_TIME = 15;

   public Bullet(Component panel) {
    this.panel = panel;
    this.offPanel = false; 
    this.hit = false;
   }

   /**
     * Run method to define the life of this FallingSnow, which consists of
     * falling down to the bottom of the component, then sitting there for
     * a little while before melting.
     */
    @Override
    public void run() {
        while ( 0 < upperLeft.y  ) {
            // every 30 ms or so, we move the coordinates of bullet
            try {
                sleep(DELAY_TIME);
            } catch (InterruptedException e) {
            }
            upperLeft.translate(0, -bulletSPEED );
            //Repaint
        }
        offPanel = true;
    }

    /**
     * Paints the bullet object at its current position, if no collision is made
     * 
     * @param g the Graphics object in which to paint
     */
    public void paint(Graphics g) {
        if( !hit || !offPanel){
            g.setColor(Color.WHITE);
            g.fillRect(upperLeft.x, upperLeft.y, bulletWidth,  bulletHeight);
        }
    }

     /**
     * Set hit to true if the bullet hit an object.
     * 
     */
    public void bulletHit() {
        hit = true;
    }

    /**
     * returns if the bullet is off the panel or not
     * 
     * @param g the Graphics object in which to paint
     */
    public boolean isOffPanel(){
        return offPanel;
    }

     /**
     * returns if the bullet has hit an object or not
     * 
     * @return true if the bullet hit an object
     */
    public boolean isHit(){
        return hit;
    }

    
     /**
     * Return upperleft of the bullet
     * 
     * @return the upperleft of bullet
     */
    public Point getUpperLeft() {
        return upperLeft;
    }

    /**
     * A utility method to have the thread sleep without the need to
     * place the call in a try-catch block.
     * 
     * @param millis the number of milliseconds for the thread to sleep
     */
    public static void sleepWithCatch(long millis) {

        try {
            sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}