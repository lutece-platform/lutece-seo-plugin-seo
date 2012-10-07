

INSERT INTO seo_friendly_url ( id_url, friendly_url, technical_url, is_canonical, is_sitemap, sitemap_lastmod, sitemap_changefreq, sitemap_priority ) VALUES
( 1 , '/sitemap.html' , '/jsp/site/Portal.jsp?page=map' , 1 , 1, '2012-10-10' , 'monthly' , '0.8' ),
( 2 , '/contacts.html' , '/jsp/site/Portal.jsp?page=contact' , 1 , 1, '2012-10-10' , 'monthly' , '0.8' ),
( 3 , '/credits.html' , '/jsp/site/PopupCredits.jsp' , 1 , 1, '2012-10-10' , 'monthly' , '0.8' );

INSERT INTO seo_rule ( id_rule , rule_from , rule_to ) VALUES
( 1 , '/page/([0-9]+)', '/jsp/site/Portal.jsp?page_id=$1' );
INSERT INTO seo_rule ( id_rule , rule_from , rule_to ) VALUES
( 2 , '/app/([a-z]+)',  '/jsp/site/Portal.jsp?page=$1' );
INSERT INTO seo_rule ( id_rule , rule_from , rule_to ) VALUES
( 3 , '/map', '/jsp/site/Portal.jsp?page=map');
       
