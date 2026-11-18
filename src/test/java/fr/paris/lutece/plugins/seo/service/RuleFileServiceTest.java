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

import org.junit.jupiter.api.Test;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.NormalRule;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

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
     * The template is rendered with the HTML output format the core gives to a .xml template: a rule holding XML
     * special characters is escaped, and the filter reads it back unchanged.
     */
    @Test
    public void testGetRulesXmlEscapesTheRules( )
    {
        String strTo = "/jsp/site/Portal.jsp?page=seo&id=1&label=\"a\"<b>";
        UrlRewriterRule rule = new UrlRewriterRule( );
        rule.setRuleFrom( "/seo-escaping-test/(\\d+)" );
        rule.setRuleTo( strTo );
        rule = UrlRewriterRuleHome.create( rule );

        try
        {
            String strRules = RuleFileService.getRulesXml( );

            assertTrue( strRules.contains( "<to>/jsp/site/Portal.jsp?page=seo&amp;id=1&amp;label=&quot;a&quot;&lt;b&gt;</to>" ), strRules );

            Conf conf = new Conf( new ByteArrayInputStream( strRules.getBytes( StandardCharsets.UTF_8 ) ), "test" );
            assertTrue( conf.isOk( ), "the filter must accept the rules" );
            assertTrue( conf.getRules( ).stream( ).anyMatch( r -> r instanceof NormalRule && strTo.equals( ( (NormalRule) r ).getTo( ) ) ),
                    "the filter must read the rule unchanged" );
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

        assertTrue( DatastoreService.getDataValue( SEODataKeys.KEY_RULES_VERSION, "" ).matches( "\\d+" ), "the version must be a timestamp" );
        assertEquals( DatastoreService.VALUE_TRUE, DatastoreService.getDataValue( SEODataKeys.KEY_CONFIG_UPTODATE, "" ) );
    }

    /**
     * A friendly URL matches a whole URL, a rule the start of the URL: the friendly URL must win (the rule /map must not take /mapping.html)
     */
    @Test
    public void testFriendlyUrlBeforeRuleMatchingItsStart( )
    {
        String strTechnicalUrl = "/jsp/site/Portal.jsp?page_id=987";
        UrlRewriterRule rule = new UrlRewriterRule( );
        rule.setRuleFrom( "/seo-map" );
        rule.setRuleTo( "/jsp/site/Portal.jsp?page=seo-map" );
        rule = UrlRewriterRuleHome.create( rule );
        FriendlyUrl friendlyUrl = new FriendlyUrl( );
        friendlyUrl.setFriendlyUrl( "/seo-mapping-test.html" );
        friendlyUrl.setTechnicalUrl( strTechnicalUrl );
        friendlyUrl = FriendlyUrlHome.create( friendlyUrl );

        try
        {
            String strRules = RuleFileService.getRulesXml( );
            Conf conf = new Conf( new ByteArrayInputStream( strRules.getBytes( StandardCharsets.UTF_8 ) ), "test" );
            assertTrue( conf.isOk( ), "the filter must accept the rules" );

            NormalRule firstMatchingRule = null;
            for ( Object objectRule : conf.getRules( ) )
            {
                if ( objectRule instanceof NormalRule normalRule && Pattern.compile( normalRule.getFrom( ) ).matcher( "/seo-mapping-test.html" ).find( ) )
                {
                    firstMatchingRule = normalRule;
                    break;
                }
            }
            assertNotNull( firstMatchingRule );
            assertEquals( strTechnicalUrl, firstMatchingRule.getTo( ) );
        }
        finally
        {
            UrlRewriterRuleHome.remove( rule.getIdRule( ) );
            FriendlyUrlHome.remove( friendlyUrl.getId( ) );
        }
    }
}
