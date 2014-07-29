package ru.cfif11.cosmo;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Settings {

    private static final int defWidth = 800;
    private static final int defHeight = 600;
    private static boolean fullScreen;

    public static double scaleFactor = 1;

    private static double width;
    private static int height;

    public static void init(boolean fullScreen1) {
        fullScreen = fullScreen1;
        calcParam();

    }

    private static void calcParam() {
        if(!fullScreen) {
            height = defHeight;
            width = defWidth;
        }
    }


}
