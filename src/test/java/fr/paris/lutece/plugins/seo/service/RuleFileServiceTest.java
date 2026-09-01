/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RuleFileService Test
 */
public class RuleFileServiceTest extends LuteceTestCase
{
    /**
     * The rules are handed to the rewrite filter as a configuration document : it has to start with the XML declaration,
     * with nothing before it, and to hold an urlrewrite element.
     */
    @Test
    public void testGetRulesXml( )
    {
        String strRules = RuleFileService.getRulesXml( );

        assertTrue( strRules.startsWith( "<?xml" ), "the rules must start with the XML declaration : " + strRules.substring( 0, Math.min( 40, strRules.length( ) ) ) );
        assertTrue( strRules.contains( "<urlrewrite>" ), "the rules must hold an urlrewrite element" );
        assertTrue( strRules.contains( "</urlrewrite>" ), "the urlrewrite element must be closed" );
    }

    /**
     * Rendering the rules must be free of side effect, the filter calling it on every reload check.
     */
    @Test
    public void testGetRulesXmlLeavesTheVersionUntouched( )
    {
        String strVersion = DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" );

        RuleFileService.getRulesXml( );

        assertEquals( strVersion, DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" ) );
    }

    /**
     * Publishing stamps a version, which every node compares to the one it has loaded to decide whether to reload.
     */
    @Test
    public void testPublishRules( )
    {
        RuleFileService.publishRules( );

        assertTrue( DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" ).matches( "\\d+" ), "the version must be a timestamp" );
        assertEquals( DatastoreService.VALUE_TRUE, DatastoreService.getDataValue( SEODataKeys.KEY_CONFIG_UPTODATE, "" ) );
    }
}
