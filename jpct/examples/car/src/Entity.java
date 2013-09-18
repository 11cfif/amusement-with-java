import com.threed.jpct.*;

/**
 * An entity of the "game world", like the car, a bullet...
 */
public interface Entity {

   /**
    * Makes sure that the world knows this entity. Of course, a world can't
    * work with Entity but Object3D. Have a look at AbstractEntity to see
    * how this example deals with it.
    * @param world the world
    */
   void addToWorld(World world);

   /**
    * Moves the entity forward
    */
   void moveForward();

   /**
    * Moves the entity backwards
    */
   void moveBackward();

   /**
    * Gets the speed of the entity
    * @return the speed
    */
   float getSpeed();

   /**
    * Sets the speed of the entity
    * @param speed the speed
    */
   void setSpeed(float speed);
}
