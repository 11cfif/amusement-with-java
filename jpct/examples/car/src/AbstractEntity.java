import com.threed.jpct.*;

/**
 * This abstract implementation of Entity extends Object3D, so that every subclass of AbstractEntity
 * is an Object3D. That makes a lot of things very easy.
 */
public abstract class AbstractEntity extends Object3D implements Entity {

   protected float speed=0;

   public AbstractEntity(Object3D obj) {
      super(obj);
   }

   /**
    * Make sure that the world knows this entity. Of course, a world can't
    * work with Entity but Object3D. But an AbstractEntity is an Object3D, so
    * it can be added to the world directly. If an AbstractEntity itself
    * contains other Object3Ds, this method have to be overwritten to
    * ensure that all of them will be added correctly. Have a look at the
    * Car.class for an example.
    * @param world the world
    */
   public void addToWorld(World world) {
      world.addObject(this);
   }

   public void moveForward() {
      /**
       * Moving forward means a translation along the z-axis of the Entity
       * in this case with the entity's speed.
       */
      SimpleVector a=this.getZAxis();
      a.scalarMul(speed);
      this.translate(a);
   }

   public void moveBackward() {
      /**
       * The same here...
       */
      SimpleVector a=this.getZAxis();
      a.scalarMul(-speed);
      this.translate(a);
   }

   public float getSpeed() {
      return speed;
   }

   public void setSpeed(float speed) {
      this.speed=speed;
   }
}
