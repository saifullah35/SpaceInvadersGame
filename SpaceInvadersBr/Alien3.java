import java.awt.*;

/**
 * Alien type 3 that extends Alein
 * 
 */
public class Alien3 extends Alien{
    protected static Image alienImage;

    public Alien3(Point upperLeft){
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
    public static void loadAlien3Pic() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        alienImage = toolkit.getImage("Alien3.png");
    }

   
}
