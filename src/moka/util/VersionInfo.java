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

import java.util.Arrays;
import java.util.Objects;

/**
 * This class provides a standardized method to identify version numbering in an
 * application. The version number is composed of five (5) parts in the form of
 * <i>major.minor.revision.build extra</i> where:
 * <ul>
 * <li><i>major</i> is the major version number;</li>
 * <li><i>minor</i> is the minor version number; and</li>
 * <li><i>revision</i> is the bug release version number.</li>
 * <li><i>build</i> is a build number information in the version number.</li>
 * <li><i>extra</i> is an extra information in the version number.</li>
 * </ul>
 *
 * <h2>Major version</h2>
 *
 * A change in the <i>major</i> version means, as it says, major changes that renders it
 * incompatible with other version that do not have the same <i>major</i> version number.
 *
 * <h2>Minor version</h2>
 *
 * A change in the <i>minor</i> version means only minor changes has been done, such as
 * new functionalities and bug fixes, but <i>must</i> be compatible with other version
 * that has the same <i>major</i> version number. Functionalities (class, method) cannot
 * be removed. If obsolete, such functionalities should be marked {@code deprecated} and
 * may be removed in the next major release. Existing methods must return the same result
 * as the previous version.
 *
 * <h2>Revision number</h2>
 *
 * A change in the <i>revision</i> number means only bugs fixes, performance improvement,
 * etc, and as for <i>Minor</i> version, must remain 100% compatible with previous version
 * that have the same <i>major</i> and <i>minor</i> version number. If this value is
 * negative, it will be ignored as well as the build number.
 *
 * <h2>Build number</h2>
 *
 * Build numbers are an integer number that is incremented every time the application is
 * built or packaged but can be used for other purposes. If this value is negative, it
 * will be ignored.
 *
 * <h2>Extra information</h2>
 *
 * This field can be use for almost any purpose to help identify the exact version. Such
 * data can be the packaging date, beta version identification, release candidate
 * identification, etc.
 *
 * @author Yanick Poirier
 */
