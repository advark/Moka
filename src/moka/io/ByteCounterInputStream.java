/*
 * Copyright (C) 2020 Yanick Poirier <ypoirier at hotmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package moka.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A byte counting output stream.
 * <p>
 * This {@linkplain FilterInputStream} counts the number of bytes (not characters) that are read
 * from an {@linkplain OutputStream}.
 * <p>
 * There is no limit on the number of object of this class that can be chained in an ouput stream
 * chain. For example:
 *
 * <blockquote><pre>
 * FileInputStream fis = new FileInputStream( "myfile" );
 * ByteCounterInputStream bcis1 = new ByteCounterInputStream( fis );
 * ZipInputStream zis = new ZipOutputStream( bcis1 );
 * ByteCounterInputStream bcis2 = new ByteCounterInputStream( zis );
 * </pre></blockquote>
 *
 * {@code bcos2} will count the number of compressed bytes read from {@code fis} and
 * {@code bcos1} will count the number of uncompressed bytes read to {@code fis}.
 * <p>
 * This class is thread safe.
 *
 * @author Yanick Poirier <ypoirier at hotmail.com>
 */
public class ByteCounterInputStream
        extends FilterInputStream {

    private long byteRead = 0;

    /**
     * Constructor.
     * <p>
     * This initial byte counter is set to 0.
     *
     * @param in {@linkplain InputStream} to count the bytes written to.
     */
    public ByteCounterInputStream( InputStream in ) {
        this( in, 0 );
    }

    /**
     * Constructor.
     *
     * @param in    {@linkplain InputStream} to count the bytes read.
     * @param value Initial byte count value.
     */
    public ByteCounterInputStream( InputStream in,
                                   long value ) {
        super( in );
        byteRead = value;
    }

    /**
     * Retrieves the number of bytes read to the underlying {@linkplain InputStream}.
     * <p>
     * The count value returned is the actual number of bytes read from the underlying stream. If
     * that stream is buffered, the return value may not represent the actual number of bytes
     * physically read from the stream but rather read from that stream's internal buffer.
     *
     * @return the number of byte read from the underlying {@linkplain InputStream}.
     */
    synchronized public long getCount() {
        return byteRead;
    }

    @Override
    synchronized public int read()
            throws IOException {
        int n = super.read();
        byteRead++;
        return n;
    }

    @Override
    synchronized public int read( byte[] bytes )
            throws IOException {
        return read( bytes, 0, bytes.length );
    }

    @Override
    synchronized public int read( byte[] bytes,
                                  int start,
                                  int count )
            throws IOException {
        int n = -1;
        for( int i = 0; i < count; i++ ) {
            int b = read();
            if( b == -1 ) {
                break;
            }

            bytes[start + i] = (byte) ( b & 0xff );
        }

        return n;
    }

    /**
     * Sets the current number of byte read.
     *
     * @param value New byte counter value.
     */
    synchronized public void setCount( long value ) {
        byteRead = value;
    }

    /**
     * Resets the number of byte read to 0.
     */
    synchronized public void zero() {
        byteRead = 0;
    }

}
