import java.util.ArrayList;
import java.awt.*;

/**
 * This is a bullet object that is shot from a Enemyship
 * 
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio
 * @version Spring 2022
 */
public class EnemyBullet extends Bullet {    
    protected static ArrayList<EnemyBullet> enemyBullets = new ArrayList<EnemyBullet>();
    public EnemyBullet(Component panel, Point currentPos) {
        super(panel);
        upperLeft = new Point(currentPos.x- (bulletWidth / 2),currentPos.y ) ;
    }

     /**
     * Run method to define the life of this bullet.
     */
    @Override
    public void run() {
        while (  upperLeft.y < SinglePlayer.GAME_PANEL_HEIGHT  ) {
            // every 30 ms or so, we move the coordinates of bullet
            sleepWithCatch(DELAY_TIME);
            upperLeft.translate(0, bulletSPEED );
        }
        offPanel = true;
    } 
    /**
    * Run method to define the life of this bullet.
    */
   @Override
    public void paint(Graphics g) {
        if( !hit || !offPanel){
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(upperLeft.x, upperLeft.y, bulletWidth,  bulletHeight);
        }
    }

}