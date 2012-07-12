

INSERT INTO seo_rule ( id_rule , rule_from , rule_to ) VALUES
( 1 , '/page/([0-9]+)', '/jsp/site/Portal.jsp?page_id=$1' );
INSERT INTO seo_rule ( id_rule , rule_from , rule_to ) VALUES
( 2 , '/app/([a-z]+)',  '/jsp/site/Portal.jsp?page=$1' );
INSERT INTO seo_rule ( id_rule , rule_from , rule_to ) VALUES
( 3 , '/map', '/jsp/site/Portal.jsp?page=map');
       
