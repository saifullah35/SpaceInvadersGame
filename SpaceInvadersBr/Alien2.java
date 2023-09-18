import java.awt.*;

/**
 * Alein type 2 that extends Alein
 * 
 */
public class Alien2 extends Alien{
    protected static Image alienImage;

    public Alien2(Point upperLeft){
        super(upperLeft);
    }
  /**
     * paint this object onto the given Graphics area
     * 
     * @param g the Graphics object where the shape should be drawn
     */
    @Override
    public void paint(Graphics g) {
        if (!alienHit) {
            g.drawImage(alienImage, upperLeftOfAlien.x, upperLeftOfAlien.y, ALIENSIZE, ALIENSIZE, null);
        }
    }

  
    /**
     * Set the Image to be used by the Alien Object
     */
    public static void loadAlien2Pic() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        alienImage = toolkit.getImage("Alien2.png");
    }

    
}
