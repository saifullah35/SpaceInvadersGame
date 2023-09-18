import java.awt.*;

/**
 * Alein type 1 that extends Alien
 * 
 */
public class Alien1 extends Alien{
    
    protected static Image alienImage;
    public Alien1(Point upperLeft){
        super(upperLeft);
        super.hitLeftBorder = true;
        super.hitRighttBorder = false;
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
    public static void loadAlienPic() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        alienImage = toolkit.getImage("Alien.png");
    }

    

}
