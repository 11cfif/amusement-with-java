import com.threed.jpct.*;
import java.io.*;

/**
 * The PlantManager does two things: Place the plants (done once) and draw the radar
 */
public class PlantManager {

   /**
    * Number of plants
    */
   private final static int MAX_PLANTS=100;

   /**
    * The plants...
    */
   private Plant[] plants;

   /**
    * ...and their positions in world space
    */
   private SimpleVector[] positions;

   /**
    * The plants will get a CollisionListener. One instance for all is sufficient here.
    */
   private final BulletPlantListener LISTENER=new BulletPlantListener();

   private static Texture radar=null;

   static {
      /**
       * The Manager also draws the radar and needs a texture for that task
       */
      radar=new Texture("textures"+File.separatorChar+"radar.jpg");
      TextureManager.getInstance().addTexture("radar", radar);
   }

   /**
    * Creates a new PlantManager. This will place the plants on the ground within the given range
    * (from -range to +range to be exact).
    * @param world the world
    * @param ground the ground
    * @param range the range in world units
    */
   public PlantManager(World world, Object3D ground, float range) {
      plants=new Plant[MAX_PLANTS];
      positions=new SimpleVector[MAX_PLANTS];
      for (int i=0; i<MAX_PLANTS; i++) {
         Plant plant=new Plant();
         plants[i]=plant;
         boolean ok=false;
         do {
            float xpos=(float)(Math.random()-0.5d)*range*2f;
            float zpos=(float)(Math.random()-0.5d)*range*2f;
            plant.setTranslationMatrix(new Matrix());
            plant.translate(xpos, 0, zpos);
            ok=plant.place(ground);
         } while (!ok);
         plant.addToWorld(world);
         plant.addCollisionListener(LISTENER);
      }

      for (int i=0; i<MAX_PLANTS; i++) {
         positions[i]=plants[i].getTransformedCenter();
         /**
          * The positions won't change anymore, so we can do this:
          */
         plants[i].enableLazyTransformations();
      }
   }

   /**
    * Draws the radar. The radar shows the plants' positions relative to the car.
    * @param buffer the FrameBuffer in which the radar should be drawn
    * @param car the car
    */
   public void drawRadar(FrameBuffer buffer, Car car) {
      float xf=0;
      float zf=0;

      SimpleVector carPos=car.getTransformedCenter();
      SimpleVector temp=new SimpleVector();
      float rot=car.getDirection();
      float dx=(float) buffer.getOutputWidth()/10f;
      float dy=(float) buffer.getOutputHeight()/10f;
      float carPosX=(buffer.getOutputWidth()-dx-50);
      float carPosY=dy+50;

      for (int i=0; i<MAX_PLANTS; i++) {

         if (!plants[i].isDead()) {

            temp.x=positions[i].x-carPos.x;
            temp.z=positions[i].z-carPos.z;
            temp.rotateY(rot);

            xf=temp.x;
            zf=temp.z;

            if (xf>-600&&xf<600&&zf>-600&&zf<600) {
               int x=(int) (carPosX+((xf/600f)*(float) dx));
               int z=(int) (carPosY+((zf/600f)*-(float) dy));
               buffer.blit(radar, 0, 0, x, z, 4, 4, FrameBuffer.OPAQUE_BLITTING);
            }
         }
      }
      // Finally, a small red dot is blitted to show where the car is located
      buffer.blit(radar, 0, 0, (int) carPosX, (int) carPosY, 2, 2, FrameBuffer.OPAQUE_BLITTING);
   }
}
