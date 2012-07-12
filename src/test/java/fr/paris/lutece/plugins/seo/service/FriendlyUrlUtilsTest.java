/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.service;

import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author pierre
 */
public class FriendlyUrlUtilsTest
{
    private static final String SOURCE = "<html> <body> <a href=\"toto\" >toto</a> text <a href=\"titi\" >titi</a> </body></html>";

    /**
     * Test of convertToAlias method, of class GeneratorUtils.
     */
    @Test
    public void testConvertToAlias()
    {
        System.out.println("convertToAlias");
        String strSource = "";
        String expResult = "";
        String result = FriendlyUrlUtils.convertToFriendlyUrl(strSource);
    }

    /**
     * Test of replaceLink method, of class GeneratorUtils.
     */
    @Test
    public void testReplaceLink()
    {
        System.out.println("replaceLink");
        String strSource = SOURCE;
        Map<String, String> map = new HashMap<String,String>();
        map.put("toto", "replaced" );
        String result = FriendlyUrlUtils.replaceByFriendlyUrl(strSource, map);
        System.out.println(result);
    }
}
