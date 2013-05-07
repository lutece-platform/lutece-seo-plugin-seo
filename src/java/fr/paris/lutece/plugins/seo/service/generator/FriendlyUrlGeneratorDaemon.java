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
package fr.paris.lutece.plugins.seo.service.generator;

import fr.paris.lutece.plugins.seo.service.RuleFileService;
import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.IOException;

import java.text.DateFormat;

import java.util.Date;


/**
 * Sitemap Deamon
 */
public class FriendlyUrlGeneratorDaemon extends Daemon
{
    /**
     * {@inheritDoc }
     */
    @Override
    public void run(  )
    {
        String strDeamon = DatastoreService.getDataValue( SEODataKeys.KEY_FRIENDLY_URL_GENERATOR_DAEMON_ENABLED,
                DatastoreService.VALUE_FALSE );
        String strLog = "Friendly Url Generator Deamon isn't enabled";

        if ( strDeamon.equals( DatastoreService.VALUE_TRUE ) )
        {
            GeneratorOptions options = new GeneratorOptions(  );

            options.setForceUpdate( false );
            options.setAddPath( DatastoreService.getDataValue( SEODataKeys.KEY_GENERATOR_ADD_PATH, "" )
                                                .equals( DatastoreService.VALUE_TRUE ) );
            options.setHtmlSuffix( DatastoreService.getDataValue( SEODataKeys.KEY_GENERATOR_ADD_HTML_SUFFIX, "" )
                                                   .equals( DatastoreService.VALUE_TRUE ) );

            FriendlyUrlGeneratorService.instance(  ).generate( options );
            strLog = "Friendly Url Generator Deamon last run : " +
                DateFormat.getDateTimeInstance(  ).format( new Date(  ) );

            try
            {
                RuleFileService.generateFile(  );
                strLog += "\nand URL Rewriting rules file updated";
            }
            catch ( IOException ex )
            {
                strLog = "Error writing URL rewriting rules file : " + ex.getMessage(  );
                AppLogService.error( strLog, ex );
            }
        }

        setLastRunLogs( strLog );
    }
}
