package ru.cfif11.cosmo.object;

import com.threed.jpct.FrameBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Aleksandr Galkin
 */
public interface MovableInterface {

    public void move(long ticks, FrameBuffer buffer);

}
