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
 * A simple class to retrieves some system information on the current user.
 *
 * @author Yanick Poirier
 */
public class UserInfo {

    public UserInfo() {
    }

    static public String getName() {
        return System.getProperty( "user.name" );
    }

    /**
     * Retrieves the user's home directory.
     *
     * @return User's home directory <i>without</i> the last file separator ('/' or '\')
     */
    static public String getHome() {
        return System.getProperty( "user.home" );
    }

}
