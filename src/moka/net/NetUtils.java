/*
 * Copyright (C) 2018 Yanick Poirier <ypoirier at hotmail.com>.
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
package moka.net;

import java.io.IOException;
import java.net.InetAddress;
import moka.util.SystemUtils;

/**
 * This class provides static utility methods related to network communcation and connection.
 *
 * @author Yanick Poirier <ypoirier at hotmail.com>
 */
final public class NetUtils {

    /**
     * Checks if the specified destination is reachable. This method uses the operating system
     * {@code ping} command and wait for it to return. On Windows, the command is
     * {@code ping -n 1 <destination>} while on *nix-like the command is
     * {@code ping -c 1 <destination>}
     *
     * @param dest Must be a valid IP address or hostname.
     *
     * @return {@code true} if the destination is reachable or {@code false} otherwise.
     */
    static public boolean ping( String dest ) {
        return ping( dest, 0 );
    }

    /**
     * Checks if the specified destination is reachable. This method uses the operating system
     * {@code ping} command and wait for it to return. On Windows, the command is
     * {@code ping -n 1 <destination>} while on *nix-like the command is
     * {@code ping -c 1 <destination>}
     *
     * @param dest     Must be a valid IP address or hostname.
     * @param waitTime Number of seconds to wait for the destination to respond. A value of 0 or
     *                 less will use the default wait time. The default value is system dependent but
     *                 is usually 3-4 seconds.
     *
     * @return {@code true} if the destination is reachable or {@code false} otherwise.
     */
    static public boolean ping( String dest,
                                int waitTime ) {
        boolean rc = false;
        String command;
        Process proc;

        if( waitTime <= 0 ) {
            if( SystemUtils.isWindows() ) {
                command = String.format( "ping -n 1 %s", dest );
            }
            else {
                command = String.format( "ping -c 1 %s", dest );
            }
        }
        else {
            if( SystemUtils.isWindows() ) {
                command = String.format( "ping -n 1 -w %d %s", waitTime, dest );
            }
            else {
                command = String.format( "ping -c 1 -W %d %s", waitTime, dest );
            }
        }

        try {
            proc = Runtime.getRuntime().exec( command );
            proc.waitFor();
            rc = proc.exitValue() == 0;
        }
        catch( IOException | InterruptedException ex ) {
            rc = false;
        }

        return rc;
    }

    /**
     * Checks if the specified destination is reachable. This method is equivalent to:
     * <pre><blockquote>
     * ping( destination.getHostAddress());
     * </blockquote></pre>
     *
     * @param dest {@code InetAddress} to ping. Cannot be {@code null}
     *
     * @return {@code true} if the destination is reachable or {@code false} otherwise.
     *
     * @throws NullPointerException if {@code dest} is {@code null}.
     *
     * @see #ping(java.lang.String)
     */
    static public boolean ping( InetAddress dest ) {
        return ping( dest.getHostAddress() );
    }

    /**
     * Checks if the specified destination is reachable. This method is equivalent to:
     * <pre><blockquote>
     * ping( destination.getHostAddress());
     * </blockquote></pre>
     *
     * @param dest     {@code InetAddress} to ping. Cannot be {@code null}
     * @param waitTime Number of seconds to wait for the destination to respond. A value of 0 or
     *                 less will use the default wait time. The default value is system dependent but
     *                 is usually 3-4 seconds.
     *
     * @return {@code true} if the destination is reachable or {@code false} otherwise.
     *
     * @throws NullPointerException if {@code dest} is {@code null}.
     *
     * @see #ping(java.lang.String)
     */
    static public boolean ping( InetAddress dest,
                                int waitTime ) {
        return ping( dest.getHostAddress(), waitTime );
    }

    private NetUtils() {
    }

}
