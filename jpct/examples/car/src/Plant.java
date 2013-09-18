import com.threed.jpct.*;
import java.io.*;
import java.awt.*;

/**
 * A plant has to be placed on the ground and can be shot.
 */
public class Plant extends AbstractEntity {

  private final static Object3D PLANT;
  private boolean dead=false;


  static {
     /**
      * Load all the textures required by this entity and initialize
      * a "blue print" for plants.
      */
     Texture tex=new Texture("textures"+File.separatorChar+"plant.jpg");
     TextureManager.getInstance().addTexture("plant", tex);
     Texture tex2=new Texture("textures"+File.separatorChar+"plant2.jpg");
     TextureManager.getInstance().addTexture("plant2", tex2);
     Object3D[] objs=Loader.load3DS("models"+File.separatorChar+"plant.3ds",2.5f);
     if (objs.length==1) {
        PLANT=objs[0];
        PLANT.setTexture("plant");
        PLANT.setTransparency(2);
        PLANT.setCulling(Object3D.CULLING_DISABLED);
        PLANT.rotateX(-(float)Math.PI/2f);
        PLANT.rotateMesh();
        PLANT.setRotationMatrix(new Matrix());
        PLANT.setAdditionalColor(new Color(100,100,100));
        PLANT.build();
     }
     else {
        // Opps...this shouldn't happen...
        Logger.log("Wrong file format for plants!", Logger.ERROR);
        PLANT=null;
     }
  }

  /**
   * Creates a new plant.
   */
  public Plant() {
     super(PLANT);
     setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
     setCollisionOptimization(Object3D.COLLISION_DETECTION_OPTIMIZED);
  }

  /**
   * Place the plant. Placing in this case means that the plant will be dropped to the ground
   * from it's current position (which has to be above the ground).
   * @param ground the ground
   * @return success...or not
   */
  public boolean place(Object3D ground) {

     SimpleVector dropDown=new SimpleVector(0, 1, 0);
     float height=ground.calcMinDistance(getTransformedCenter(), dropDown, 500);

     if (height!=Object3D.COLLISION_NONE) {
        dropDown.scalarMul(height+getCenter().y);
        translate(dropDown);
        enableLazyTransformations();
        return true;
     } else {
        return false;
     }
  }

  /**
   * "Destroys" the plant. The only thing that happens when destroying a plant is, that the
   * texture changes and the plant can no longer be hit by bullets
   */
  public void destroy() {
     setTexture("plant2");
     dead=true;
     setCollisionMode(Object3D.COLLISION_CHECK_NONE);
     disableCollisionListeners();
  }

  /**
   * Returns is the plant is dead, i.e. if it has been destroyed
   * @return dead or not
   */
  public boolean isDead() {
     return dead;
  }
}
