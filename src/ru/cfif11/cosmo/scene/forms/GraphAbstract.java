package ru.cfif11.cosmo.scene.forms;

import com.threed.jpct.Texture;
import ru.cfif11.cosmo.Main;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public abstract class GraphAbstract {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Texture texture;

    protected GraphAbstract(int x, int y, int width, int height) {
        this.x          = x;
        this.y          = y;
        this.width      = width;
        this.height     = height;
    }

    protected void setTexture(String texture) {
        this.texture = Main.texMan.getTexture(texture);
    }


}
