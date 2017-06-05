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
package moka.util.checksum;

import java.util.zip.Checksum;

/**
 *
 * @author Yanick Poirier
 */
public class CRC64
        implements Checksum {

    private static final long TABLE[] = new long[ 256 ];
    private static final long POLY = 0xC96C5795D7870F42L;

    static {
        for( int b = 0; b < TABLE.length; ++b ) {
            long r = b;
            for( int i = 0; i < 8; ++i ) {
                if( ( r & 1 ) == 1 ) {
                    r = ( r >>> 1 ) ^ POLY;
                }
                else {
                    r >>>= 1;
                }
            }

            TABLE[b] = r;
        }

    }

    /**
     * Constructor.
     */
    public void CRC64() {
        mValue = -1;
    }

    @Override
    public void update( int b ) {
        byte buffer[] = new byte[ 1 ];
        buffer[0] = (byte) ( b & 0xff );
        update( buffer, 0, 1 );
    }

    @Override
    public void update( byte[] b,
                        int off,
                        int len ) {

        for( int j = off; j < len; j++ ) {
            mValue = TABLE[( b[j] ^ (int) mValue ) & 0xFF] ^ ( mValue >>> 8 );
        }
    }

    @Override
    public long getValue() {
        return ~mValue;
    }

    @Override
    public void reset() {
        mValue = -1;
    }

    private long mValue;
}
