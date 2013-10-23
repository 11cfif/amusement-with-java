package ru.cfif11.cosmo.Control;

import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;

/**
 * Created with IntelliJ IDEA.
 * User: Student
 */

public class KeyboardControl {
    private  KeyMapper keyMapper;

    public KeyboardControl(){
        keyMapper   = new KeyMapper();
    }
    public KeyState pollControls() {
        KeyState ks = null;
        while((ks = keyMapper.poll()) != KeyState.NONE) {
            System.out.println("2)"+this.getClass());
            return ks;      }
        return ks;
    }


}
