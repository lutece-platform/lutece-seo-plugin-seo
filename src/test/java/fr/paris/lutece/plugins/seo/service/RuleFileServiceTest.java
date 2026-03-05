/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.plugins.seo.service.RuleFileService;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.jupiter.api.Test;

/**
 * RuleFileService Test
 */
public class RuleFileServiceTest extends LuteceTestCase
{
    /**
     * Test of generateFile method, of class RuleFileService.
     */
    @Test
    public void testGenerateFile( ) throws Exception
    {
        System.out.println( "generateFile" );
        RuleFileService.generateFile( );
    }
}
