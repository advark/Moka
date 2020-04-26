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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A byte counting output stream.
 * <p>
 * This {@linkplain FilterOutputStream} counts the number of bytes (not characters) that are written
 * to an {@linkplain OutputStream}.
 * <p>
 * Usage examples can vary but a typical one is a log file where a file rotation is performed when
 * it reaches a fixed size. There is no limit on the number of object of this class that can be
 * chained in an ouput stream chain. For example:
 *
 * <blockquote><pre>
 * FileOutputStream fos = new FileOutputStream( "myfile" );
 * ByteCounterOutputStream bcos1 = new ByteCounterOutputStream( fos );
 * ZipOutputStream zos = new ZipOutputStream( bcos1 );
 * ByteCounterOutputStream bcos2 = new ByteCounterOutputStream( zos );
 * </pre></blockquote>
 *
 * {@code bcos2} will count the number of uncompressed bytes written to {@code fos} and
 * {@code bcos1} will count the number of compressed bytes written to {@code fos}.
 * <p>
 * This class is thread safe.
 *
 * @author Yanick Poirier <ypoirier at hotmail.com>
 */
public class ByteCounterOutputStream
        extends FilterOutputStream {

    private long byteWritten = 0;

    /**
     * Constructor.
     * <p>
     * This initial byte counter is set to 0.
     *
     * @param out {@linkplain OutputStream} to count the bytes written to.
     */
    public ByteCounterOutputStream( OutputStream out ) {
        this( out, 0 );
    }

    /**
     * Constructor.
     *
     * @param out   {@linkplain OutputStream} to count the bytes written to.
     * @param value Initial byte count value.
     */
    public ByteCounterOutputStream( OutputStream out,
                                    long value ) {
        super( out );
        byteWritten = value;
    }

    /**
     * Retrieves the number of bytes written to the underlying {@linkplain OutputStream}.
     * <p>
     * The count value returned is the actual number of bytes written to the underlying stream. If
     * that stream is buffered, the return value may not represent the actual number of bytes
     * physically written to the stream but rather written to that stream's internal buffer.
     *
     * @return the number of byte written to the underlying {@linkplain OutputStream}.
     */
    synchronized public long getCount() {
        return byteWritten;
    }

    /**
     * Resets the number of byte written to 0.
     */
    synchronized public void reset() {
        byteWritten = 0;
    }

    /**
     * Sets the current number of byte written.
     *
     * @param value New byte counter value.
     */
    synchronized public void setCount( long value ) {
        byteWritten = value;
    }

    @Override
    synchronized public void write( int b )
            throws IOException {
        super.write( b );
        byteWritten++;
    }

    @Override
    synchronized public void write( byte[] bytes )
            throws IOException {
        write( bytes, 0, bytes.length );
    }

    @Override
    synchronized public void write( byte[] bytes,
                                    int start,
                                    int count )
            throws IOException {
        // We could've call super.write( bytes, start, count ) and then add total number of bytes
        // written to the counter but doing may gives wrong results if an exception is thrown in the
        // middle of the writting process. In such case, the byte counter will get updated even if
        // there is a partial number of byte written.
        //
        // Using a for loop may be a little be slower but it will ensure a greater accuracy of the
        // number of bytes being actually written to the output stream. Bear in mind that if the
        // underlying output stream is buffered the byte written counter tells how many bytes were
        // written to the buffered stream and may not actually be written to their final destination
        // such as disk, socket, etc.
        for( int i = start; i < start + count; i++ ) {
            write( bytes[i] );
        }
    }

}
