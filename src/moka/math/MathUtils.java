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
package moka.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author ypoirier
 */
public class MathUtils {

    final static public BigDecimal TWO = new BigDecimal( "2" );

    /** Euler's number (natural logarithm) truncated at 50 decimals */
    final public static BigDecimal e = new BigDecimal( "2.71828182845904523536028747135266249775724709369995" );
    /** The number PI truncated at 50 decimals */
    final public static BigDecimal PI = new BigDecimal( "3.14159265358979323846264338327950288419716939937510" );

    /**
     * Computes the square root of a number.
     *
     * @param number Number of square root.
     *
     * @return a {@code BigDecimal} number containing the square root value of {@code this} or
     *         {@code null} if it cannot be calculated within this method's limits.
     *
     * @throws NullPointerException if {@code number} is {@code null}.
     * @see <a href="https://en.wikipedia.org/wiki/Newton's_method">Newton's method</a> on Wikipedia
     */
    static public BigDecimal squareRoot( BigDecimal number ) {
        return squareRoot( number, new MathContext( number.precision(), RoundingMode.HALF_UP ) );
    }

    /**
     * Computes the square root of a number.
     *
     * @param number    Number of square root.
     * @param precision Maximum number of fractional digit.
     *
     * @return a {@code BigDecimal} number containing the square root value of {@code this} or
     *         {@code null} if it cannot be calculated within this method's limits.
     *
     * @throws ArithmeticException  if {@code this} is negative.
     * @throws NullPointerException if {@code number} is {@code null}.
     */
    static public BigDecimal squareRoot( BigDecimal number,
                                         int precision ) {
        return squareRoot( number, new MathContext( precision, RoundingMode.HALF_UP ) );
    }

    /**
     * Computes the square root of a number.
     *
     * @param number Number to square root.
     * @param mc     {@code MathContext}
     *
     * @return a {@code BigDecimal} number containing the square root value of {@code this} or
     *         {@code null} if it cannot be calculated within this method's limits.
     *
     * @throws NullPointerException if {@code number} is {@code null}.
     * @throws ArithmeticException  if {@code this} is negative.
     */
    static public BigDecimal squareRoot( BigDecimal number,
                                         MathContext mc ) {
        // Check that x >= 0.
        if( number.signum() < 0 ) {
            throw new ArithmeticException( "Square Root of a negative value" );
        }

        // n = x*(10^(2*precision))
        BigInteger n = number.movePointRight( mc.getPrecision() << 1 ).toBigInteger();

        // The first approximation is the upper half of n.
        int bits = ( n.bitLength() + 1 ) >> 1;
        BigInteger ix = n.shiftRight( bits );
        BigInteger ixPrev;

        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            ixPrev = ix;

            // x = (x + n/x)/2
            ix = ix.add( n.divide( ix ) ).shiftRight( 1 );
        }
        while( ix.compareTo( ixPrev ) != 0 );

