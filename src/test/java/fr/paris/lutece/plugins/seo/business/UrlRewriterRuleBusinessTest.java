/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.seo.business;

import fr.paris.lutece.plugins.seo.business.UrlRewriterRule;
import fr.paris.lutece.plugins.seo.business.UrlRewriterRuleHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;


public class UrlRewriterRuleBusinessTest extends LuteceTestCase
{
    private final static int IDRULE1 = 1;
    private final static int IDRULE2 = 2;
    private final static String RULEFROM1 = "RuleFrom1";
    private final static String RULEFROM2 = "RuleFrom2";
    private final static String RULETO1 = "RuleTo1";
    private final static String RULETO2 = "RuleTo2";

    public void testBusiness(  )
    {
        Plugin plugin = PluginService.getPlugin( "seo" );

        // Initialize an object
        UrlRewriterRule urlRewriterRule = new UrlRewriterRule(  );
        urlRewriterRule.setIdRule( IDRULE1 );
        urlRewriterRule.setRuleFrom( RULEFROM1 );
        urlRewriterRule.setRuleTo( RULETO1 );

        // Create test
        UrlRewriterRuleHome.create( urlRewriterRule );

        UrlRewriterRule urlRewriterRuleStored = UrlRewriterRuleHome.findByPrimaryKey( urlRewriterRule.getIdRule(  ) );
        assertEquals( urlRewriterRuleStored.getIdRule(  ), urlRewriterRule.getIdRule(  ) );
        assertEquals( urlRewriterRuleStored.getRuleFrom(  ), urlRewriterRule.getRuleFrom(  ) );
        assertEquals( urlRewriterRuleStored.getRuleTo(  ), urlRewriterRule.getRuleTo(  ) );

        // Update test
        urlRewriterRule.setIdRule( IDRULE2 );
        urlRewriterRule.setRuleFrom( RULEFROM2 );
        urlRewriterRule.setRuleTo( RULETO2 );
        UrlRewriterRuleHome.update( urlRewriterRule );
        urlRewriterRuleStored = UrlRewriterRuleHome.findByPrimaryKey( urlRewriterRule.getIdRule(  ) );
        assertEquals( urlRewriterRuleStored.getIdRule(  ), urlRewriterRule.getIdRule(  ) );
        assertEquals( urlRewriterRuleStored.getRuleFrom(  ), urlRewriterRule.getRuleFrom(  ) );
        assertEquals( urlRewriterRuleStored.getRuleTo(  ), urlRewriterRule.getRuleTo(  ) );

        // List test
        UrlRewriterRuleHome.findAll(  );

        // Delete test
        UrlRewriterRuleHome.remove( urlRewriterRule.getIdRule(  ) );
        urlRewriterRuleStored = UrlRewriterRuleHome.findByPrimaryKey( urlRewriterRule.getIdRule(  ) );
        assertNull( urlRewriterRuleStored );
    }
}
