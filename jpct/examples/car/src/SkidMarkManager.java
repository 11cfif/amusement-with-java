import com.threed.jpct.*;

/**
 * The SkidMarkManager is responsible for drawing skidmarks when necessary.
 */
public class SkidMarkManager {

   /**
    * The maximum number of skidmarks
    */
   private final static int SKID_MARKS=180;

   /**
    * The delta between the skidmarks
    */
   private final static float SKID_DELTA=3f;

   private SkidMark[] skidmarks=new SkidMark[SKID_MARKS];
   private SimpleVector lastPos=null;
   private int nextMark=0;

   /**
    * Creates a new SkidMarkManager
    * @param world the world
    */
   public SkidMarkManager(World world) {
      for (int i=0; i<SKID_MARKS; i++) {
         skidmarks[i]=new SkidMark();
         world.addObject(skidmarks[i]);
     }
   }


   /**
    * "Creates" a skidmark behind the car. If too many skidmarks are visible, the oldest ones
    * will be removed and replaced with the new ones.
    * @param car the car that causes the skidmarks
    */
   public void createSkidMarks(Car car) {
      SimpleVector curPos=car.getTransformedCenter();
      if (lastPos!=null) {
         SimpleVector deltaPos=curPos.calcSub(lastPos);
         float delta=deltaPos.length();
         if (delta>SKID_DELTA) {
            SimpleVector addy=deltaPos;
            addy.scalarMul(SKID_DELTA/delta);
            SimpleVector sPos=new SimpleVector(lastPos);
            while (delta>SKID_DELTA) {
               sPos.add(addy);
               skidmarks[nextMark].place(-1, car, sPos);
               skidmarks[nextMark+1].place(1, car, sPos);
               nextMark+=2;
               nextMark%=SKID_MARKS;
               delta-=SKID_DELTA;
            }
            lastPos=curPos;
         }
      }
      else {
         lastPos=curPos;
      }
   }
}