        return new BigDecimal( ix, mc.getPrecision() ).stripTrailingZeros();
    }

    static public BigDecimal power( BigDecimal number,
                                    BigInteger pow ) {
        BigDecimal y = BigDecimal.ONE;
        BigDecimal x = number;
        BigInteger p = pow;

        // Loop until all bits of the 'pow' value are shifted right
        while( p.compareTo( BigInteger.ZERO ) != 0 ) {
            if( pow.testBit( 0 ) ) {
                // If bit 0 is set, multiply by x
                y = y.multiply( x );
            }
            // Shift one bit of the 'pow' value to the right
            p = p.shiftRight( 1 );

            // Square x
            x = x.multiply( x );
        }

        return y;
    }

    static public BigInteger power( BigInteger number,
                                    BigInteger pow ) {
        BigInteger y = BigInteger.ONE;
        BigInteger x = number;
        BigInteger p = pow;

        do {
            if( pow.testBit( 0 ) ) {
                y = y.multiply( x );
            }

            p = p.shiftRight( 1 );

            x = x.multiply( x );
        }
        while( p.compareTo( BigInteger.ZERO ) != 0 );

        return y;
    }

    static public BigDecimal stirlingApprox( int n ) {
        return stirlingApprox( new BigDecimal( n ) );
    }

    static public BigDecimal stirlingApprox( long n ) {
        return stirlingApprox( new BigDecimal( n ) );
    }

    static public BigDecimal stirlingApprox( double n ) {
        return stirlingApprox( new BigDecimal( n ) );
    }

    static public BigDecimal stirlingApprox( BigInteger n ) {
        return stirlingApprox( new BigDecimal( n ) );
    }

    /**
     * Calculates the Stirling's Approximation of the specified number.
     *
     * @param n Original number
     *
     * @return the Striling's Approximation value which is very close to the factorial value of
     *         {@code n}.
     *
     * @see <a href ="https://en.wikipedia.org/wiki/Stirling's_Approximation">Stirling's
     * approximation</a> on Wikipedia.
     */
    static public BigDecimal stirlingApprox( BigDecimal n ) {
        BigDecimal n1 = n;
        BigDecimal a = squareRoot( TWO.multiply( PI ).multiply( n ) );
        BigDecimal b = power( n1.divide( e ), n.toBigInteger() );
        return a.multiply( b ).stripTrailingZeros();
    }

    /**
     * Returns a string representation of a {@code BigDecimal}, using fractional notation.
     * <p>
     * The fraction returned is a finite one (i.e. 1/3 is not a finite fraction and will never be
     * returned as such). The fraction returned depends on the precision value of this object.
     * <p>
     * <table border="1">
     * <tr><th>original
     * value</th><th>precision()</th><th>toPlainString()</th><th>toFractionString()</th></tr>
     * <tr><td>1.0/3</td><td>51
     * (<i>default</i>)</td><td>0.333333333333333314829616256247390992939472198486328125</td><td>6004799503160661/18014398509481984</td></tr>
     * <tr><td>1.0/3</td><td>20</td><td>0.33333333333333331483</td><td>3333333333/10000000000</td></tr>
     * <tr><td>1.0/3</td><td>10</td><td>0.3333333333</td><td>33333/100000</td></tr>
     * <tr><td>1.0/3</td><td>5</td><td>0.33333</td><td>33333/100000</td></tr>
     * <tr><td>1.0/3</td><td>2</td><td>0.33</td><td>33/100</td></tr>
     * <tr><td>1.0/3</td><td>1</td><td>0.3</td><td>3/10</td></tr>
     * </table>
     * <p>
     * The returned fraction is always reduced to its minimal value. For example, 0.54 will return
     * "27/50" and not "54/100".
     *
     * @param number Decimal number.
     *
     * @return string representation of this {@code BigDecimal}, using fractional notation.
     */
    static public String toFractionString( BigDecimal number ) {
        MathContext mc = new MathContext( number.precision() );
        String[] parts = number.toPlainString().split( "\\." );

        if( parts.length > 1 ) {
            // Denominator
            BigInteger den = BigDecimal.TEN.pow( parts[1].length(), mc ).toBigInteger();
            // Numerator
            BigInteger num = new BigDecimal( parts[0] ).multiply( new BigDecimal( den, mc ) ).add( new BigDecimal( parts[1], mc ) ).toBigInteger();

            // Get the greater common divisor
            BigInteger gcd = num.gcd( den );

            num = num.divide( gcd );
            den = den.divide( gcd );

            // Get the integer part
            BigInteger integer = num.divide( den );
            // Adjust the numerator
            num = num.subtract( integer.multiply( den ) );

            if( integer.compareTo( BigInteger.ZERO ) == 0 ) {
                // No integer
                return num.toString() + "/" + den.toString();
            }

            // Integer and fraction
            return integer.toString() + " " + num.toString() + "/" + den.toString();
        }

        // No fraction
        return number.toPlainString();
    }

    /**
     * Returns a string representation of a {@code double} value, using fractional notation.
     *
     * @param number Decimal number as {@code double}
     *
     * @return string representation of this {@code BigDecimal}, using fractional notation.
     */
    static public String toFractionString( double number ) {
        return toFractionString( new BigDecimal( number ) );
    }

    private MathUtils() {
    }

}
