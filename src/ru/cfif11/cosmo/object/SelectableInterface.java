package ru.cfif11.cosmo.object;

import com.threed.jpct.SimpleVector;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public interface SelectableInterface {

    public SimpleVector getPosition();
    public boolean hasTexture();
    public boolean isSelect();
    public void setSelect(boolean select);

}
