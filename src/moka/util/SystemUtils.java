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

/**
 *
 * @author Yanick Poirier
 */
public class SystemUtils {

    static public String getStartupDirectory() {
        return System.getProperty( "user.dir" );
    }

    /**
     * Returns the operating system's name.
     *
     * @return
     */
    static public String getOSName() {
        return System.getProperty( "os.name" );
    }

    /**
     * Checks if the current platform is of Windows family.
     *
     * @return {@code true} is the platform is of Windows family.
     */
    static public boolean isWindows() {
        return getOSName().startsWith( "Windows" );
    }

    /**
     * Checks if the current platform is of Linux family.
     *
     * @return {@code true} is the platform is of Linux family.
     */
    static public boolean isLinux() {
        return getOSName().startsWith( "Linux" );
    }

    /**
     * Returns the operating system's version.
     *
     * @return
     */
    static public String getOSVersion() {
        return System.getProperty( "os.version" );
    }

    /**
     * Returns the operating system's architecture.
     *
     * @return
     */
    static public String getOSArch() {
        return System.getProperty( "os.arch" );
    }

    static public String getLineSeparator() {
        return System.getProperty( "line.separator" );
    }

    private SystemUtils() {
    }

}
