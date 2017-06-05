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
 * A base64 decoding input stream. It decodes a Base64 data read from an input stream.
 * This implementation is based on <a
 * href="http://tools.ietf.org/pdf/rfc4648.pdf">RFC4648</a>. All characters not part of
 * the base64 alphabet are ignored (i.e. CR, LF, TAB, etc.)
 *
 * @see Base32hexInputStream
 * @see Base32hexOutputStream
 * @see Base32InputStream
 * @see Base32OutputStream
 * @see Base64OutputStream
 * @see Base64urlInputStream
 * @see Base64urlOutputStream
 *
 * @author Yanick Poirier
 */
public class Base64InputStream
        extends FilterInputStream {

    /**
     * Constructs a Base64 input stream encoder on top of the specified input stream.
     *
     * @param in the underlying input stream.
     */
    public Base64InputStream( InputStream in ) {
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

        // Read loop
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
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    }

    /**
     * Reads a maximum of 4 bytes from the underlying <code>InputStream</code> and stores
     * them in the working buffer.
     *
     * @throws java.io.IOException
     */
    private void _loadBuffer()
            throws IOException {
        byte tmp[] = new byte[ 4 ];
        int i = 0;
        String alphabet = getAlphabet();

        do {
            // Read the next byte
            int b = super.read();

            if( b == -1 ) {
                // EOF reached
                if( i != 0 ) {
                    // We have only a partial Base64 data. Something is wrong.
                    throw new IOException( "Invalid Base64 stream." );
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
                throw new IOException( "Invalid Base64 stream." );
            }

            if( tmp[2] == -1 && tmp[3] != -1 ) {
                // If the 3rd byte is padding, the 4th one must be padding.
                throw new IOException( "Invalid Base64 stream." );
            }

            // At this point, we have a valid 4 byte array to work on. The 4 bytes are
            // converted into a 3 byte array using the following scheme:
            // Input = [xxAAAAAA][xxAABBBB][xxBBBBCC][xxCCCCCC]
            // Output = [AAAAAAAA][BBBBBBBB][CCCCCCCC]
            if( tmp[3] == -1 ) {
                if( tmp[2] == -1 ) {
                    // Only the first 2 bytes are valid
                    mBuffer = new byte[ 1 ];
                    mBuffer[0] = (byte) ( ( tmp[0] & 0x3F ) << 2 );
                    mBuffer[0] |= (byte) ( ( tmp[1] & 0x30 ) >> 4 );
                }
                else {
                    // Only the first 3 bytes are valid
                    mBuffer = new byte[ 2 ];
                    mBuffer[0] = (byte) ( ( tmp[0] & 0x3F ) << 2 );
                    mBuffer[0] |= (byte) ( ( tmp[1] & 0x30 ) >> 4 );
                    mBuffer[1] = (byte) ( ( tmp[1] & 0x0F ) << 4 );
                    mBuffer[1] |= (byte) ( ( tmp[2] & 0x3C ) >> 2 );
                }
            }
            else {
                // All 4 bytes are valid
                mBuffer = new byte[ 3 ];
                mBuffer[0] = (byte) ( ( tmp[0] & 0x3F ) << 2 );
                mBuffer[0] |= (byte) ( ( tmp[1] & 0x30 ) >> 4 );
                mBuffer[1] = (byte) ( ( tmp[1] & 0x0F ) << 4 );
                mBuffer[1] |= (byte) ( ( tmp[2] & 0x3C ) >> 2 );
                mBuffer[2] = (byte) ( ( tmp[2] & 0x03 ) << 6 );
                mBuffer[2] |= (byte) ( ( tmp[3] & 0x3F ) );
            }

            mBufOffset = 0;
        }
    }

    /** Working buffer. */
    private byte mBuffer[] = null;

    /** Current position in the buffer. */
    private int mBufOffset = 0;

    /** End of file flag */
    private boolean mEof = false;
}
