/*
 * Copyright (C) 2011-2018 Yanick Poirier
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
package moka.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

/**
 *
 * @author Yanick Poirier <ypoirier at hotmail.com>
 */
final public class InetAddressUtils {

    /**
     * Retrieves the subnet mask of the specified IPv4 local address.
     *
     * @param addr IPv4 local address.
     *
     * @return an {@code Inet4Address} containing the subnet mask or {@code null} if the subnet mask
     *         cannot be determine.
     */
    static public Inet4Address getSubnetMask( Inet4Address addr ) {
        Inet4Address mask = null;
        List<InterfaceAddress> ifsAddr;

        // Retrieve the list of InterfaceAddress
        try {
            NetworkInterface ni = NetworkInterface.getByInetAddress( addr );
            ifsAddr = ni.getInterfaceAddresses();
        }
        catch( SocketException ex ) {
            return null;
        }

        int i = 0;

        // Loop for the number InterfaceAddress
        while( i < ifsAddr.size() ) {
            InterfaceAddress ia = ifsAddr.get( i );

            if( ia.getAddress().equals( addr ) ) {
                // Get the prefix length, i.e. the number of bits that compose the net mask.
                int prefix = ia.getNetworkPrefixLength();

                // Build a binary bit mask by shifting down the sign bit for the number of bit in the prefix.
                int m = 1 << 31;
                for( int j = prefix - 1; j > 0; j-- ) {
                    m >>= 1;
                }

                // Create an Inet4Address object which address is the net mask itself.
                try {
                    mask = (Inet4Address) InetAddress.getByName( String.format( "%d.%d.%d.%d",
                                                                                ( m >> 24 ) & 0xFF,
                                                                                ( m >> 16 ) & 0xFF,
                                                                                ( m >> 8 ) & 0xFF,
                                                                                m & 0xFF ) );
                }
                catch( UnknownHostException ex ) {
                }

                // We are done, break the loop.
                break;
            }
            i++;
        }

        return mask;
    }

    private InetAddressUtils() {

    }

}
