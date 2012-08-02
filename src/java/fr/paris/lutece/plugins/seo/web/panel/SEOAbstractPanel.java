/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.web.panel;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;


/**
 * Abstract SEO Panel
 */
public abstract class SEOAbstractPanel implements SEOPanel
{
    private Locale _locale;
    private HttpServletRequest _request;

    /**
     * {@inheritDoc }
     */
    @Override
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Locale getLocale(  )
    {
        return _locale;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setRequest( HttpServletRequest request )
    {
        _request = request;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HttpServletRequest getRequest(  )
    {
        return _request;
    }
    
}
