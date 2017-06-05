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

import java.io.OutputStream;

/**
 * A base32hex encoding output stream. It encodes in base32hex everything passed to the
 * output stream. This implementation is based on <a
 * href="http://tools.ietf.org/pdf/rfc4648.pdf">RFC4648</a>. Therefore, the output
 * base32hex is on a single line, i.e. it is not truncated to 76 characters per line.
 *
 * @see Base32hexInputStream
 * @see Base32InputStream
 * @see Base32OutputStream
 * @see Base64InputStream
 * @see Base64OutputStream
 * @see Base64urlInputStream
 * @see Base64urlOutputStream
 *
 * @author Yanick Poirier
 */
public class Base32hexOutputStream
        extends Base32OutputStream {

    /**
     * Constructs a Base32 output stream encoder on top of the specified output stream.
     *
     * @param out the underlying output stream.
     */
    public Base32hexOutputStream( OutputStream out ) {
        super( out );
    }

    /**
     * Retrieves the encoding alphabet.
     *
     * @return String
     */
    protected String getAlphabet() {
        return "0123456789ABCDEFGHIJKLMNOPQRSTUV";
    }

}
