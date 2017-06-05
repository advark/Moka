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

/**
 * This utility class performs padding of string either on left (prefix), on the right
 * (suffix) or both using a single character, for example, the space character.
 * <p>
 * <b>Note</b>: When padding on both sides and the string to be padded has a odd length
 * and the maximum length of the padded string is reached, a extra padding character is
 * appended. For example, if the string as a length of 5, and we want to pad it with 5
 * characters on both side but the maximum length is not to exceed 10 character, the result
 * will be 2 characters + string + 3 characters.
 *
 * @author Yanick Poirier
 */
public class StringUtils {

    /**
     * Pads the left side of the string (prefix).
     *
     * @param padChar Padding character.
     * @param count   Number of padding character to insert.
     * @param string  String to be padded.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String padLeft( final char padChar,
                                  final int count,
                                  final String string ) {
        return getPadString( padChar, count ) + string;
    }

    /**
     * Pads the left side of the string (prefix).
     *
     * @param padChar   Padding character.
     * @param count     Number of padding character.
     * @param string    String to be padded.
     * @param maxLength Maximum length of the string not to exceed with the
     *                  padding.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String padLeft( final char padChar,
                                  final int count,
                                  final String string,
                                  final int maxLength ) {
        int c = count;
        if( c + string.length() > maxLength ) {
            c = maxLength - string.length();
        }
        return padLeft( padChar, c, string );
    }

    /**
     * Pads the left side of the string (prefix). The method will pad the string until it
     * reaches its maximum length.
     *
     * @param padChar   Padding character.
     * @param string    String to be padded.
     * @param maxLength Maximum length of the string not to exceed with the
     *                  padding.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String padLeft( final char padChar,
                                  final String string,
                                  final int maxLength ) {
        return padLeft( padChar, maxLength - string.length(), string );
    }

    /**
     * Pads the right side of the string (suffix).
     *
     * @param padChar Padding character.
     * @param count   Number of padding character to insert.
     * @param string  String to be padded.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String padRight( final char padChar,
                                   final int count,
                                   final String string ) {
        return string + getPadString( padChar, count );
    }

    /**
     * Pads the right side of the string (suffix).
     *
     * @param padChar   Padding character.
     * @param count     Number of padding character.
     * @param string    String to be padded.
     * @param maxLength Maximum length of the string not to exceed with the
     *                  padding.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String padRight( final char padChar,
                                   final int count,
                                   final String string,
                                   final int maxLength ) {
        int c = count;
        if( c + string.length() > maxLength ) {
            c = maxLength - string.length();
        }
        return padRight( padChar, c, string );
    }

    /**
     * Pads the right side of the string (suffix). The method will pad the string until it
     * reaches its maximum length.
     *
     * @param padChar   Padding character.
     * @param string    String to be padded.
     * @param maxLength Maximum length of the string not to exceed with the
     *                  padding.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String padRight( final char padChar,
                                   final String string,
                                   final int maxLength ) {
        return padRight( padChar, maxLength - string.length(), string );
    }

    /**
     * Pads the both sides of the string (prefix and suffix).
     *
     * @param padChar Padding character.
     * @param count   Number of padding character to insert on each side.
     * @param string  String to be padded.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String pad( final char padChar,
                              final int count,
                              final String string ) {
        return padLeft( padChar, count, padRight( padChar, count, string ) );
    }

    /**
     * Pads the both sides of the string (prefix and suffix).
     *
     * @param padChar   Padding character.
     * @param count     Number of padding character to insert on each side.
     * @param string    String to be padded.
     * @param maxLength Maximum length of the string not to exceed with the
     *                  padding.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String pad( final char padChar,
                              final int count,
                              final String string,
                              final int maxLength ) {
        int leftCount = 0;
        int rightCount = 0;

        if( string.length() < maxLength ) {
            if( ( count * 2 ) + string.length() > maxLength ) {
                /* We are exceeding the maximum length */
                leftCount = ( maxLength - string.length() ) / 2;