public class VersionInfo
        implements Comparable<VersionInfo> {

    /**
     * Default constructor.
     */
    public VersionInfo() {
        mVersion = new int[ 4 ];

        mVersion[0] = -1;
        mVersion[1] = -1;
        mVersion[2] = -1;
        mVersion[3] = -1;
    }

    /**
     * Constructor.
     *
     * @param major    Major version number.
     * @param minor    Minor version number.
     * @param revision Revision number.
     * @param build    Build number.
     */
    public VersionInfo( int major,
                        int minor,
                        int revision,
                        int build ) {
        this();

        mVersion[0] = major;
        mVersion[1] = minor;
        mVersion[2] = revision;
        mVersion[3] = build;
    }

    /**
     * Constructor.
     *
     * @param major    Major version number.
     * @param minor    Minor version number.
     * @param revision Revision number.
     * @param build    Build number.
     * @param extra    Extra version information. Can be used to identify beta
     *                 version, final release, release candidate, etc.
     */
    public VersionInfo( int major,
                        int minor,
                        int revision,
                        int build,
                        String extra ) {
        this();

        mVersion[0] = major;
        mVersion[1] = minor;
        mVersion[2] = revision;
        mVersion[3] = build;
        mExtraInfo = extra;
    }

    /**
     * Retrieves the complete version number in a quad integer array. The indexes of the
     * array are defined as follow:
     *
     * <ul>
     * <li>0: Major version,</li>
     * <li>1: Minor version,</li>
     * <li>2: Revision number, and</li>
     * <li>3: Build number</li>
     * </ul>
     *
     * @return int[4]
     *
     * @see #getBuildNumber()
     * @see #getMajorVersion()
     * @see #getMinorVersion()
     * @see #getRevisionNumber()
     */
    public int[] getVersion() {
        return mVersion;
    }

    /**
     * Retrieves the major version number.
     *
     * @return {@code int} or -1 if the major version is not available.
     *
     * @see #getBuildNumber()
     * @see #getMinorVersion()
     * @see #getRevisionNumber()
     * @see #getVersion()
     */
    public int getMajorVersion() {
        return mVersion[0];
    }

    /**
     * Retrieves the minor version number.
     *
     * @return {@code int} or -1 if the minor version is not available.
     *
     * @see #getBuildNumber()
     * @see #getMajorVersion()
     * @see #getRevisionNumber()
     * @see #getVersion()
     */
    public int getMinorVersion() {
        return mVersion[1];
    }

    /**
     * Retrieves the release version number.
     *
     * @return {@code int} or -1 if the release version is not available.
     *
     * @see #getBuildNumber()
     * @see #getMajorVersion()
     * @see #getMinorVersion()
     * @see #getVersion()
     */
    public int getRevisionNumber() {
        return mVersion[2];
    }

    /**
     * Retrieves the build number.
     *
     * The build number provides extra information on the version. The number is increased
     * every time the source code is compiled. This number is reset to 0 every time the
     * major version changes.
     *
     * @return {@code int} or -1 if the build number is not available.
     *
     * @see #getVersion()
     * @see #getMajorVersion()
     * @see #getMinorVersion()
     * @see #getRevisionNumber()
     */
    public int getBuildNumber() {
        return mVersion[3];
    }

    public String getExtra() {
        return mExtraInfo;
    }

    public void setMajorVersion( int major ) {
        mVersion[0] = major;
    }

    public void setMinorVersion( int minor ) {
        mVersion[1] = minor;
    }

    public void setRevisionNumber( int revision ) {
        mVersion[2] = revision;
    }

    public void setBuildNumber( int build ) {
        mVersion[3] = build;
    }

    public void setExtra( String extra ) {
        mExtraInfo = extra;
    }

    public void setVersion( int[] versions ) {
        int max = versions.length > 4 ? 4 : versions.length;
        System.arraycopy( versions, 0, mVersion, 0, max );
    }

    /**
     * Compares the version numbers of this class with another {@code VersionInfo} object.
     * <p/>
     * <b>Note</b>: this class has a natural ordering that is inconsistent with equals and
     * does not take into account the <i>extra</i> version data.
     *
     * @param vi {@code VersionInfo} to compare with.
     */
    @Override
    public int compareTo( VersionInfo vi ) {
        if( vi != null ) {
            int rc = 0;
            int i = 0;

            while( i < mVersion.length && rc == 0 ) {
                rc = mVersion[i] - vi.mVersion[i];
            }
        }

        return 1;
    }

    /**
     * Returns a {@code String} representation of the version number using the format
     * <i>major.minor[[.revision].build][-[extra]]</i>.
     *
     * If the revision number is negative and the build number is not negative, the
     * revision number is assumed to be 0.
     *
     * If both, the revision and build numbers, are negatives, only the major and minor
     * versions are returned.
     *
     * @return {@code String}
     */
    @Override
    public String toString() {
        String rc = "" + mVersion[0] + "." + mVersion[1];

        if( mVersion[2] > -1 ) {
            rc += "." + mVersion[2];

            if( mVersion[3] > -1 ) {
                rc += "." + mVersion[3];
            }
        }
        else if( mVersion[3] > -1 ) {
            rc += ".0." + mVersion[3];
        }

        if( mExtraInfo != null ) {
            rc += "-" + mExtraInfo;
        }

        return rc;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.hashCode( mVersion );
        hash = 89 * hash + Objects.hashCode( mExtraInfo );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if( obj == null ) {
            return false;
        }

        if( getClass() != obj.getClass() ) {
            return false;
        }

        final VersionInfo other = (VersionInfo) obj;
        if( Arrays.equals( mVersion, other.mVersion ) ) {
            if( !Objects.equals( mExtraInfo, other.mExtraInfo ) ) {
                return false;
            }
        }
        else {
            return false;
        }

        return true;
    }

    private int[] mVersion = null;
    private String mExtraInfo = null;
}
