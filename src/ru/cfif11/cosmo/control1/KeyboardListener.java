package ru.cfif11.cosmo.control1;

import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */

public class KeyboardListener {
    public static final HashMap<String, Integer>    KEYS;
    public static final int                         MAX_KEY = 130;

    private KeyMapper   keyMapper;
    private KeyState    mainState = KeyState.NONE;


    static {
        KEYS = new HashMap<String, Integer>(MAX_KEY);
        for(int i = 0; i < MAX_KEY; i++) {
            KEYS.put(KeyEvent.getKeyText(i), i);
        }
    }


    public KeyboardListener() {
        keyMapper = new KeyMapper();
    }

    public  void pollControls(String[] keyNames, boolean result[]) {
        while (mainState != KeyState.NONE) {
            for (int i = 0; i < keyNames.length; i++) {
                if(mainState.getKeyCode() == KEYS.get(keyNames[i]))
                    result[i] = mainState.getState();
            }
            setMainState();
        }

    }

    public KeyState getMainState(){
        return mainState;
    }

    public void setMainState() {
        mainState = keyMapper.poll();
    }


}
