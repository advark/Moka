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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows Java applications to interact with the standard Windows INI configuration
 * files.
 *
 * @author Yanick Poirier
 */
public class INIProperties {

    private ArrayList<Node> nodes;

    public INIProperties() {
    }

    public List<String> getKeywords( String section ) {
        return null;
    }

    public void putKeyword( String section,
                            String keyword,
                            String value ) {

    }

    public void removeKeyword( String section,
                               String keyword ) {

    }

    public void removeAllKeywords( String section ) {

    }

    public List<String> getSections() {
        return null;
    }

    public void addSection( String section ) {

    }

    public void removeSection( String section ) {
        removeAllKeywords( section );
    }

    public void removeAllSections() {

    }

    public void read( File file )
            throws FileNotFoundException {
        read( new BufferedReader( new FileReader( file ) ) );
    }

    public void read( InputStream is ) {
        read( new BufferedReader( new InputStreamReader( is ) ) );
    }

    public void read( Reader reader ) {

    }

    public void write( File file )
            throws IOException {
        write( new FileWriter( file ) );
    }

    public void write( OutputStream os ) {
        write( new OutputStreamWriter( os ) );
    }

    public void write( Writer writer ) {

    }

    /**
     * This class encapsulates a single INI keyword.
     */
    public class Keyword
            extends Node {

        public Keyword() {
            super( Node.NodeType.NODE_KEYWORD );
            name = null;
            value = null;
        }

        public Keyword( String name,
                        String value ) {
            this();
            this.name = name;
            this.value = value;
        }

        public void setName( String name ) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setValue( String value ) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getExpandedValue() {
            return value;
        }

        private String name;
        private String value;
    }

    /**
     * This class encapsulates a single INI section.
     */
    public class Section
            extends Node {

        public Section() {
            super( Node.NodeType.NODE_SECTION );
            name = null;
            nodes = new ArrayList<>();
        }

        public Section( String name ) {
            this();

            this.name = name;
        }

        public void setName( String name ) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private String name;
        private ArrayList<Node> nodes;
    }

    /**
     * This class encapsulates a single INI comment. In a INI file, comment always starts the line
     * with a semi-colon(;). Comments cannot be inserted at the end of a keyword/value pair.
     */
    public class Comment
            extends Node {

        public Comment() {
            super( Node.NodeType.NODE_COMMENT );
        }

        public Comment( String comment ) {
            this();
            this.comment = comment;
        }

        public void set( String comment ) {
            this.comment = comment;
        }

        public String get() {
            return comment;
        }

        private String comment;
    }

    static private class Node {

        public enum NodeType {
            NODE_BLANKLINE,
            NODE_KEYWORD,
            NODE_SECTION,
            NODE_COMMENT
        }

        public Node( NodeType type ) {
            this.type = type;
        }

        public NodeType getType() {
            return type;
        }

        final private NodeType type;
    }
}
