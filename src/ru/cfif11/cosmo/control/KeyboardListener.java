package ru.cfif11.cosmo.control;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Set;

import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;

/**
 * Created with IntelliJ IDEA.
 * User: Galkin Aleksandr
 */

public class KeyboardListener {
	public static final HashMap<String, Integer> KEYS;
	public static final int MAX_KEY = 130;
	private boolean[] keyStates;

	private KeyMapper keyMapper;
	private KeyState mainState = KeyState.NONE;


	static {
		KEYS = new HashMap<>(MAX_KEY);
		for (int i = 0; i < MAX_KEY; i++) {
			KEYS.put(KeyEvent.getKeyText(i), i);
		}
	}


	public KeyboardListener() {
		keyMapper = new KeyMapper();
		keyStates = new boolean[MAX_KEY];
	}

	public void pollControls() {
		while (mainState != KeyState.NONE) {
			Set<String> keyNames = KEYS.keySet();
			for (String str : keyNames) {
				if (mainState.getKeyCode() == KEYS.get(str)) {
					keyStates[KEYS.get(str)] = mainState.getState();
				}
			}
			setMainState();
		}

	}

	public KeyState getMainState() {
		return mainState;
	}

	public void setMainState() {
		mainState = keyMapper.poll();
	}


	public void recordPollСontrols(String[] keys, boolean[] keyStates) {
		for (int i = 0; i < keys.length; i++) {
			keyStates[i] = this.keyStates[KEYS.get(keys[i])];
		}
	}
}
