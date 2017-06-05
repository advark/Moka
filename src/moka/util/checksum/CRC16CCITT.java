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
 * @author ypoirier
 */
public class CRC16CCITT
        implements Checksum {

    public void update( byte b[] ) {
        update( b, 0, b.length );
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
            mValue = ( ( mValue >>> 8 ) | ( mValue << 8 ) ) & 0xffff;
            mValue ^= ( b[j] & 0xff );//byte to int, trunc sign
            mValue ^= ( ( mValue & 0xff ) >> 4 );
            mValue ^= ( mValue << 12 ) & 0xffff;
            mValue ^= ( ( mValue & 0xFF ) << 5 ) & 0xffff;
        }

        mValue &= 0xffff;
    }

    @Override
    public long getValue() {
        return mValue;
    }

    @Override
    public void reset() {
        mValue = 0xffff;
    }

    private int mValue;
}
