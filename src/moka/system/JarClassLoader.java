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
package moka.system;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A {@code ClassLoader} that allows dynamic class loading in Jar files that are not in
 * the classpath.
 *
 * @author Yanick Poirier
 *
 * @see DigestClassLoader
 */
public class JarClassLoader
        extends ClassLoader {

    /**
     * Initializes this object using the default digest algorithm. See description
     * for details.
     *
     * @param parent Parent class loader.
     */
    public JarClassLoader( ClassLoader parent ) {
        super( parent );

        mClassPaths = new ArrayList<>();
        mLoadedClasses = new HashMap<>();
    }

    /**
     * Adds a new Jar file to search from. If the file is already in the search
     * list, the request is simply ignored.
     *
     * @param fileName Name of the Jar file.
     *
     * @throws FileNotFoundException if the file cannot be found.
     * @throws NullPointerException  if {@code filename} is {@code null}
     */
    public void add( String fileName )
            throws FileNotFoundException {
        add( new File( fileName ) );
    }

    /**
     * Adds new Jar file to search from. If the file is already in the search list, the
     * request is simply ignored.
     *
     * @param file Jar file to add.
     *
     * @throws FileNotFoundException if the {@code file} cannot be found or is
     *                               a directory.
     * @throws NullPointerException  if {@code file} is {@code null}
     */
    public void add( File file )
            throws FileNotFoundException {

        try {
            if( file.exists() &&
                !file.isDirectory() &&
                findJar( file.getCanonicalPath() ) != null ) {
                mClassPaths.add( file );
            }
            else {
                throw new FileNotFoundException( "JarClassLoader: Cannot find " +
                                                 file.getAbsolutePath() );
            }
        }
        catch( IOException ex ) {
        }
    }

    /**
     * Removes the specified file from the dynamic class path. This method only
     * removes the file from the search path and does not prevent a loaded class
     * from that file to be reused in future.
     *
     * @param fileName Name of the file to remove.
     *
     * @return {@code true} on success or {@code false} otherwise.
     */
    public boolean remove( String fileName ) {
        return remove( new File( fileName ) );
    }

    /**
     * Removes the specified file from the dynamic class path. This method only
     * removes the file from the search path and does not prevent a loaded class
     * from that file to be reused in future.
     *
     * @param file File to remove.
     *
     * @return {@code true} on success or {@code false} otherwise.
     */
    public boolean remove( File file ) {
        boolean found = false;
        int i = 0;

        while( i < mClassPaths.size() && !found ) {
            File f = mClassPaths.get( i );

            if( file.compareTo( f ) == 0 ) {
                mClassPaths.remove( i );
                found = true;
            }
            i++;
        }
        return found;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected URL findResource( String resName ) {
        URL url = null;

        JarFile jarFile = null;
        JarEntry entry;
        boolean found = false;
        File file;

        int i = 0;
        // Loop through all search file entries until one is found
        while( i < mClassPaths.size() && !found ) {
            file = mClassPaths.get( i );

            if( file.exists() ) {
                try {
                    jarFile = new JarFile( file );
                }
                catch( IOException ex ) {
                    return null;
                }

                Enumeration<JarEntry> entries = jarFile.entries();

                // Loop through all Jar entries until found.
                while( entries.hasMoreElements() && !found ) {
                    entry = entries.nextElement();
                    String e = entry.getName();

                    if( e.compareTo( resName ) == 0 ) {
                        found = true;
                    }
                }
            }

            i++;
        }

        if( found && jarFile != null ) {
            try {
                url = new URL( "jar:file:" + jarFile.getName() + "!/" );
            }
            catch( MalformedURLException ex ) {
                return null;
            }
        }

        return url;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public URL getResource( String resName ) {
        return findResource( resName );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public InputStream getResourceAsStream( String resName ) {
        JarFile file;
        JarEntry entry;
        InputStream is = null;
        URL url = getResource( resName );

        if( url != null ) {
            // Removes the leading "jar:" and trailing "!/" characters from
            // the URL.
            String fileName = url.getFile().substring( 5 );
            fileName = fileName.substring( 0, fileName.length() - 2 );

            try {
                file = new JarFile( fileName );
                entry = file.getJarEntry( resName );
                is = file.getInputStream( entry );
            }
            catch( IOException ex ) {
                is = null;
            }
        }

        return is;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected Class<?> findClass( String className )
            throws ClassNotFoundException {
        byte[] resBytes;
        Class<?> cl = _findLoadedClass( className );

        if( cl == null ) {
            try {
                resBytes = loadClassData( className );
            }
            catch( Exception ex ) {
                throw new ClassNotFoundException( ex.getMessage(), ex );
            }

            if( resBytes != null ) {
                cl = defineClass( className, resBytes, 0, resBytes.length );

                // Save the class for future references.
                mLoadedClasses.put( className, cl );
            }
            else {
                throw new ClassNotFoundException( "Cannot find class <" + className + ">" );
            }
        }

        return cl;
    }

    /**
     * Checks if the specified class is already loaded.
     *
     * @param className Name of the class to check.
     *
     * @return {@code Class} object if found or {@code null}.
     */
    protected Class<?> _findLoadedClass( String className ) {
        Class<?> cl;

        cl = mLoadedClasses.get( className );

        if( cl == null ) {
            try {
                cl = findSystemClass( className );
            }
            catch( ClassNotFoundException e ) {
                // The class is not found. Not an error... yet.
            }
        }

        return cl;
    }

    /**
     * Loads the class raw byte code data.
     *
     * @param className Name of the class to load.
     *
     * @return {@code byte[]} or {@code null} if the class cannot be found.
     *
     * @throws Exception if an error occurs.
     */
    protected byte[] loadClassData( String className )
            throws Exception {
        byte[] buffer = null;
        String fName = className.replace( '.', '/' ) + ".class";
        InputStream is = getResourceAsStream( fName );

        if( is != null ) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try {
                // Reads all the class bytes.
                int b = is.read();
                while( b != -1 ) {
                    bos.write( b );
                    b = is.read();
                }

                // Retrieves the byte buffer
                buffer = bos.toByteArray();
            }
            catch( IOException ex ) {
                return null;
            }
        }

        return buffer;
    }

    /**
     * Searches the dynamic classpath for the specified Jar name.
     *
     * @param name Name of the Jar file to find.
     *
     * @return {@code File} object of the matching Jar file or {@code null} if not found.
     */
    protected File findJar( String name ) {
        File jarFile = null;
        int i = 0;
        boolean found = false;

        while( !found && i < mClassPaths.size() ) {
            jarFile = mClassPaths.get( i );

            try {
                found = jarFile.getCanonicalPath().equals( name );
            }
            catch( IOException ex ) {
            }
            i++;
        }

        return found ? jarFile : null;
    }

    private final ArrayList<File> mClassPaths;
    private final HashMap<String, Class<?>> mLoadedClasses;
}
