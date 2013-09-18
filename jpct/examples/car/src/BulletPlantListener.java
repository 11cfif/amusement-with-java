import com.threed.jpct.*;

/**
 * A CollisionListener that will be notified when a bullet hits a plant. This
 * listener will be added to every plant.
 */
public class BulletPlantListener implements CollisionListener {
   public boolean requiresPolygonIDs() {
      // not needed in this case...
      return false;
   }

   public void collision(CollisionEvent e) {
      if (e.getType()==CollisionEvent.TYPE_TARGET && e.getSource()!=null) {
         if (e.getSource() instanceof Bullet) {
            // The collision has really been caused by a bullet!
            Bullet bullet=(Bullet) e.getSource();
            Plant plant=(Plant) e.getObject();
            plant.destroy();
            bullet.disable();
         }
      }
   }
}
