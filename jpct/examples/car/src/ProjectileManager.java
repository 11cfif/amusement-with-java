import com.threed.jpct.*;

/**
 * The ProjectileManager manages bullets and decals. The basic idea behind this is,
 * that keeping such short lived entities in Entity-pools (which the manager actually
 * is) is a good idea...
 */
public class ProjectileManager {

   /**
    * The manager can manage up to 100 bullets...
    */
   private final static int MAX_BULLETS=100;

   /**
    * ...and up to 20 decals.
    */
   private final static int MAX_DECALS=20;

   private Bullet[] bullets=null;
   private Decal[] decals=null;

   /**
    * Creates a new ProjectileManager for this world.
    * @param world the world.
    */
   public ProjectileManager(World world) {
      bullets=new Bullet[MAX_BULLETS];
      decals=new Decal[MAX_DECALS];
      for (int i=0; i<MAX_BULLETS; i++) {
         bullets[i]=new Bullet();
         bullets[i].addToWorld(world);
      }

      for (int i=0; i<MAX_DECALS; i++) {
         decals[i]=new Decal();
         decals[i].addToWorld(world);
      }
   }

   /**
    * Moves all active bullets
    */
   public void moveBullets() {
      for (int i=0; i<MAX_BULLETS; i++) {
         if (bullets[i].getVisibility()) {
            bullets[i].moveForward();
         }
      }
   }

   /**
    * Creates a new bullet (i.e. ask the pool for an available one).
    * @param emitter the entity that fires the bullet
    */
   public void createBullet(AbstractEntity emitter) {
      Bullet bullet=getFreeBullet();
      if (bullet!=null) {
         bullet.enable();
         bullet.fire(emitter);
      }
   }

   /**
    * Creates a new decal (i.e. ask the pool for an available one).
    * @param pos the position of the decal
    * @param normal the normal of the polygon the decal should be applied to
    */
   public void createDecal(SimpleVector pos, SimpleVector normal) {
      Decal decal=getFreeDecal();
      if (decal!=null) {
         Matrix m=normal.getRotationMatrix();
         decal.place(pos);
         decal.rotate(m);
      }
   }

   /**
    * Gets a free bullet from the pool. If there is none, null will be returned.
    * @return the bullet or null
    */
   private Bullet getFreeBullet() {
      for (int i=0; i<MAX_BULLETS; i++) {
         if (!bullets[i].getVisibility()) {
            return bullets[i];
         }
      }
      return null;
   }

   /**
    * Gets a free decal from the pool. If there is none the oldest decal will
    * be replaced with the new one.
    * @return the decal
    */
   private Decal getFreeDecal() {
      long min=Long.MAX_VALUE;
      Decal decal=null;
      for (int i=0; i<MAX_DECALS; i++) {
         if (!decals[i].getVisibility()) {
            return decals[i];
         }

         if (decals[i].getDecalID()<min) {
            min=decals[i].getDecalID();
            decal=decals[i];
         }
      }
      return decal;
   }
}
