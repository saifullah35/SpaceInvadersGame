import java.awt.*;

/**
 * This is a bullet object that is shot from a player
 * 
 * 
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio
 * @version Spring 2022
 */
public class PlayerBullet extends Bullet {

    public PlayerBullet(Component panel, Point currentPos) {
        super(panel);
        upperLeft = new Point(currentPos.x - (bulletWidth / 2), Player.PLAYERYPOS - 5);
    }

    /**
     * Run method to define the life of this bullet
     */
    @Override
    public void run() {
        while (0 < upperLeft.y) {
            // every 30 ms or so, we move the coordinates of bullet
            sleepWithCatch(DELAY_TIME);
            upperLeft.translate(0, -bulletSPEED);
        }
        offPanel = true;
    }

}
