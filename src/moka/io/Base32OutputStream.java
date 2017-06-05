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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A base32 encoding output stream. It encodes in base32 everything passed to the output
 * stream. This implementation is based on <a
 * href="http://tools.ietf.org/pdf/rfc4648.pdf">RFC4648</a>. Therefore, the output
 * base32 is on a single line, i.e. it is not truncated to 76 characters per line.
 *
 *
 * @see Base32hexInputStream
 * @see Base32hexOutputStream
 * @see Base32InputStream
 * @see Base64InputStream
 * @see Base64OutputStream
 * @see Base64urlInputStream
 * @see Base64urlOutputStream
 *
 * @author Yanick Poirier
 */
public class Base32OutputStream
        extends FilterOutputStream {

    /**
     * Constructs a Base64 output stream encoder on top of the specified output stream.
     *
     * @param out the underlying output stream.
     */
    public Base32OutputStream( OutputStream out ) {
        super( out );
    }

    /**
     * Closes this output stream and releases any system resources associated with the
     * stream. It calls its {@code flush} method, but <b>does not</b> calls the
     * {@code close()} method of its underlying output stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close()
            throws IOException {
        flush();
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out
     * to the stream.
     * <p>
     * Flushing this stream also completes the Base64 encoding process and writes the
     * required padding characters, if needed, to the stream. Therefore, this method
     * <b>must not</b> be called if the encoding process is not completed. Otherwise,
     * invalid Base64 data may be generated.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void flush()
            throws IOException {
        if( mByteCount > 0 ) {
            String alphabet = getAlphabet();

            // The 5 bytes are converted into 8 bytes using the following scheme:
            //
            // Input  = [AAAAAAAA][BBBBBBBB][CCCCCCCC][DDDDDDDD][EEEEEEEE]
            // Output = [000AAAAA][000AAABB][000BBBBB][000BCCCC][000CCCCD][000DDDDD][000DDEEE][000EEEEE]
            byte b1 = (byte) ( ( mBuffer & 0xF800000000L ) >> 35 );
            byte b2 = (byte) ( ( mBuffer & 0x07C0000000L ) >> 30 );
            byte b3 = (byte) ( ( mBuffer & 0x003E000000L ) >> 25 );
            byte b4 = (byte) ( ( mBuffer & 0x0001F00000L ) >> 20 );
            byte b5 = (byte) ( ( mBuffer & 0x00000F8000L ) >> 15 );
            byte b6 = (byte) ( ( mBuffer & 0x0000007C00L ) >> 10 );
            byte b7 = (byte) ( ( mBuffer & 0x00000003E0L ) >> 5 );
            byte b8 = (byte) ( mBuffer & 0x000000001FL );

            // The 8 bytes are mapped to the character table 'alphabet'. If less than 8 bytes
            // are available, padding character are written to the stream.
            super.write( alphabet.charAt( b1 ) );
            super.write( alphabet.charAt( b2 ) );
            if( mByteCount < 2 ) {
                super.write( '=' );
                super.write( '=' );
                super.write( '=' );
                super.write( '=' );
                super.write( '=' );
                super.write( '=' );
            }
            else {
                super.write( alphabet.charAt( b3 ) );
                super.write( alphabet.charAt( b4 ) );
                if( mByteCount < 3 ) {
                    super.write( '=' );
                    super.write( '=' );
                    super.write( '=' );
                    super.write( '=' );
                }
                else {
                    super.write( alphabet.charAt( b5 ) );
                    if( mByteCount < 4 ) {
                        super.write( '=' );
                        super.write( '=' );
                        super.write( '=' );
                    }
                    else {
                        super.write( alphabet.charAt( b6 ) );
                        super.write( alphabet.charAt( b7 ) );
                        if( mByteCount < 5 ) {
                            super.write( '=' );
                        }
                        else {
                            super.write( alphabet.charAt( b8 ) );
                        }
                    }
                }
            }

            // Reset the buffer and its position.
            mBuffer = 0;
            mByteCount = 0;

            super.flush();
        }
    }

    @Override
    public void write( int b )
            throws IOException {
        mBuffer |= ( b & 0xFFL ) << ( 32 - ( mByteCount * 8 ) );
        mByteCount++;
        if( mByteCount == 5 ) {
            flush();
        }
    }

    /**
     * Retrieves the encoding alphabet.
     *
     * @return String
     */
    protected String getAlphabet() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    }

    /** Output buffer. */
    private long mBuffer = 0;
    /** Number of bytes in the output buffer. */
    private int mByteCount = 0;
}
