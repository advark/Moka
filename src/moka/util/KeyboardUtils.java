/*
 * Copyright (C) 2011-2017 Yanick Poirier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * More details about the licensing can be found at https://www.gnu.org/licenses/lgpl-3.0.en.html.
 */
package moka.util;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

/**
 *
 * @author Yanick Poirier
 */
public class KeyboardUtils {

    /**
     * Checks if the CAPS LOCK key is on.
     *
     * @return {@code true} if CAPS LOCK is on, {@code false} otherwise.
     */
    static public boolean isCapsLockOn() {
        boolean on = false;

        Toolkit tk = Toolkit.getDefaultToolkit();

        try {
            on = tk.getLockingKeyState( KeyEvent.VK_CAPS_LOCK );
        }
        catch( UnsupportedOperationException ex ) {
        }

        return on;
    }

    /**
     * Checks if the NUM LOCK key is on.
     *
     * @return {@code true} if NUM LOCK is on, {@code false} otherwise.
     */
    static public boolean isNumLockOn() {
        boolean on = false;

        Toolkit tk = Toolkit.getDefaultToolkit();

        try {
            on = tk.getLockingKeyState( KeyEvent.VK_NUM_LOCK );
        }
        catch( UnsupportedOperationException ex ) {
        }

        return on;
    }

    /**
     * Checks if the SCROLL LOCK key is on.
     *
     * @return {@code true} if SCROLL LOCK is on, {@code false} otherwise.
     */
    static public boolean isScrollLockOn() {
        boolean on = false;

        Toolkit tk = Toolkit.getDefaultToolkit();

        try {
            on = tk.getLockingKeyState( KeyEvent.VK_SCROLL_LOCK );
        }
        catch( UnsupportedOperationException ex ) {
        }

        return on;
    }

    private KeyboardUtils() {
    }

}
