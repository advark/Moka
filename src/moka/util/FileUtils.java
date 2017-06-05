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
package moka.util;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Yanick Poirier
 */
public class FileUtils {

    /**
     * Retrieves the extension of the specified file object. The file extension
     * is the part that is after the last dot '.' in the file name.
     *
     * @param file File object.
     *
     * @return Returns the file extension. If there is no extension and the file
     *         name does not end with a period (.), <code>null</code> is
     *         returned. If the name ends with a period, an empty string is
     *         returned.
     */
    static public String getExtension( File file ) {
        String ext = null;
        String fullName = file.getAbsolutePath();

        int i = fullName.lastIndexOf( '.' );

        if( i > 0 ) {
            if( i < fullName.length() - 1 ) {
                // There is a least 1 character in the extension.
                ext = fullName.substring( i + 1 );
            }
            else {
                ext = "";
            }
        }

        return ext;
    }

    /**
     * Retrieves the current directory.
     *
     * @return <code>File</code> or <code>null</code> if the current directory
     *         cannot be established.
     */
    static public File currentDirectory() {
        return new File( "," ).getAbsoluteFile();
    }

    static public File[] findFiles( File path,
                                    String mask,
                                    boolean recursive ) {
        ArrayList<File> files = new ArrayList<>();

        return null;
    }

    static public File[] findFiles( File path,
                                    String mask ) {
        return findFiles( path, mask, false );
    }

    static public File[] findFiles( String path,
                                    String mask ) {
        return findFiles( new File( path ), mask );
    }

    static public File[] findFiles( String path,
                                    String mask,
                                    boolean recursive ) {
        return findFiles( new File( path ), mask, recursive );
    }

    private FileUtils() {
    }

}
