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
 * More details about the licensing can be found [here](https://www.gnu.org/licenses/lgpl-3.0.en.html).
 */
package exception;

/**
 * This exception is thrown when the format of data is not valid.
 *
 * @see moka.util.INIProperties#read(Reader)
 * @author Yanick Poirier
 */
public class IllegalFormatException
        extends Exception {

    private static final long serialVersionUID = -670358089727896993L;

    public IllegalFormatException() {
        super();
    }

    public IllegalFormatException( String msg ) {
        super( msg );
    }

    public IllegalFormatException( Throwable t ) {
        super( t );
    }

    public IllegalFormatException( String msg,
                                   Throwable t ) {
        super( msg, t );
    }

}
