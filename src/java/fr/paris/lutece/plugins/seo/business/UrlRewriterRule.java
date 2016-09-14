/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

/**
 * This is the business class for the object UrlRewriterRule
 */
public class UrlRewriterRule
{
    // Variables declarations
    private int _nIdRule;
    private String _strRuleFrom;
    private String _strRuleTo;

    /**
     * Returns the IdRule
     * 
     * @return The IdRule
     */
    public int getIdRule( )
    {
        return _nIdRule;
    }

    /**
     * Sets the IdRule
     * 
     * @param nIdRule
     *            The IdRule
     */
    public void setIdRule( int nIdRule )
    {
        _nIdRule = nIdRule;
    }

    /**
     * Returns the RuleFrom
     * 
     * @return The RuleFrom
     */
    public String getRuleFrom( )
    {
        return _strRuleFrom;
    }

    /**
     * Sets the RuleFrom
     * 
     * @param strRuleFrom
     *            The RuleFrom
     */
    public void setRuleFrom( String strRuleFrom )
    {
        _strRuleFrom = strRuleFrom;
    }

    /**
     * Returns the RuleTo
     * 
     * @return The RuleTo
     */
    public String getRuleTo( )
    {
        return _strRuleTo;
    }

    /**
     * Sets the RuleTo
     * 
     * @param strRuleTo
     *            The RuleTo
     */
    public void setRuleTo( String strRuleTo )
    {
        _strRuleTo = strRuleTo;
    }
}