                if( ( maxLength - string.length() ) % 2 == 0 ) {
                    /*
                     * The total padding count is even. Split equally between
                     * left and right
                     */
                    rightCount = leftCount;
                }
                else {
                    /*
                     * The total padding count is odd. The right will have an
                     * extra padding.
                     */
                    rightCount = leftCount + 1;
                }
            }
            else {
                leftCount = count;
                rightCount = count;
            }
        }

        return padLeft( padChar, leftCount, padRight( padChar, rightCount,
                                                      string ) );
    }

    /**
     * Pads the both sides of the string (prefix and suffix). The method will pad the
     * string until it reaches its maximum length.
     *
     * @param padChar   Padding character.
     * @param string    String to be padded.
     * @param maxLength Maximum length of the string not to exceed with the
     *                  padding.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String pad( final char padChar,
                              final String string,
                              final int maxLength ) {
        StringBuffer sb = new StringBuffer( 1 );
        sb.append( padChar );

        int count = ( maxLength - string.length() ) / 2;

        if( count <= 0 ) {
            return string;
        }

        return pad( padChar, count, string, maxLength ) +
               ( ( ( maxLength - string.length() ) % 2 == 0 ) ? "" : new String( sb ) );
    }

    static private String getPadString( final char padChar,
                                        final int count ) {
        String res = "";
        for( int i = 0; i < count; i++ ) {
            res += padChar;
        }

        return res;
    }

    /**
     * Removes all the specified character that starts the string. For example,
     *
     * <blockquote>
     *
     * <pre>
     * s = StringUtilities.trimLeft( &quot;xxxxAbc defxxxx&quot;, 'x' );
     * </pre>
     *
     * </blockquote>
     *
     * will return "<i>Abc defxxxx</i>".
     *
     * @param string   String to trim.
     * @param trimChar Character to remove.
     *
     * @return Trimmed string.
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String stripLeft( final String string,
                                    final char trimChar ) {
        String s = "";
        int i = 0;
        boolean done = false;

        while( i < string.length() && !done ) {
            if( string.charAt( i ) == trimChar ) {
                i++;
            }
            else {
                done = true;
            }
        }

        if( i < string.length() ) {
            s = string.substring( i );
        }
        else if( !done ) {
            s = "";
        }
        else {
            s = string;
        }

        return s;
    }

    /**
     * Removes all the specified character that ends the string. For example,
     *
     * <blockquote>
     *
     * <pre>
     * s = StringUtilities.trimRight( &quot;xxxxAbc defxxxx&quot;, 'x' );
     * </pre>
     *
     * </blockquote>
     *
     * will return <i>xxxxAbc def</i>.
     *
     * @param string   String to trim.
     * @param trimChar Character to remove.
     *
     * @return Trimmed string.
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String stripRight( final String string,
                                     final char trimChar ) {
        String s = string;
        int i = string.length() - 1;
        boolean done = false;

        if( i > 0 ) {
            while( i >= 0 && !done ) {
                if( string.charAt( i ) == trimChar ) {
                    i--;
                }
                else {
                    done = true;
                }
            }

            if( i >= 0 ) {
                s = string.substring( 0, i + 1 );
            }
            else {
                s = "";
            }
        }
        else if( i == 0 ) {
            if( string.charAt( 0 ) == trimChar ) {
                s = "";
            }
        }

        return s;
    }

    /**
     * Removes all the specified character at both ends the string. The method is equivalent
     * to:
     *
     * <blockquote>
     *
     * <pre>
     * s = StringUtilities.trimLeft( StringUtilities.trimRight( &quot;xxxxAbc defxxxx&quot;, 'x' ), 'x' );
     * </pre>
     *
     * </blockquote>
     *
     * @param string   String to trim.
     * @param trimChar Character to remove.
     *
     * @return Trimmed string.
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String strip( final String string,
                                final char trimChar ) {
        return stripLeft( stripRight( string, trimChar ), trimChar );
    }

    /**
     * Removes all occurrences of the specified character within the specified string.
     *
     * @param string Original string.
     * @param ch     Character to remove.
     *
     * @return String stripped of all {@code ch}.
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String removeAllOccurences( final String string,
                                              final char ch ) {
        String s = string;
        int i = string.indexOf( ch );

        while( i > -1 ) {
            if( i == 0 ) {
                s = string.substring( 1 );
            }
            else {
                s = s.substring( 0, i ) + s.substring( i + 1 );
            }

            i = s.indexOf( ch );
        }

        return s;
    }

    /**
     * Removes the character at the specified index position from the string.
     *
     * @param string Original string.
     * @param index  0 based index position of the character to remove.
     *
     * @return String
     *
     * @throws NullPointerException if {@code string} is {@code null}.
     */
    static public String removeCharAt( final String string,
                                       final int index ) {
        String s;

        if( index > -1 && index < string.length() ) {
            if( index == 0 ) {
                s = string.substring( 1 );
            }
            else {
                s = string.substring( 0, index );
                if( index < string.length() ) {
                    s += string.substring( index + 1 );
                }
            }
        }
        else {
            s = string;
        }

        return s;
    }

    /**
     * Extracts a substring located between two specified substring. If no ending string
     * marker is specified, the extraction is up to the end of the original string.
     *
     * @param string Original string.
     * @param start  Starting string marker.
     * @param end    ending string marker. Can be {@code null}.
     *
     * @return String
     */
    static public String extract( final String string,
                                  final String start,
                                  final String end ) {
        String data = null;
        int s = string.indexOf( start );
        int e;

        if( end != null ) {
            e = string.indexOf( end );
        }
        else {
            e = string.length();
        }

        if( s > -1 ) {
            if( e > -1 ) {
                data = string.substring( s + start.length(), e );
            }
            else {
                data = string.substring( s + start.length() );
            }
        }

        return data;
    }

    /**
     * Extracts a substring located after a specified substring. This is equivalent to:
     * <blockquote>{@code
     * extract( string, start, null );
     * }</blockquote>
     *
     * @param string Original string.
     * @param start  Starting string marker.
     *
     * @return String
     */
    static public String extract( final String string,
                                  final String start ) {
        return extract( string, start, null );
    }

    static public String truncateLeft( final String string,
                                       final int maxLength ) {
        if( string.length() >= maxLength ) {
            return string.substring( string.length() - maxLength, string.length() );
        }

        return string;
    }

    static public String truncateRight( final String string,
                                        final int maxLength ) {
        if( string.length() >= maxLength ) {
            return string.substring( 0, string.length() - maxLength );
        }

        return string;
    }

    private StringUtils() {
    }

}
