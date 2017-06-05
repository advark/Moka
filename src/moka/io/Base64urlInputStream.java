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

import java.io.InputStream;

/**
 * A base64 URL and file safe decoding input stream. It decodes a base64 data read from an
 * input stream. This implementation is based on <a
 * href="http://tools.ietf.org/pdf/rfc4648.pdf">RFC4648</a>. All characters not part of
 * the base64url alphabet are ignored (i.e. CR, LF, TAB, etc.)
 *
 * @see Base32hexInputStream
 * @see Base32hexOutputStream
 * @see Base32InputStream
 * @see Base32OutputStream
 * @see Base64InputStream
 * @see Base64OutputStream
 * @see Base64urlOutputStream
 *
 * @author Yanick Poirier
 */
public class Base64urlInputStream
        extends Base64InputStream {

    /**
     * Constructs a Base64 input stream encoder on top of the specified input stream.
     *
     * @param in the underlying input stream.
     */
    public Base64urlInputStream( InputStream in ) {
        super( in );
    }

    /**
     * Retrieves the encoding alphabet.
     *
     * @return String
     */
    protected String getAlphabet() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    }

}
