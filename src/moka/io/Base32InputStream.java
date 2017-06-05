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
package moka.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A base32 decoding input stream. It decodes a base32 data read from an input stream.
 * This implementation is based on <a
 * href="http://tools.ietf.org/pdf/rfc4648.pdf">RFC4648</a>. All characters not part of
 * the base32 alphabet are ignored (or skipped).
 *
 * @see Base32hexInputStream
 * @see Base32hexOutputStream
 * @see Base32OutputStream
 * @see Base64InputStream
 * @see Base64OutputStream
 * @see Base64urlInputStream
 * @see Base64urlOutputStream
 *
 * @author Yanick Poirier
 */
public class Base32InputStream
        extends FilterInputStream {

    /**
     * Constructs a Base32 input stream encoder on top of the specified input stream.
     *
     * @param in the underlying input stream.
     */
    public Base32InputStream( InputStream in ) {
        super( in );
    }

    @Override
    public int read()
            throws IOException {
        if( mBuffer == null ) {
            _loadBuffer();
        }
        else if( mBufOffset == mBuffer.length ) {
            _loadBuffer();
        }

        return mBuffer.length == 0 ? -1 : ( mBuffer[mBufOffset++] & 0xff );
    }

    @Override
    public int read( byte[] b,
                     int offset,
                     int length )
            throws IOException {
        int i = 0;
        int lb;
        int count = 0;

        do {
            lb = read();
            if( lb != -1 ) {
                b[i++] = (byte) lb;
                count++;
            }
        }
        while( i < length && lb != -1 );

        return count;
    }

    /**
     * Retrieves the encoding alphabet.
     *
     * @return String
     */
    protected String getAlphabet() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    }

    /**
     * Reads a maximum of 4 bytes from the underlying {@code InputStream} and stores them
     * in the working buffer.
     *
     * @throws java.io.IOException
     */
    private void _loadBuffer()
            throws IOException {
        byte tmp[] = new byte[ 8 ];
        int i = 0;
        String alphabet = getAlphabet();

        do {
            // Read the next byte
            int b = super.read();

            if( b == -1 ) {
                // EOF reached
                if( i != 0 ) {
                    // We have only a partial Base32 data. Something is wrong.
                    throw new IOException( "Invalid Base32 stream." );
                }

                // Flag the EOF.
                mBuffer = new byte[ 0 ];
                mBufOffset = 0;
                mEof = true;
            }
            else {
                // Get the index position in the alphabet
                tmp[i] = (byte) alphabet.indexOf( b );

                // In order to respect the RFC4648, we do ignore all the characters that
                // are not part of the Base64 alphabet, unless it is a padding character.
                if( tmp[i] == -1 ) {
                    // The byte is not part of the alphabet. Check for the pad character.
                    if( b == '=' ) {
                        // Padding character found, don't ignore it.
                        i++;
                    }
                }
                else {
                    // Valid character found
                    i++;
                }
            }
        }
        while( i < tmp.length && !mEof );

        if( !mEof ) {
            if( tmp[0] == -1 || tmp[1] == -1 ) {
                // The first 2 bytes cannot be padding characters.
                throw new IOException( "Invalid Base32 stream." );
            }

            if( tmp[2] == -1 && ( tmp[3] != -1 || tmp[4] != -1 || tmp[5] != -1 ||
                                  tmp[6] != -1 || tmp[7] != -1 ) ) {
                // If the 3rd byte is padding, the 4th, 5th, 6th, 7th and 8th ones must be
                // padding.
                throw new IOException( "Invalid Base32 stream." );
            }

            // At this point, we have a valid 8 byte array to work on.
            if( tmp[7] == -1 ) {
                // No 5th byte (E)
                if( tmp[6] == -1 ) {
                    // No 4th byte (D)
                    if( tmp[4] == -1 ) {
                        // No 3rd byte (C)
                        if( tmp[3] == -1 ) {
                            // No 2nd byte (B)
                            mBuffer = new byte[ 1 ];
                            mBuffer[0] = _getByteA( tmp );
                        }
                        else {
                            // 2 bytes (A-B)
                            mBuffer = new byte[ 2 ];
                            mBuffer[0] = _getByteA( tmp );
                            mBuffer[1] = _getByteB( tmp );
                        }
                    }
                    else {
                        // 3 bytes (A-B-C)
                        mBuffer = new byte[ 3 ];
                        mBuffer[0] = _getByteA( tmp );
                        mBuffer[1] = _getByteB( tmp );
                        mBuffer[2] = _getByteC( tmp );
                    }
                }
                else {
                    // 4 bytes (A-B-C-D)
                    mBuffer = new byte[ 4 ];
                    mBuffer[0] = _getByteA( tmp );
                    mBuffer[1] = _getByteB( tmp );
                    mBuffer[2] = _getByteC( tmp );
                    mBuffer[3] = _getByteD( tmp );
                }
            }
            else {
                // 5 bytes (A-B-C-D-E)
                mBuffer = new byte[ 5 ];
                mBuffer[0] = _getByteA( tmp );
                mBuffer[1] = _getByteB( tmp );
                mBuffer[2] = _getByteC( tmp );
                mBuffer[3] = _getByteD( tmp );
                mBuffer[4] = _getByteE( tmp );

            }
            mBufOffset = 0;
        }

    }

    /*
     * The 8 bytes are converted into 5 bytes using the following
     * scheme:
     * 0 1 2 3 4 5 6 7
     * Input =
     * [000AAAAA][000AAABB][000BBBBB][000BCCCC][000CCCCD][000DDDDD][000DDEEE][000EEEEE]
     * Output = [AAAAAAAA][BBBBBBBB][CCCCCCCC][DDDDDDDD][EEEEEEEE]
     */
    private byte _getByteA( byte[] in ) {
        return (byte) ( ( ( in[0] & 0x1F ) << 3 ) | ( ( in[1] & 0x1C ) >> 2 ) );
    }

    private byte _getByteB( byte[] in ) {
        return (byte) ( ( ( in[1] & 0x03 ) << 6 ) | ( ( in[2] & 0x1F ) << 1 ) |
                        ( ( in[3] &
                            0x10 ) >> 4 ) );
    }

    private byte _getByteC( byte[] in ) {
        return (byte) ( ( ( in[3] & 0x0F ) << 4 ) | ( ( in[4] & 0x1E ) >> 1 ) );
    }

    private byte _getByteD( byte[] in ) {
        return (byte) ( ( ( in[4] & 0x01 ) << 7 ) | ( ( in[5] & 0x1F ) << 2 ) |
                        ( ( in[6] &
                            0x18 ) >> 3 ) );
    }

    private byte _getByteE( byte[] in ) {
        return (byte) ( ( ( in[6] & 0x07 ) << 5 ) | ( ( in[7] & 0x1F ) ) );
    }

    /** Working buffer. */
    private byte mBuffer[] = null;
    /** Current position in the buffer. */
    private int mBufOffset = 0;
    /** EOF flag */
    private boolean mEof = false;
}
