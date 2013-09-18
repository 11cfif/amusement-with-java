import com.threed.jpct.*;

/**
 * A CollisionListener that will be notified when a bullet hits the ground. This
 * listener will be added to the ground, not to every bullet.
 */
public class BulletTerrainListener implements CollisionListener {

   private ProjectileManager bulMan=null;

   public BulletTerrainListener(ProjectileManager bulMan) {
      this.bulMan=bulMan;
   }

   public boolean requiresPolygonIDs() {
      // We really need them...
      return true;
   }

   public void collision(CollisionEvent e) {
      /**
       * Make sure that something collided with the ground and that this
       * was an actual entity (and not the camera or whatever).
       */
      if (e.getType()==CollisionEvent.TYPE_TARGET && e.getSource()!=null) {
         Object obj=e.getSource();
         /**
          * Make sure, that the source of the collision was really
          * a bullet.
          */
         if (obj instanceof Bullet) {
            Bullet bullet=(Bullet) obj;
            bullet.disable();
            Object3D ground=e.getObject();

            /**
             * Here, we have to calculate the position and orientation of the decal the
             * bullet leaves at the ground. This requires a call to calcMinDistance() and
             * to make sure that we are not triggering the CollisionListener again in that case,
             * we temporally disable collision events on the ground.
             */
            ground.disableCollisionListeners();
            SimpleVector za=bullet.getZAxis();
            SimpleVector tc=bullet.getTransformedCenter();
            float d=ground.calcMinDistance(tc, za, bullet.getSpeed()*10);
            if (d!=Object3D.COLLISION_NONE) {
               za.scalarMul(d*0.6f);
               tc.add(za);
               int[] ids=e.getPolygonIDs();
               if (ids!=null&&ids.length>0) {
                  int id=ids[0];
                  SimpleVector n=e.getObject().getPolygonManager().getTransformedNormal(id);
                  bulMan.createDecal(tc, n);
               }
            }
            /**
             * And on again...
             */
            ground.enableCollisionListeners();
         }
      }
   }
}
