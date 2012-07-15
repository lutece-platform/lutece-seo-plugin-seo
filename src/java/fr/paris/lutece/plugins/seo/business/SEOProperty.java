/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
 * This is the business class for the object SEOProperty
 */
public class SEOProperty
{
    // Variables declarations 
    private String _strPropertyKey;
    private String _strPropertyValue;

    /**
     * Constructor
     */
    public SEOProperty()
    {
        
    }
    
    /**
     * Constructor
     * @param strKey The key
     * @param strValue The value
     */
    public SEOProperty( String strKey , String strValue )
    {
        _strPropertyKey = strKey;
        _strPropertyValue = strValue;
    }
    
    /**
     * Returns the PropertyKey
     * @return The PropertyKey
     */
    public String getPropertyKey(  )
    {
        return _strPropertyKey;
    }

    /**
     * Sets the PropertyKey
     * @param strPropertyKey The PropertyKey
     */
    public void setPropertyKey( String strPropertyKey )
    {
        _strPropertyKey = strPropertyKey;
    }

    /**
     * Returns the PropertyValue
     * @return The PropertyValue
     */
    public String getPropertyValue(  )
    {
        return _strPropertyValue;
    }

    /**
     * Sets the PropertyValue
     * @param strPropertyValue The PropertyValue
     */
    public void setPropertyValue( String strPropertyValue )
    {
        _strPropertyValue = strPropertyValue;
    }
}
