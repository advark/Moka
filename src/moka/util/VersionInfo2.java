/*
 * Copyright (C) 2017 Yanick Poirier <ypoirier at hotmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package moka.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class contains the application versioning information.
 * <p/>
 * The version number is composed of at least 3 components and at most 5 formatted as follow:
 * <pre><blockquote>
 * major.minor.patch[-build-state]
 * </blockquote></pre>
 * <ul>
 * <li>major: Major version number</li>
 * <li>minor: Minor version number</li>
 * <li>patch: Patch version number</li>
 * <li>build: Build number. The build number is packaging compilation date</li>
 * <li>state: Version current state (ex: dev, alpha, beta, rc, etc)</li>
 * </ul>
 *
 * Examples:
 * <p/>
 * 0.1.0-dev -> major: 0, minor: 1, patch: 0, state: dev, build: <br/>
 * 1.2.3-17205-rc1 -> major: 1, minor: 2, patch: 3, state: rc1, build: 17205<br/>
 * 2.34.0-17210-alpha -> major: 2, minor: 34, patch: 0, state: alpha, build: 17210<br/>
 * 2.34.0 -> major: 2, minor: 34, patch: 0, state: , build: <br/>
 *
 * @author Yanick Poirier <ypoirier at hotmail.com>
 */
public class VersionInfo2
        implements Comparable<VersionInfo2> {

    public VersionInfo2() {
        ClassLoader cl = getClass().getClassLoader();

        Properties p = new Properties();
        try {
            InputStream is = cl.getResourceAsStream( "version.property" );
            p.load( is );
            try {
                major = Integer.parseInt( p.getProperty( "version.major" ) );
            }
            catch( NumberFormatException ex ) {
                // Invalid major number
                major = -1;
            }

            try {
                minor = Integer.parseInt( p.getProperty( "version.minor" ) );
            }
            catch( NumberFormatException ex ) {
                // Invalid minor number
                minor = -1;
            }

            try {
                patch = Integer.parseInt( p.getProperty( "version.patch" ) );
            }
            catch( NumberFormatException ex ) {
                // Invalid patch number
                patch = -1;
            }

            try {
                build = Integer.parseInt( p.getProperty( "version.build" ) );
            }
            catch( NumberFormatException ex ) {
                // Invalid build number
                build = -1;
            }

            try {
                seqId = Integer.parseInt( p.getProperty( "version.seq" ) );
            }
            catch( NumberFormatException ex ) {
                // Invalid compilation ID number
                seqId = -1;
            }

            state = p.getProperty( "version.state" );
        }
        catch( IOException ex ) {
            System.out.println( ex );
        }
    }

    /**
     * Retrieves the major version number.
     *
     * @return the major version number or -1 if unknown or invalid.
     */
    public int getMajor() {
        return major;
    }

    /**
     * Retrieves the minor version number.
     *
     * @return the minor version number or -1 if unknown or invalid.
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Retrieves the patch version number.
     *
     * @return the patch version number or -1 if unknown or invalid.
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Retrieves the build version number. The build number is usually 5 digits.
     *
     * @return the build version number, 0 if irreleveant or -1 if unknown or invalid.
     */
    public int getBuild() {
        if( seqId != -1 ) {
            return build * 1000 + seqId;
        }
        return build;
    }

    /**
     * Retrieves the version state.
     * <p/>
     * The state is an extra information that represents the current state of the version data. For
     * example, state value can be <i>dev</> for development version, <i>rc</i> for Release
     * Candidate, etc. Stable release will always return an empty String
     *
     * @return String containing the current version's state or an empty String for stable release.
     */
    public String getState() {
        return state;
    }

    public String getVersion() {
        if( getState().isEmpty() ) {
            return String.format( "%d.%d.%d (build %d)", getMajor(), getMinor(), getPatch(), getBuild() );
        }

        return String.format( "%d.%d.%d-%s (build %d)", getMajor(), getMinor(), getPatch(), getState(), getBuild() );
    }

    /**
     * Checks if this version object's major value is equal or higher than the specified major
     * value.
     *
     * @param major Minimum major version number
     *
     * @return {@code true} if {@link #getMajor()} >= {@code major}
     */
    public boolean isMajorAtLeast( int major ) {
        return getMajor() >= major;
    }

    /**
     * Checks if this version object's major value is equal or higher than the specified major value
     * and the minor value is equal or higher than the specified minor value.
     * <p/>
     * The minor version number is only verified <i>if</i> the major version number is equal to the
     * specified one. For example, a call to:
     * <pre><blockquote>
     * isMajorMinorAtLeast( 2, 4 )
     * </blockquote></pre>
     * is performed on a version object where the major version number is 3, {@code true} will
     * returned even is the minor version number is less than 4 (i.e. version 3.x is always newer
     * than version 2.4.
     *
     * @param major Minimum major version number
     * @param minor Minimum minor version number
     *
     * @return {@code true} if {@link #getMajor()} >= {@code major}
     */
    public boolean isMajorMinorAtLeast( int major,
                                        int minor ) {
        if( getMajor() > major ) {
            return true;
        }
        if( getMajor() == major ) {
            return getMinor() >= minor;
        }

        return false;
    }

    @Override
    public int compareTo( VersionInfo2 version ) {
        int rc = getMajor() - version.getMajor();
        if( rc == 0 ) {
            rc = getMinor() - version.getMinor();
            if( rc == 0 ) {
                rc = getPatch() - version.getPatch();
                if( rc == 0 ) {
                    rc = getBuild() - version.getBuild();
                }
            }
        }

        return rc;
    }

    private int major = -1;
    private int minor = -1;
    private int patch = -1;
    private int build = -1;
    private int seqId = -1;
    private String state = "";
}
