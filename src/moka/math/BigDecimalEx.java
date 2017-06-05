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
 * This class extends the {@linkplain BigDecimal} class to add some functionalities.
 * <p/>
 * <b>BigDecimal and BigDecimalEx</b>
 * <p/>
 * Because this class and the {@linkplain BigDecimal} class are both immutable a <i>copy</i>
 * constructor
 * has been added to class in order to keep the method chaining working. For example, the following
 * code works
 *
 * <blockquote><pre>
 * BigDecimal a = new BigDecimal( "2" );
 * BigDecimal b = a.mulitply( 50 ).divide( 10 );     <i>// b == 10</i>
 * </pre></blockquote>
 *
 * but the following will not work
 *
 * <blockquote><pre>
 * BigDecimalEx a = new BigDecimalEx( "2" );
 * BigDecimalEx b = a.mulitply( 50 ).squareRoot();  <i>// Won't compile</i>
 * </pre></blockquote>
 *
 * This is due because the {@code multiply(50)} returns a {@code BigDecimal} object that doesn't
 * have
 * a {@code squareRoot} method. To overcome this, either all the {@code BigDecimal} methods would
 * have
 * to be overrode or new <i>copy</i> constructor has to be implemented. The latter solution was
 * chosen
 *
 * <blockquote><pre>
 * BigDecimalEx a = new BigDecimalEx( "2" );
 * BigDecimalEx b = new BigDecimalEx( a.mulitply( 50 ).squareRoot());   <i>// b == 10</i>
 * a = new BigDecimalEx( b.divide(a),subtract( BigDecimal.ONE );        <i>// a == 4</i>
 * </pre></blockquote>
 *
 * @author Yanick Poirier
 */
public class BigDecimalEx
        extends BigDecimal {

    private static final long serialVersionUID = -693149768930070193L;

//    private static final int DEF_SCALE = 20;
    /** The value 2 with a scale of 0 */
    final static public BigDecimalEx TWO = new BigDecimalEx( 2 );

    /** Euler's number (natural logarithm) truncated to 50 decimals */
    final public static BigDecimal e = new BigDecimalEx( "2.71828182845904523536028747135266249775724709369995" );
    /** The number PI truncated to 50 decimals */
    final public static BigDecimal PI = new BigDecimalEx( "3.14159265358979323846264338327950288419716939937510" );

    /**
     * Translate a {@code BigDecimal} into a {@code BigDecimalEx}. See class description.
     *
     * @param val {@code BigDecimal} value to be converted to {@code BigDecimalEx}
     */
    public BigDecimalEx( BigDecimal val ) {
        super( val.toString() );
    }

    /**
     * Translates a {@code BigInteger} into a {@code BigDecimalEx}. The scale of the
     * {@code BigDecimalEx}
     * is zero.
     *
     * @param val {@code BigInteger} value to be converted to {@code BigDecimalEx}.
     */
    public BigDecimalEx( BigInteger val ) {
        super( val );
    }

    /**
     * Translates a {@code BigInteger} unscaled value and an {@code int} scale into a
     * {@code BigDecimalEx}.
     * The value of the {@code BigDecimalEx} is (<code>unscaledVal × 10<sup>-scale</sup></code>).
     *
     * @param unscaledVal - unscaled value of the {@code BigDecimalEx}.
     * @param scale       - scale of the {@code BigDecimalEx}.
     */
    public BigDecimalEx( BigInteger unscaledVal,
                         int scale ) {
        super( unscaledVal, scale );
    }

    /**
     * Translates a {@code BigInteger} unscaled value and an {@code int} scale into a
     * {@code BigDecimalEx},
     * with rounding according to the context settings. The value of the {@code BigDecimalEx} is
     * (<code>unscaledVal × 10<sup>-scale</sup></code>), rounded according to the precision and
     * rounding mode settings.
     *
     * @param unscaledVal unscaled value of the {@code BigDecimalEx}.
     * @param scale       scale of the {@code BigDecimal}.
     * @param mc          the context to use.
     *
     * @throws ArithmeticException if the result is inexact but the rounding mode is
     *                             {@code UNNECESSARY}.
     */
    public BigDecimalEx( BigInteger unscaledVal,
                         int scale,
                         MathContext mc ) {
        super( unscaledVal, scale, mc );
    }

    /**
     * Translates a {@code BigInteger} into a {@code BigDecimalEx} rounding according to the context
     * settings. The scale of the {@code BigDecimalEx} is zero.
     *
     * @param val {@code BigInteger} value to be converted to {@code BigDecimalEx}.
     * @param mc  the context to use.
     *
     * @throws ArithmeticException if the result is inexact but the rounding mode is
     *                             {@code UNNECESSARY}.
     */
    public BigDecimalEx( BigInteger val,
                         MathContext mc ) {
        super( val, mc );
    }

    /**
     * Translates a character array representation of a {@code BigDecimalEx} into a
     * {@code BigDecimalEx},
     * accepting the same sequence of characters as the {@link BigDecimalEx(String)} constructor.
     * <p>
     * Note that if the sequence of characters is already available as a character array, using this
     * constructor is faster than converting the char array to string and using the
     * {@code BigDecimal(String)} constructor .
     *
     * @param in char array that is the source of characters.
     *
     * @throws NumberFormatException if in is not a valid representation of a {@code BigDecimalEx}.
     */
    public BigDecimalEx( char[] in ) {
        super( in );
    }

    /**
     * Translates a character array representation of a {@code BigDecimalEx} into a
     * {@code BigDecimalEx},
     * accepting the same sequence of characters as the {@link BigDecimalEx(String)} constructor,
     * while allowing a sub-array to be specified.
     * <p>
     * Note that if the sequence of characters is already available within a character array, using
     * this constructor is faster than converting the char array to string and using the
     * {@code BigDecimalEx(String)} constructor .
     *
     * @param in     char array that is the source of characters.
     * @param offset first character in the array to inspect.
     * @param len    number of characters to consider.
     *
     * @throws NumberFormatException if in is not a valid representation of a {@code BigDecimalEx}
     *                               or the defined sub-array is not wholly within in.
     */
    public BigDecimalEx( char[] in,
                         int offset,
                         int len ) {
        super( in, offset, len );
    }

    /**
     * Translates a character array representation of a {@code BigDecimalEx} into a
     * {@code BigDecimalEx},
     * accepting the same sequence of characters as the {@link BigDecimalEx(String)} constructor,
     * while
     * allowing a sub-array to be specified and with rounding according to the context settings.
     * <p>
     * Note that if the sequence of characters is already available within a character array, using
     * this constructor is faster than converting the char array to string and using the
     * {@code BigDecimalEx(String)} constructor .
     *
     * @param in     char array that is the source of characters.
     * @param offset first character in the array to inspect.
     * @param len    number of characters to consider..
     * @param mc     the context to use.
     *
     * @throws ArithmeticException   if the result is inexact but the rounding mode is
     *                               {@code UNNECESSARY}.
     * @throws NumberFormatException if in is not a valid representation of a {@code BigDecimalEx}
     *                               or
     *                               the defined subarray is not wholly within in.
     */
    public BigDecimalEx( char[] in,
                         int offset,
                         int len,
                         MathContext mc ) {
        super( in, offset, len, mc );
    }

    /**
     * Translates a character array representation of a {@code BigDecimalEx} into a
     * {@code BigDecimalEx},
     * accepting the same sequence of characters as the {@link BigDecimalEx(String)} constructor and
     * with rounding according to the context settings.
     * <p>
     * Note that if the sequence of characters is already available as a character array, using this
     * constructor is faster than converting the char array to string and using the
     * {@code BigDecimalEx(String)} constructor.
     *
     * @param in char array that is the source of characters.
     * @param mc the context to use.
     *
     * @throws ArithmeticException   if the result is inexact but the rounding mode is
     *                               {@code UNNECESSARY}.
     * @throws NumberFormatException if in is not a valid representation of a {@code BigDecimalEx}.
     */
    public BigDecimalEx( char[] in,
                         MathContext mc ) {
        super( in, mc );
    }

    /**
     * Translates a {@code double} into a {@code BigDecimalEx} which is the exact decimal
     * representation
     * of the {@code double}'s binary floating-point value. The scale of the returned
     * {@code BigDecimalEx}
     * is the smallest value such that (<code>10<sup>scale</sup> × val</code>) is an integer.
     * <p>
     * <b>Notes:</b>
     * <ol>
     * <li>The results of this constructor can be somewhat unpredictable. One might assume that
     * writing
     * {@code new BigDecimal(0.1)} in Java creates a {@code BigDecimalEx} which is exactly equal to
     * 0.1
     * (an unscaled value of 1, with a scale of 1), but it is actually equal to
     * 0.1000000000000000055511151231257827021181583404541015625. This is because 0.1 cannot be
     * represented
     * exactly as a {@code double} (or, for that matter, as a binary fraction of any finite length).
     * Thus, the value that is being passed in to the constructor is not exactly equal to 0.1,
     * appearances
     * notwithstanding.</li>
     * <li>The {@code String} constructor, on the other hand, is perfectly predictable: writing
     * {@code new BigDecimal("0.1")} creates a {@code BigDecimalEx} which is exactly equal to 0.1,
     * as
     * one would expect. Therefore, it is generally recommended that the String constructor be used
     * in preference to this one.</li>
     * <li> When a {@code double} must be used as a source for a {@code BigDecimalEx}, note that
     * this
     * constructor provides an exact conversion; it does not give the same result as converting the
     * {@code double} to a {@code String} using the {@link Double#toString(double)} method and then
     * using the {@link BigDecimalEx(String)} constructor. To get that result, use the static
     * {@link BigDecimal#valueOf(double)} method.</li>
     * </ol>
     *
     * @param val {@code double} value to be converted to {@code BigDecimal}.
     *
     * @thorw NumberFormatException if {@code val} is infinite or NaN.
     */
    public BigDecimalEx( double val ) {
        super( val );
    }

    /**
     * Translates a double into a {@code BigDecimalEx}, with rounding according to the context
     * settings.
     * The scale of the {@code BigDecimalEx} is the smallest value such that
     * (<code>10<sup>scale</sup> × val</code>) is an integer.
     * <p>
     * The results of this constructor can be somewhat unpredictable and its use is generally not
     * recommended; see the notes under the {@link BigDecimalEx(double)} constructor.
     *
     * @param val {@code double} value to be converted to {@code BigDecimalEx}.
     * @param mc  the context to use.
     *
     * @throws ArithmeticException   if the result is inexact but the RoundingMode is
     *                               {@code UNNECESSARY}.
     * @throws NumberFormatException if {@code val} is infinite or NaN.
     */
    public BigDecimalEx( double val,
                         MathContext mc ) {
        super( val, mc );
    }

    /**
     * Translates an {@code int} into a {@code BigDecimalEx}. The scale of the {@code BigDecimal} is
     * zero.
     *
     * @param val {@code int} value to be converted to {@code BigDecimalEx}.
     */
    public BigDecimalEx( int val ) {
        super( val );
    }

    /**
     * Translates an {@code int} into a {@code BigDecimal}, with rounding according to the context
     * settings. The scale of the {@code BigDecimal}, before any rounding, is zero.
     *
     * @param val {@code int} value to be converted to {@code BigDecimalEx}.
     * @param mc  the context to use.
     *
     * @throws ArithmeticException if the result is inexact but the rounding mode is
     *                             {@code UNNECESSARY}.
     */
    public BigDecimalEx( int val,
                         MathContext mc ) {
        super( val, mc );
    }

    /**
     * Translates an {@code long} into a {@code BigDecimalEx}. The scale of the {@code BigDecimal}
     * is zero.
     *
     * @param val {@code long} value to be converted to {@code BigDecimalEx}.
     */
    public BigDecimalEx( long val ) {
        super( val );
    }

    /**
     * Translates an {@code long} into a {@code BigDecimal}, with rounding according to the context
     * settings. The scale of the {@code BigDecimal}, before any rounding, is zero.
     *
     * @param val {@code long} value to be converted to {@code BigDecimalEx}.
     * @param mc  the context to use.
     *
     * @throws ArithmeticException if the result is inexact but the rounding mode is
     *                             {@code UNNECESSARY}.
     */
    public BigDecimalEx( long val,
                         MathContext mc ) {
        super( val, mc );
    }

    /**
     * Translates the string representation of a {@code BigDecimalEx} into a {@code BigDecimalEx}.
     * The string representation consists of an optional sign, '+' ( '\u002B') or '-' ('\u002D'),
     * followed by a sequence of zero or more decimal digits ("the integer"), optionally followed by
     * a fraction, optionally followed by an exponent.
     * <p>
     * The fraction consists of a decimal point followed by zero or more decimal digits. The string
     * must contain at least one digit in either the integer or the fraction. The number formed by
     * the sign, the integer and the fraction is referred to as the significand.
     * <p>
     * The exponent consists of the character 'e' ('\u0065') or 'E' ('\u0045') followed by one or
     * more
     * decimal digits. The value of the exponent must lie between
     * -{@link Integer.MAX_VALUE} ({@link Integer.MIN_VALUE}+1)
     * and {@link Integer.MAX_VALUE}, inclusive.
     * <p>
     * More formally, the strings this constructor accepts are described by the following grammar:
     * <blockquote><pre>
     * <b>BigDecimalString:</b>
     * Signopt Significand Exponentopt
     *
     * <b>Sign:</b>
     * +
     * -
     *
     * <b>Significand:</b>
     * IntegerPart . FractionPartopt
     * . FractionPart
     * IntegerPart
     *
     * <b>IntegerPart:</b>
     * Digits
     *
     * <b>FractionPart:</b>
     * Digits
     *
     * <b>Exponent:</b>
     * ExponentIndicator SignedInteger
     *
     * <b>ExponentIndicator:</b>
     * e
     * E
     *
     * <b>SignedInteger:</b>
     * Signopt Digits
     *
     * <b>Digits:</b>
     * Digit
     * Digits Digit
     *
     * <b>Digit:</b>
     * any character for which {@link Character#isDigit(char)} returns true, including 0, 1, 2 ...
     * </pre></blockquote>
     *
     * The scale of the returned {@code BigDecimalEx} will be the number of digits in the fraction,
     * or zero if the string contains no decimal point, subject to adjustment for any exponent; if
     * the string contains an exponent, the exponent is subtracted from the scale. The value of the
     * resulting scale must lie between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE},
     * inclusive.
     * <p>
     * The character-to-digit mapping is provided by {@link Character#digit(char, int)} set to
     * convert
     * to radix 10. The String may not contain any extraneous characters (whitespace, for example).
     * <p>
     * <b>Examples:</b>
     * <p>
     * The value of the returned {@code BigDecimal} is equal to <i>significand × 10
     * <sup>exponent</sup></i>.
     * For each string on the left, the resulting representation [{@code BigInteger, scale}] is
     * shown on the right.
     * <pre>
     * "0"          [0,0]
     * "0.00"       [0,2]
     * "123"        [123,0]
     * "-123"       [-123,0]
     * "1.23E3"     [123,-1]
     * "1.23E+3"    [123,-1]
     * "12.3E+7"    [123,-6]
     * "12.0"       [120,1]
     * "12.3"       [123,1]
     * "0.00123"    [123,5]
     * "-1.23E-12"  [-123,14]
     * "1234.5E-4"  [12345,5]
     * "0E+7"       [0,-7]
     * "-0"         [0,0]
     * </pre>
     *
     * Note: For values other than {@code float} and {@code double} NaN and ±Infinity, this
     * constructor
     * is compatible with the values returned by {@link Float#toString(float)} and
     * {@link Double#toString(double)}.
     * This is generally the preferred way to convert a {@code float} or {@code double} into a
     * {@code BigDecimalEx},
     * as it doesn't suffer from the unpredictability of the {@code BigDecimal(double)} constructor.
     *
     * @param val String representation of {@code BigDecimalEx}.
     *
     * @throws NumberFormatException if {@code val} is not a valid representation of a {
     * @ocde BigDecimalEx}. */
    public BigDecimalEx( String val ) {
        super( val );
    }

    /**
     * Translates the string representation of a {@code BigDecimalEx} into a {@code BigDecimal},
     * accepting the same strings as the {@code BigDecimal(String)} constructor, with rounding
     * according to the context settings.
     *
     * @param val string representation of a {@code BigDecimalEx}.
     * @param mc  the context to use.
     *
     * @throws ArithmeticException   if the result is inexact but the rounding mode is
     *                               {@code UNNECESSARY}.
     * @throws NumberFormatException if {@code val} is not a valid representation of a
     *                               {@code BigDecimalEx}.
     */
    public BigDecimalEx( String val,
                         MathContext mc ) {
        super( val, mc );
    }

    /**
     * Returns a string representation of this {@code BigDecimal}, using fractional notation.
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
     * @return string representation of this {@code BigDecimal}, using fractional notation.
     */
    public String toFractionString() {
        return MathUtils.toFractionString( this );
    }

    /**
     * Computes the square root of a number.
     *
     * @return a {@code BigDecimal} number containing the square root value of {@code this} or
     *         {@code null} if it cannot be calculated within this method's limits.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Newton's_method">Newton's method</a> on Wikipedia
     */
    public BigDecimalEx squareRoot() {
        return new BigDecimalEx( MathUtils.squareRoot( this ) );
    }

    /**
     * Computes the square root of a number.
     *
     * @param precision Maximum number of fractional digit.
     *
     * @return a {@code BigDecimal} number containing the square root value of {@code this} or
     *         {@code null} if it cannot be calculated within this method's limits.
     *
     * @throws ArithmeticException if {@code this} is negative.
     */
    public BigDecimalEx squareRoot( int precision ) {
        return new BigDecimalEx( MathUtils.squareRoot( this, precision ) );
    }

    /**
     * Computes the square root of a number.
     *
     * @param mc {@code MathContext}
     *
     * @return a {@code BigDecimal} number containing the square root value of {@code this} or
     *         {@code null} if it cannot be calculated within this method's limits.
     *
     * @throws ArithmeticException if {@code this} is negative.
     */
    public BigDecimalEx squareRoot( MathContext mc ) {
        return new BigDecimalEx( MathUtils.squareRoot( this, mc ) );
    }

    /**
     * Calculates the Stirling's Approximation of {@code this} which is very close to the factorial
     * {@code this} value.
     *
     * @return the Striling's Approximation number
     *
     * @throws ArithmeticException if {@code this} value is larger than 999999999.
     *
     * @see <a href ="https://en.wikipedia.org/wiki/Stirling's_Approximation">Stirling's
     * approximation</a> on Wikipedia.
     */
    public BigDecimalEx stirlingApprox() {
        return stirlingApprox( new MathContext( precision(), RoundingMode.HALF_UP ) );

    }

    /**
     * Calculates the Stirling's Approximation of {@code this} which is very close to the factorial
     * {@code this} value.
     *
     * @param precision Maximum number of fractional digit.
     *
     * @return the Striling's Approximation value
     *
     * @throws ArithmeticException if {@code this} value is larger than 999999999.
     *
     * @see <a href ="https://en.wikipedia.org/wiki/Stirling's_Approximation">Stirling's
     * approximation</a> on Wikipedia.
     */
    public BigDecimalEx stirlingApprox( int precision ) {
        return stirlingApprox( new MathContext( precision, RoundingMode.HALF_UP ) );
    }

    /**
     * Calculates the Stirling's Approximation of {@code this} which is very close to the factorial
     * {@code this} value.
     *
     * @param mc {@code MathContext} to use
     *
     * @return the Striling's Approximation value
     *
     * @throws ArithmeticException if {@code this} value is larger than 999999999.
     *
     * @see <a href ="https://en.wikipedia.org/wiki/Stirling's_Approximation">Stirling's
     * approximation</a> on Wikipedia.
     */
    public BigDecimalEx stirlingApprox( MathContext mc ) {
        return new BigDecimalEx( MathUtils.stirlingApprox( new BigDecimal( this.toPlainString() ) ).toPlainString(), mc );
        /*
         * if( compareTo( new BigDecimalEx( Integer.MAX_VALUE ) ) > 0 ) {
         * throw new ArithmeticException( "Number too large" );
         * }
         *
         * BigDecimalEx a = new BigDecimalEx( multiply( TWO.multiply( PI ) ) ).squareRoot();
         * BigDecimalEx b = new BigDecimalEx( divide( e, mc ).pow( intValue(), mc ) );
         *
         * return new BigDecimalEx( a.multiply( b ).stripTrailingZeros() );
         */
    }

}
