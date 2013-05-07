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
package fr.paris.lutece.plugins.seo.service;


/**
 * SEO Data keys used to store datas using DatastoreService
 */
public final class SEODataKeys
{
    public static final String KEY_SITEMAP_UPDATE_LOG = "seo.sitemap.update.log";
    public static final String KEY_REWRITE_CONFIG_UPDATE = "seo.rewrite.config.lastUpdate";
    public static final String KEY_CONFIG_UPTODATE = "seo.config.uptodate";
    public static final String KEY_SITEMAP_DEAMON_ENABLED = "seo.sitmap.daemon.enabled";
    public static final String KEY_FRIENDLY_URL_GENERATOR_DAEMON_ENABLED = "seo.generator.daemon.enabled";
    public static final String KEY_URL_REPLACE_ENABLED = "seo.replaceUrl.enabled";
    public static final String KEY_CANONICAL_URLS_ENABLED = "seo.canonicalUrls.enabled";
    public static final String KEY_GENERATOR_ADD_PATH = "seo.generator.option.addPath";
    public static final String KEY_GENERATOR_ADD_HTML_SUFFIX = "seo.generator.option.addHtmlSuffix";
    public static final String PREFIX_GENERATOR = "seo.generator.";
    public static final String SUFFIX_CANONICAL = ".canonical";
    public static final String SUFFIX_SITEMAP = ".sitemap";
    public static final String SUFFIX_CHANGE_FREQ = ".changeFreq";
    public static final String SUFFIX_LAST_MOD = ".lastMod";
    public static final String SUFFIX_PRIORITY = ".priority";

    /**
     * Private constructor
     */
    private SEODataKeys(  )
    {
    }
}
