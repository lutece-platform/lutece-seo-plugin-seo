/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.seo.web.panel;

import java.util.Locale;


/**
 * Abstract SEO Panel
 */
public abstract class SEOAbstractPanel implements SEOPanel
{
    private Locale _locale;

    /**
     * {@inheritDoc }
     */
    @Override
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Gets the locale
     * @return The locale
     */
    public Locale getLocale(  )
    {
        return _locale;
    }
}
