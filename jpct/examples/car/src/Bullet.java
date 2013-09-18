import com.threed.jpct.*;
import java.io.*;

/**
 * A bullet can be fired by the car. Even if everything within here is public, in this example
 * Bullets are managed by using the ProjectileManager.
 */
public class Bullet extends AbstractEntity {

   /**
    * The distance the bullet may have from the emitter
    * before it "dies".
    */
   private final static float MAX_DISTANCE=1000;

   private final static float SPEED=20;

   private static Object3D BULLET=null;

   private SimpleVector startPos=null;

   static {
      /**
       * Load all the textures required by this entity and initialize
       * a "blue print" for bullets.
       */
      Texture tex=new Texture("textures"+File.separatorChar+"spot.jpg");
      TextureManager.getInstance().addTexture("bullet", tex);
      BULLET=Primitives.getSphere(4, 2);
      BULLET.setTexture("bullet");
      BULLET.calcTextureWrapSpherical();
      BULLET.build();
   }

   /**
    * A Bullet is a small sphere. It's invisible after creation.
    */
   public Bullet() {
      super(BULLET);
      setCollisionMode(Object3D.COLLISION_CHECK_SELF);
      setVisibility(false);
      setSpeed(SPEED);
   }

   /**
    * Overwrites moveForward() from AbstractEntity, because we need to do some additional
    * work like collision detection on the bullets when moving them. The collision detection is
    * performed by using the ray-polygon-approach. This may not be the best choice to check for
    * collision with the plants but anyway...
    */
   public void moveForward() {
      if (getVisibility()) {
         int objID=checkForCollision(getZAxis(), getSpeed());
         if (objID==Object3D.NO_OBJECT) {
            // No collision? Then just move on
            super.moveForward();
            if (getTransformedCenter().calcSub(startPos).length()>MAX_DISTANCE) {
               /**
                * The bullet has reached the maximum distance from the emitter?
                * In that case, we disable it.
                */
               disable();
            }
         }
      }
   }

   /**
    * Enables the bullet. This is simply done by using the Object3D's visibility.
    */
   public void enable() {
      setVisibility(true);
   }

   /**
    * Disables the bullet. This is simply done by using the Object3D's visibility.
    */
   public void disable() {
      setVisibility(false);
   }

   /**
    * Fires the bullet from the emitter. This requires that the bullet is placed and
    * aligned like the emitting entity.
    * @param emitter the emitting (i.e. firing) entity
    */
   public void fire(AbstractEntity emitter) {
      align(emitter);
      setTranslationMatrix(new Matrix());
      setOrigin(emitter.getTransformedCenter());
      startPos=emitter.getTransformedCenter();
   }
}
