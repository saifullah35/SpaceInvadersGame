import java.util.ArrayList;
import java.awt.*;

/**
 * This is a bullet object that is shot from either ship in Multi-Player mode
 * 
 * @author Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio, Tyler Streithorst
 * @version Spring 2022
 */
public class MultiPlayerBullet extends Bullet {    
    private int rotationInt;
    private Point centerPoint;
    public MultiPlayerBullet(Component panel, Point currentPos, int rotationInt) {
        super(panel);
        this.rotationInt = rotationInt;
        centerPoint = new Point(currentPos.x, currentPos.y) ;
    }

    /*
     * This is used for the hitbox, and is derived from the centerpoint
     */
    @Override
    public Point getUpperLeft() {
        return new Point(centerPoint.x-6, centerPoint.y-6);
    }

     /**
     * Run method to define the life of this bullet.
     */
    @Override
    public void run() {
        while (centerPoint.x > 0 && centerPoint.x < DoublePlayer.GAME_PANEL_WIDTH && centerPoint.y < SinglePlayer.GAME_PANEL_HEIGHT  && centerPoint.y > 0) {
            // every 30 ms or so, we move the center point of bullet
            sleepWithCatch(DELAY_TIME);
            centerPoint.translate((int)(10*Math.cos(Math.toRadians(rotationInt-90))), (int)(10*Math.sin(Math.toRadians(rotationInt-90))));
        }
        offPanel = true;
    }

    /*
     * This code creates 4 points around the centerpoint, and draws the shape to the graphics object
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        int[] Xs = {(int)(centerPoint.x-6*Math.cos(Math.toRadians(rotationInt-90-30))), (int)(centerPoint.x-6*Math.cos(Math.toRadians(rotationInt-90+30))),
            (int)(centerPoint.x-6*Math.cos(Math.toRadians(rotationInt+90-30))), (int)(centerPoint.x-6*Math.cos(Math.toRadians(rotationInt+90+30)))};
        int[] Ys = {(int)(centerPoint.y-6*Math.sin(Math.toRadians(rotationInt-90-30))), (int)(centerPoint.y-6*Math.sin(Math.toRadians(rotationInt-90+30))),
            (int)(centerPoint.y-6*Math.sin(Math.toRadians(rotationInt+90-30))), (int)(centerPoint.y-6*Math.sin(Math.toRadians(rotationInt+90+30)))};
        g.fillPolygon(Xs, Ys, 4);
        if(DoublePlayer.debugMode)
            g.drawRect(centerPoint.x-5, centerPoint.y-5, 5, 5);
    }

}