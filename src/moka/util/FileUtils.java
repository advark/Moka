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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class provides complementary static methods that extend or add functionalities to the
 * {@code File} class.
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
        return new File( "." ).getAbsoluteFile();
    }

    /**
     * Finds all the files in the specified directory that matches the {@code FilenameFilter}
     * pattern. The directories will not appear in the returned array.
     *
     * @param dir       Directory where the search will begin
     * @param pattern   Pattern that a file name must match.
     * @param recursive If {@code true}, the search will be propagated to the sub-directories as
     *                  well. If {@code false}, the search is not propagated to the sub-directories.
     *
     * @return An array of {@code File} object.
     */
    static public File[] findFiles( File dir,
                                    FilenameFilter pattern,
                                    boolean recursive ) {
        ArrayList<File> files = new ArrayList<>();

        File root = new File( "", dir.getAbsolutePath() );
        String list[] = root.list( pattern );

        for( String name : list ) {
            File f = new File( name );
            if( f.isDirectory() && recursive ) {
                files.addAll( Arrays.asList(
                        findFiles( new File( dir.getAbsolutePath() ), pattern, recursive )
                ) );
            }
            else {
                files.add( f );
            }
        }

        return (File[]) files.toArray();
    }

    /**
     * Finds all the files in the specified directory that matches the {@code FilenameFilter}
     * pattern. The directories will not appear in the returned array. The sub-directories of
     * {@code dir} are not searched.
     *
     * @param dir     Directory where the search will begin
     * @param pattern Pattern that a file name must match.
     *
     * @return An array of {@code File} object.
     */
    static public File[] findFiles( File dir,
                                    FilenameFilter pattern ) {
        return findFiles( dir, pattern, false );
    }

    /**
     * Finds all the files in the specified directory that matches the {@code FilenameFilter}
     * pattern. The directories will not appear in the returned array. The sub-directories of
     * {@code dir} are not searched.
     *
     * @param dir     Directory where the search will begin
     * @param pattern Pattern that a file name must match.
     *
     * @return An array of {@code File} object.
     */
    static public File[] findFiles( String dir,
                                    FilenameFilter pattern ) {
        return findFiles( new File( dir ), pattern );
    }

    /**
     * Finds all the files in the specified directory that matches the {@code FilenameFilter}
     * pattern. The directories will not appear in the returned array.
     *
     * @param dir       Directory where the search will begin
     * @param pattern   Pattern that a file name must match.
     * @param recursive If {@code true}, the search will be propagated to the sub-directories as
     *                  well. If {@code false}, the search is not propagated to the sub-directories.
     *
     * @return An array of {@code File} object.
     */
    static public File[] findFiles( String dir,
                                    FilenameFilter pattern,
                                    boolean recursive ) {
        return findFiles( new File( dir ), pattern, recursive );
    }

    private FileUtils() {
    }

}
