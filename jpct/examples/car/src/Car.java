import com.threed.jpct.*;
import java.io.*;

/**
 * The car entity. This is the most complex entity in this example, because it also
 * contains the wheels.
 */
public class Car extends AbstractEntity {

   /**
    * The car consists of 5 parts: The "body" (which is the object itself) and
    * the 4 wheels.
    */
   private Object3D leftFront=null;
   private Object3D leftRear=null;
   private Object3D rightFront=null;
   private Object3D rightRear=null;

   private float yRot=0;

   static {
      /**
      * Load all the textures required by this entity
      */
      Texture spot=new Texture("textures"+File.separatorChar+"spot.jpg");
      TextureManager.getInstance().addTexture("car", spot);
   }

   public Car() {
      /**
       * The car's "body" is this instance itself. That may seem
       * a bit strange at first glance, but it's quite convenient
       * in this case.
       */
      super(Primitives.getBox(8, 0.25f));
      rightFront=Primitives.getSphere(5, 4);
      leftFront=Primitives.getSphere(5, 4);
      rightRear=Primitives.getSphere(5, 4);
      leftRear=Primitives.getSphere(5, 4);

      /**
       * The wheels are parts, i.e. children of the car
       */
      addChild(rightFront);
      addChild(leftFront);
      addChild(rightRear);
      addChild(leftRear);

      /**
       * Initialize the car and the wheels
       */
      setTexture("car");
      rightFront.setTexture("car");
      leftFront.setTexture("car");
      rightRear.setTexture("car");
      leftRear.setTexture("car");

      setEnvmapped(Object3D.ENVMAP_ENABLED);
      rightFront.setEnvmapped(Object3D.ENVMAP_ENABLED);
      leftFront.setEnvmapped(Object3D.ENVMAP_ENABLED);
      rightRear.setEnvmapped(Object3D.ENVMAP_ENABLED);
      leftRear.setEnvmapped(Object3D.ENVMAP_ENABLED);

      /**
       * We need to offset the wheels a little...
       */
      rightFront.translate(new SimpleVector(-8, 4, 8));
      rightRear.translate(new SimpleVector(-8, 4, -8));
      leftFront.translate(new SimpleVector(8, 4, 8));
      leftRear.translate(new SimpleVector(8, 4, -8));

      rightFront.translateMesh();
      rightRear.translateMesh();
      leftFront.translateMesh();
      leftRear.translateMesh();

      rightFront.setTranslationMatrix(new Matrix());
      rightRear.setTranslationMatrix(new Matrix());
      leftFront.setTranslationMatrix(new Matrix());
      leftRear.setTranslationMatrix(new Matrix());

      /**
       * ...the wheels are now in place. We can now build the car.
       */
      build();
      rightRear.build();
      rightFront.build();
      leftRear.build();
      leftFront.build();
   }

   /**
    * Add all parts of the car to the world.
    */
   public void addToWorld(World world) {
      super.addToWorld(world);
      world.addObject(rightFront);
      world.addObject(rightRear);
      world.addObject(leftFront);
      world.addObject(leftRear);
   }

   /**
    * Places the car. The car has to follow the ground. This is done by casting 4 rays (one from
    * each wheel) to the ground and calculating the resulting rotation angles to let the car
    * follow the ground as close as possible.
    * @param ground the ground
    * @return if it was possible to place the car or not
    */
   public boolean place(Object3D ground) {
      SimpleVector dropDown=new SimpleVector(0, 1, 0);

      /**
       * To cast the rays, the car will be rotated in horizontal position first, rotated around the
       * y-axis according to the cars direction and moved 10 units up.
       */
      Matrix rotMat=getRotationMatrix();
      rotMat.setIdentity();
      setRotationMatrix(rotMat);

      rotateY(yRot);
      translate(0, -10, 0);

      /**
       * Cast the rays...
       */
      float rightFrontHeight=ground.calcMinDistance(rightFront.getTransformedCenter(), dropDown, 4*30);
      float rightRearHeight=ground.calcMinDistance(rightRear.getTransformedCenter(), dropDown, 4*30);
      float leftFrontHeight=ground.calcMinDistance(leftFront.getTransformedCenter(), dropDown, 4*30);
      float leftRearHeight=ground.calcMinDistance(leftRear.getTransformedCenter(), dropDown, 4*30);

      /**
       * Correct the movement we did above.
       */
      translate(0, 10, 0);

      /**
       * Reset the rotation matrix again.
       */
      rotMat=getRotationMatrix();
      rotMat.setIdentity();
      setRotationMatrix(rotMat);

      /**
       * The rays all hit the ground, the car can be placed
       */
      if (rightFrontHeight!=Object3D.COLLISION_NONE&&
          rightRearHeight!=Object3D.COLLISION_NONE&&
          leftFrontHeight!=Object3D.COLLISION_NONE&&
          leftRearHeight!=Object3D.COLLISION_NONE) {

         /**
          * Correct the values (see translation above)
          */
         rightFrontHeight-=10;
         rightRearHeight-=10;
         leftFrontHeight-=10;
         leftRearHeight-=10;

         /**
          * Calculate the angles between the wheels and the ground.
          * This is done four times: for the front and the rear
          * as well as for left and right. Front/rear and left/right
          * are averaged afterwards.
          */
         double angleFront=rightFrontHeight-leftFrontHeight;
         double as=(angleFront/(16d));
         angleFront=Math.atan(as);

         double angleRear=rightRearHeight-leftRearHeight;
         as=(angleRear/(16d));
         angleRear=Math.atan(as);

         float rot=(float) ((angleFront+angleRear)/2d);
         rotateZ(rot);

         double angleLeft=leftFrontHeight-leftRearHeight;
         as=(angleLeft/(16d));
         angleLeft=Math.atan(as);

         double angleRight=rightFrontHeight-rightRearHeight;
         as=(angleRight/(16d));
         angleRight=Math.atan(as);

         rot=(float) ((angleLeft+angleRight)/2d);

         rotateX(rot);

         /**
          * The car is correctly rotated now. But we still have to adjust the height.
          * We are simply taking the minimum distance from all wheels to the ground as
          * the new height.
          */
         float down=rightFrontHeight;
         if (leftFrontHeight<down) {
            down=leftFrontHeight;
         }
         if (rightRearHeight<down) {
            down=rightRearHeight;
         }
         if (leftRearHeight<down) {
            down=leftRearHeight;
         }

         dropDown.scalarMul(down-4);
         translate(dropDown);

      } else {
         return false;
      }

      /**
       * And finally, rotate the car around Y (that's the car's direction)
       */
      rotateY(yRot);
      return true;
   }

   /**
    * Turns the car to the left
    */
   public void turnLeft() {
      yRot-=0.075f;
   }

   /**
    * Turns the car to the right
    */
   public void turnRight() {
      yRot+=0.075f;
   }

   /**
    * Returns the car's direction.
    * @return the direction
    */
   public float getDirection() {
      return yRot;
   }
}
