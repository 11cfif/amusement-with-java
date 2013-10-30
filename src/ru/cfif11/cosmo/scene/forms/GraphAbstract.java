package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.Texture;
import ru.cfif11.cosmo.Main;
import ru.cfif11.cosmo.Settings;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphAbstract {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int widthDest;
    protected int heightDest;
    protected Texture texture;
    private static final int maxWidth = 256;
    private static final int maxHeight = 256;


    protected GraphAbstract(int x, int y, int width, int height, int widthDest, int heightDest) {
        this.x          = x;
        this.y          = y;
        setSize(width, height, widthDest, heightDest);
    }

    private void setSize(int width, int height, int widthDest, int heightDest) {
        if(width > maxWidth)
            this.width = maxWidth;
        if(height > maxHeight)
            this.height = maxHeight;
        this.widthDest = (int)(widthDest * Settings.scaleFactor);
        this.heightDest = (int)(heightDest * Settings.scaleFactor);
    }

    protected void setTexture(String texture) {
        this.texture = Main.texMan.getTexture(texture);
    }


}
