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
 * This class computes a CRC16-CCITT value use the polynomial 0x1021.
 *
 * @author Yanick Poirier
 */
public class CRC16CCITT
        implements Checksum {

    private int crc;

    /**
     * This method is equivalent to:
     * <pre>
     * update(data, 0, data.length);
     * </pre>
     *
     * @param data
     */
    public void update( byte data[] ) {
        update( data, 0, data.length );
    }

    @Override
    public void update( int data ) {
        byte buffer[] = new byte[ 1 ];
        buffer[0] = (byte) ( data & 0xff );

        update( buffer, 0, 1 );
    }

    /** @inheritDoc */
    @Override
    public void update( byte[] data,
                        int off,
                        int len ) {
        int crc_temp;
        int b;

        for( int j = off; j < len; j++ ) {
            b = data[j];

            for( int i = 0; i < 8; i++ ) {
                crc_temp = ( crc >> 15 ) ^ ( b >> 7 );

                crc <<= 1;
                crc &= 0xffff;

                if( crc_temp > 0 ) {
                    crc ^= 0x1021;
                    crc &= 0xffff;
                }

                b <<= 1;
                b &= 0xffff;
            }
        }

        crc &= 0xffff;
    }

    /** @inheritDoc */
    @Override
    public long getValue() {
        return crc;
    }

    /** @inheritDoc */
    @Override
    public void reset() {
        crc = 0xffff;
    }

}
