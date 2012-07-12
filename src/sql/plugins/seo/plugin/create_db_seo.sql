--
-- Structure for table seo_rule
--

DROP TABLE IF EXISTS seo_rule;
CREATE TABLE seo_rule (		
  id_rule INT DEFAULT '0' NOT NULL,
  rule_from VARCHAR(255) DEFAULT '' NOT NULL,
  rule_to VARCHAR(255) DEFAULT '' NOT NULL,
  PRIMARY KEY (id_rule)  
 
);

DROP TABLE IF EXISTS seo_friendly_url;
CREATE TABLE seo_friendly_url (		
  id_url INT DEFAULT '0' NOT NULL,
  friendly_url VARCHAR(255) DEFAULT '' NOT NULL,
  technical_url VARCHAR(255) DEFAULT '' NOT NULL,
  date_creation timestamp default CURRENT_TIMESTAMP NOT NULL,
  date_modification timestamp default '0000-00-00 00:00:00' NOT NULL,
  is_canonical int default 0 NOT NULL,
  is_sitemap int default 0 NOT NULL,
  sitemap_lastmod VARCHAR(255) DEFAULT '' NULL,
  sitemap_changefreq VARCHAR(255) DEFAULT '' NULL,
  sitemap_priority VARCHAR(255) DEFAULT '' NULL,
  PRIMARY KEY (id_url)  

);