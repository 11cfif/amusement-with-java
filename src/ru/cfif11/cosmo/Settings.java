package ru.cfif11.cosmo;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Settings {

    public static final int defWidth = 800;
    public static final int defHeight = 600;
    public static boolean fullScreen;

    public static double scaleFactor = 1;

    public static double width;
    public static int height;

    public static void init(boolean fullScreenn) {
        fullScreen = fullScreenn;
        calcParam();

    }

    private static void calcParam() {
        if(!fullScreen) {
            height = defHeight;
            width = defWidth;
        } else {

        }
    }


}
