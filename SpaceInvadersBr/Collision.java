/**
 * Checking for a collision between a bullet and a shield, bullet and alien
 * objects, bullet and alein ship or playerTwo , player two bullet and player
 * one bullet. Collison class taken from Jim Teresco breakout modified for our 
 * purposes.
 * 
 * @author Jim Teresco
 * @author Modified by ( Jonathan Masih, Trevor Collins, Saif Ullah, Seth Coluccio,)
 * @version Spring 2022
 */
public class Collision {

   /**
    * Return true if the given bullect overlaps the given Object's location and
    * size.
    * Algorithm based on example at:
    * https://www.gamedevelopment.blog/collision-detection-circles-rectangles-and-polygons/
    * 
    * 
    * 
    * @param bullX   the x-coordinate of the upper-left corner of the bullet
    * @param bullY   the y-coordinate of the upper-left corner of the bullet
    * @param bullW   the width of the bullet
    * @param bullH   the height of the bullet
    * @param objectX x-coordinate of the center of the Object
    * @param objectY y-coordinate of the center of the Object
    * @param objectW the width of the bullet
    * @param objectH the height of the bullet
    * @return true if the given circle overlaps the given rectangle
    */
   public static boolean bulletOverlapsObject(int bullX, int bullY,
         int bullW, int bullH,
         int objectX, int objectY,
         int objectW, int objectH) {
      int objectCX = objectX + objectW / 2;
      int objectCY = objectY + objectH / 2;
      // x and y distances between the bullet and Object
      int dx = Math.abs(bullX - objectCX);
      int dy = Math.abs(bullY - objectCY);

      // check if beyond the bounds of the expanded Object
      if (dx > (objectW / 2 + bullW))
         return false;
      if (dy > (objectH / 2 + bullH))
         return false;

      // is circle center within the expanded Object
      if (dx <= objectW / 2)
         return true;
      if (dy <= objectH / 2)
         return true;

      // check corners
      int cornerDistSq = ((dx - objectW / 2) * (dx - objectW / 2) +
            (dy - objectH / 2) * (dy - objectH / 2));
      if (cornerDistSq <= bullW * bullH)
         return true;

      // must not collide
      return false;
   }
}
