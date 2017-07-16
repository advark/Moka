/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moka.util.checksum;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ypoirier
 */
public class CRC64Test {

    static private final Object[][] TEST_CASES = { { "123456789", new Long( 0x995dc9bbdf1939faL ) },
                                                   { "This is a test of the emergency broadcast system.",
                                                     new Long( 0x27db187fc15bbc72L ) },
                                                   { "IHATEMATH", new Long( 0x3920e0f66b6ee0c8L ) } };

    public CRC64Test() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of CRC64 method, of class CRC64.
     */
    @Test
    public void testCRC64() {
        System.out.println( "CRC64" );
        CRC64 instance = new CRC64();

        for( Object[] test : TEST_CASES ) {
            instance.reset();

            if( test[0] instanceof String ) {
                String src = (String) test[0];
                for( byte b : src.getBytes() ) {
                    instance.update( b );
                }
                assertEquals( "CRC does not match (" + src + ")", test[1], instance.getValue() );
            }
        }
    }

}
