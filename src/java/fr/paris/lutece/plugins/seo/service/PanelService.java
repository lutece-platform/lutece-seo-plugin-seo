/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.seo.service;

import fr.paris.lutece.plugins.seo.web.SEOPanel;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Panel Service
 */
@ApplicationScoped
public class PanelService
{
    @Inject
    private Instance<SEOPanel> _panelInstances;

    private List<SEOPanel> _listPanels;

    @PostConstruct
    public void init( )
    {
        _listPanels = new ArrayList<>( );
        _panelInstances.forEach( _listPanels::add );
        _listPanels.sort( Comparator.comparingInt( SEOPanel::getPanelOrder ) );
    }

    /**
     * Returns the list of all panels
     *
     * @return The list of all panels
     */
    public List<SEOPanel> getPanels( )
    {
        return _listPanels;
    }

    /**
     * Get the index of a panel
     *
     * @param strPanelKey
     *            The panel's key
     * @return The index
     */
    public int getIndex( String strPanelKey )
    {
        int nIndex = 1;

        for ( SEOPanel panel : _listPanels )
        {
            if ( panel.getPanelKey( ).equals( strPanelKey ) )
            {
                return nIndex;
            }

            nIndex++;
        }

        return -1;
    }
}
