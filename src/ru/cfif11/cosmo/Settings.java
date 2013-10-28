package ru.cfif11.cosmo;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public class Settings {

    public static final int defWidth = 640;
    public static final int defHeight = 480;
    public final boolean fullScreen;

    public static double scaleFactor = 1;

    public static double width;
    public static int height;

    public Settings(boolean fullScreen) {
        this.fullScreen = fullScreen;

    }


}
