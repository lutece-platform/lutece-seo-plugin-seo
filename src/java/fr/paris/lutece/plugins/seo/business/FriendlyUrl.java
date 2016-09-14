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

import java.sql.Timestamp;

/**
 * This is the business class for the object FriendlyUrl
 */
public class FriendlyUrl
{
    // Variables declarations
    private int _nIdUrl;
    private String _strFriendlyUrl;
    private String _strTechnicalUrl;
    private Timestamp _dateCreation;
    private Timestamp _dateModification;
    private boolean _bCanonical;
    private boolean _bSitemap;
    private String _strSitemapLastmod;
    private String _strSitemapChangeFreq;
    private String _strSitemapPriority;

    /**
     * Returns the IdUrl
     *
     * @return The IdUrl
     */
    public int getId( )
    {
        return _nIdUrl;
    }

    /**
     * Sets the IdUrl
     *
     * @param nIdUrl
     *            The IdUrl
     */
    public void setId( int nIdUrl )
    {
        _nIdUrl = nIdUrl;
    }

    /**
     * Returns the FriendlyUrl
     *
     * @return The FriendlyUrl
     */
    public String getFriendlyUrl( )
    {
        return _strFriendlyUrl;
    }

    /**
     * Sets the FriendlyUrl
     *
     * @param strFriendlyUrl
     *            The FriendlyUrl
     */
    public void setFriendlyUrl( String strFriendlyUrl )
    {
        _strFriendlyUrl = strFriendlyUrl;
    }

    /**
     * Returns the TechnicalUrl
     *
     * @return The TechnicalUrl
     */
    public String getTechnicalUrl( )
    {
        return _strTechnicalUrl;
    }

    /**
     * Sets the TechnicalUrl
     *
     * @param strTechnicalUrl
     *            The TechnicalUrl
     */
    public void setTechnicalUrl( String strTechnicalUrl )
    {
        _strTechnicalUrl = strTechnicalUrl;
    }

    /**
     * Returns the DateCreation
     *
     * @return The DateCreation
     */
    public Timestamp getDateCreation( )
    {
        return _dateCreation;
    }

    /**
     * Sets the DateCreation
     *
     * @param dateCreation
     *            The DateCreation
     */
    public void setDateCreation( Timestamp dateCreation )
    {
        _dateCreation = dateCreation;
    }

    /**
     * Returns the DateModification
     *
     * @return The DateModification
     */
    public Timestamp getDateModification( )
    {
        return _dateModification;
    }

    /**
     * Sets the DateModification
     *
     * @param dateModification
     *            The DateModification
     */
    public void setDateModification( Timestamp dateModification )
    {
        _dateModification = dateModification;
    }

    /**
     * Returns the IsCanonical
     *
     * @return The IsCanonical
     */
    public boolean isCanonical( )
    {
        return _bCanonical;
    }

    /**
     * Sets the IsCanonical
     *
     * @param bCanonical
     *            The IsCanonical
     */
    public void setCanonical( boolean bCanonical )
    {
        _bCanonical = bCanonical;
    }

    /**
     * Returns the IsSitemap
     *
     * @return The IsSitemap
     */
    public boolean isSitemap( )
    {
        return _bSitemap;
    }

    /**
     * Sets the IsSitemap
     *
     * @param bSitemap
     *            The IsSitemap
     */
    public void setSitemap( boolean bSitemap )
    {
        _bSitemap = bSitemap;
    }

    /**
     * Returns the SitemapLastmod
     *
     * @return The SitemapLastmod
     */
    public String getSitemapLastmod( )
    {
        return _strSitemapLastmod;
    }

    /**
     * Sets the SitemapLastmod
     *
     * @param strSitemapLastmod
     *            The SitemapLastmod
     */
    public void setSitemapLastmod( String strSitemapLastmod )
    {
        _strSitemapLastmod = strSitemapLastmod;
    }

    /**
     * Returns the SitemapChangeFreq
     *
     * @return The SitemapChangeFreq
     */
    public String getSitemapChangeFreq( )
    {
        return _strSitemapChangeFreq;
    }

    /**
     * Sets the SitemapChangeFreq
     *
     * @param strSitemapChangeFreq
     *            The SitemapChangeFreq
     */
    public void setSitemapChangeFreq( String strSitemapChangeFreq )
    {
        _strSitemapChangeFreq = strSitemapChangeFreq;
    }

    /**
     * Returns the SitemapPriority
     *
     * @return The SitemapPriority
     */
    public String getSitemapPriority( )
    {
        return _strSitemapPriority;
    }

    /**
     * Sets the SitemapPriority
     *
     * @param strSitemapPriority
     *            The SitemapPriority
     */
    public void setSitemapPriority( String strSitemapPriority )
    {
        _strSitemapPriority = strSitemapPriority;
    }
}
