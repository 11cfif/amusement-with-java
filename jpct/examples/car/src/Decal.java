import com.threed.jpct.*;
import java.io.*;

/**
 * A decal is what remains if a bullets hits the ground. Decals are managed by the ProjectileManager.
 */
public class Decal extends AbstractEntity {

   private static long counter=0;
   private static Object3D DECAL=null;
   private long id=0;

   static {
      /**
       * Load all the textures required by this entity and initialize
       * a "blue print" for decals.
       */
      Texture tex=new Texture("textures"+File.separatorChar+"decal.jpg");
      TextureManager.getInstance().addTexture("decal", tex);
      DECAL=Primitives.getPlane(1,30);
      DECAL.invert();
      DECAL.setTexture("decal");
      DECAL.getMesh().compress();
      DECAL.setTransparency(3);
      DECAL.build();
   }

   /**
    * A decal is a plane with a partially transparent texture.
    */
   public Decal() {
      super(DECAL);
      setVisibility(false);
   }

   /**
    * A shortcut to Object3D's setRotationMatrix
    * @param m the rotation matrix
    */
   public void rotate(Matrix m) {
      setRotationMatrix(m);
   }

   /**
    * Places the decal
    * @param pos the position of the decal
    */
   public void place(SimpleVector pos) {
      setVisibility(true);
      setOrigin(pos);
      id=counter;
      counter++;
   }

   /**
    * Returns the id. The id is used to determine the age of a decal.
    * @return the id
    */
   public long getDecalID() {
      return id;
   }
}
