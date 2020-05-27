package com.fimet.commons;

import com.fimet.utils.converter.Converter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ConverteTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConverteTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ConverteTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testConverter()
    {
    	byte[] txtAscii = "Hola".getBytes();
    	
    	byte[] txtHex = Converter.asciiToHex(txtAscii);
    	assertEquals("486F6C61", new String(txtHex));
    	
    	byte[] txtBinary = Converter.asciiToBinary(txtAscii);
    	assertEquals("01001000011011110110110001100001", new String(txtBinary));
    }
}
