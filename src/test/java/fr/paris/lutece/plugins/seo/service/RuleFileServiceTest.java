/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.business.FriendlyUrlHome;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRule;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRuleHome;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.*;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

        assertTrue( "the rules must start with the XML declaration : " + strRules.substring( 0, Math.min( 40, strRules.length( ) ) ), strRules.startsWith( "<?xml" ) );
        assertTrue( "the rules must hold an urlrewrite element", strRules.contains( "<urlrewrite>" ) );
        assertTrue( "the urlrewrite element must be closed", strRules.contains( "</urlrewrite>" ) );
    }

    /**
     * Every rule and every friendly URL held in the database must come out as a rule element the rewrite filter accepts,
     * the sitemap rule first, with the characters XML reserves escaped so that a rule is read back as it was entered.
     */
    @Test
    public void testGetRulesXmlHoldsTheRules( ) throws Exception
    {
        UrlRewriterRule rule = new UrlRewriterRule( );
        rule.setRuleFrom( "/old-path/([0-9]+)" );
        rule.setRuleTo( "/jsp/site/Portal.jsp?page=test&id=$1" );
        rule = UrlRewriterRuleHome.create( rule );

        FriendlyUrl url = new FriendlyUrl( );
        url.setFriendlyUrl( "/friendly/<page>" );
        url.setTechnicalUrl( "/jsp/site/Portal.jsp?page_id=1&other='x'" );
        url = FriendlyUrlHome.create( url );

        try
        {
            String strRules = RuleFileService.getRulesXml( );

            assertTrue( "the sitemap rule must be present", strRules.contains( "<from>^/sitemap-seo\\.xml$</from>" ) );
            assertTrue( "the rewriter rule must be anchored at the start of the path", strRules.contains( "<from>^/old-path/([0-9]+)</from>" ) );
            assertTrue( "the target of the rewriter rule must be XML escaped", strRules.contains( "<to>/jsp/site/Portal.jsp?page=test&amp;id=$1</to>" ) );
            assertTrue( "the friendly URL must be an exact match, XML escaped", strRules.contains( "<from>^/friendly/&lt;page&gt;$</from>" ) );
            assertTrue( "the technical URL must be XML escaped", strRules.contains( "<to>/jsp/site/Portal.jsp?page_id=1&amp;other=&apos;x&apos;</to>" ) );
            assertTrue( "the sitemap rule must come first", strRules.indexOf( "sitemap-seo" ) < strRules.indexOf( "/old-path/" ) );

            int nExpectedRules = 1 + UrlRewriterRuleHome.findAll( ).size( ) + FriendlyUrlHome.findAll( ).size( );

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance( );
            factory.setValidating( false );
            factory.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
            Document document = factory.newDocumentBuilder( ).parse( new ByteArrayInputStream( strRules.getBytes( StandardCharsets.UTF_8 ) ) );
            assertEquals( "one rule element per rule, plus the sitemap rule", nExpectedRules, document.getElementsByTagName( "rule" ).getLength( ) );

            NodeList listTo = document.getElementsByTagName( "to" );
            boolean bTargetReadBack = false;
            for ( int i = 0; i < listTo.getLength( ); i++ )
            {
                bTargetReadBack |= "/jsp/site/Portal.jsp?page=test&id=$1".equals( listTo.item( i ).getTextContent( ) );
            }
            assertTrue( "the target must be read back as it was entered", bTargetReadBack );

            try ( InputStream isRules = new ByteArrayInputStream( strRules.getBytes( StandardCharsets.UTF_8 ) ) )
            {
                Conf conf = new Conf( isRules, "test" );
                assertTrue( "the rewrite filter must accept the rules", conf.isOk( ) );
                assertEquals( "the rewrite filter must load every rule", nExpectedRules, conf.getRules( ).size( ) );
            }
        }
        finally
        {
            UrlRewriterRuleHome.remove( rule.getIdRule( ) );
            FriendlyUrlHome.remove( url.getId( ) );
        }
    }

    /**
     * A rewrite filter rejects an unbalanced regular expression : the rules must then not be published.
     */
    @Test
    public void testPublishRulesRejectsAnInvalidRule( )
    {
        String strVersion = DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" );

        UrlRewriterRule rule = new UrlRewriterRule( );
        rule.setRuleFrom( "/broken/([0-9]+" );
        rule.setRuleTo( "/jsp/site/Portal.jsp" );
        rule = UrlRewriterRuleHome.create( rule );

        try
        {
            assertFalse( "an invalid regular expression must not be published", RuleFileService.publishRules( ) );
            assertEquals( "the version must be left untouched", strVersion, DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" ) );
        }
        finally
        {
            UrlRewriterRuleHome.remove( rule.getIdRule( ) );
        }
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

        assertTrue( "the version must be a timestamp", DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" ).matches( "\\d+" ) );
        assertEquals( DatastoreService.VALUE_TRUE, DatastoreService.getDataValue( SEODataKeys.KEY_CONFIG_UPTODATE, "" ) );
    }
}
