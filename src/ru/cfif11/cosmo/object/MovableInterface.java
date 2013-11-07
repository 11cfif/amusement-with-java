package ru.cfif11.cosmo.object;

import com.threed.jpct.FrameBuffer;
import ru.cfif11.cosmo.control1.ControllableMKInterface;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksandr Galkin
 */
public interface MovableInterface extends ControllableMKInterface{

    public void move(long ticks, FrameBuffer buffer);

}
