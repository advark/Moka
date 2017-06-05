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
package moka.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A {@code ClassLoader} with an optional checksum validation. This class loader acts as a
 * normal class loader but performs digest calculation on the class bytes before
 * validating the class data.
 * <p/>
 * This class uses the SHA-1 digest algorithm for checksum validation. The digest
 * validation is performed if, and only if, the class is accompanied with a checksum file
 * with the same name but with the checksum algorithm name as its last extension. For
 * example, the class {@code com.mypackage.myclassname.class} will have it digest file
 * named {@code com.mypackage.myclassname.class.sha}.
 * <p/>
 * If no checksum file is found the the class being loaded, this class acts as a regular
 * {@link JarClassLoader} unless specified in the constructor that the checksum validation
 * is mandatory.
 *
 * @author Yanick Poirier
 */
public class DigestClassLoader
        extends JarClassLoader {

    /**
     * Initializes this object using the specified digest algorithm. This is equivalent
     * to:
     * <blockquote><pre>
     * DigestClassLoader( parent, true );
     * </pre></blockquote>
     *
     * @param parent Parent class loader.
     *
     * @see DigestClassLoader#DigestClassLoader(java.lang.ClassLoader, boolean)
     */
    public DigestClassLoader( ClassLoader parent ) {
        this( parent, true );
    }

    /**
     * Initializes this object using the specified digest algorithm.
     *
     * @param parent    Parent class loader.
     * @param mandatory If {@code true} the loadclass will fail if the checksum file is
     *                  not present. If {@code false} the checksum validation is ignore is
     *                  the checksum file is missing.
     */
    public DigestClassLoader( ClassLoader parent,
                              boolean mandatory ) {
        super( parent );

        mMandatory = mandatory;
        if( mDigest == null ) {
            try {
                mDigest = MessageDigest.getInstance( "SHA" );
            }
            catch( NoSuchAlgorithmException ex ) {
                mDigest = null;
            }
        }
    }

    /**
     * Checks if the classes to be loaded have their checksum verified.
     *
     * @return {@code true} if the checksum will be verified or {@code false} otherwise.
     */
    public boolean verifyEnabled() {
        return mDigest != null && mMandatory;
    }

    /**
     * Loads the class raw byte code.
     *
     * @param className Name of the class to load.
     *
     * @return {@code byte[]} or {@code null} if the class cannot be found.
     *
     * @throws java.lang.Exception
     */
    @Override
    protected byte[] loadClassData( String className )
            throws Exception {
        byte[] buffer = super.loadClassData( className );

        if( buffer != null && mDigest != null ) {
            mDigest.reset();

            // Verify the checksum
            if( !_verifyChecksum( className,
                                  mDigest.getDigestLength(),
                                  mDigest.digest( buffer ) ) ) {
                String msg = "Class " + className +
                             " checksum does not match";
                throw new ClassNotFoundException( msg );
            }

        }
        return buffer;
    }

    private boolean _verifyChecksum( String className,
                                     int length,
                                     byte[] digest )
            throws IOException {
        boolean matched = !mMandatory;
        InputStream is = null;
        String checksumName;

        if( mDigest != null ) {
            checksumName = String.format( "%s.class.sha",
                                          className.replace( '.', '/' ) );

            is = getResourceAsStream( checksumName );
        }

        if( is != null ) {
            InputStreamReader isr = new InputStreamReader( is );
            char cbuf[] = new char[ length * 2 ];

            try {
                isr.read( cbuf );
            }
            catch( IOException ex ) {
                throw new IOException( "Error reading checksum file", ex );
            }

            String s1 = new String( cbuf );
            String s2 = "";

            // Converts the digest bytes to a Hex String value
            for( byte b : digest ) {
                String hex = Integer.toHexString( b );
                if( hex.length() < 2 ) {
                    s2 += "0" + hex;
                }
                else if( hex.length() > 2 ) {
                    s2 += hex.substring( hex.length() - 2 );
                }
                else {
                    s2 += hex;
                }
            }

            if( s1.compareToIgnoreCase( s2 ) == 0 ) {
                matched = true;
            }
        }

        return matched;
    }

    static private MessageDigest mDigest = null;
    private boolean mMandatory = false;
}
