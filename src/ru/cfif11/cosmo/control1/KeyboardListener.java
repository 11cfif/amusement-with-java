package ru.cfif11.cosmo.control1;

import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */

public class KeyboardListener {
    private  KeyMapper keyMapper;

    public KeyboardListener(){
        keyMapper   = new KeyMapper();
    }
    public KeyState pollControls() {
        KeyState ks;
        while((ks = keyMapper.poll()) != KeyState.NONE)
            return ks;
        return ks;
    }


}
