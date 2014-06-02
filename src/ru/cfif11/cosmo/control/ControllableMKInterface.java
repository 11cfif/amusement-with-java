package ru.cfif11.cosmo.control;

import com.threed.jpct.FrameBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */
public interface ControllableMKInterface {
    boolean pollControls();
    void    applyControl(long ticks, FrameBuffer buffer);
}
