--
-- Dumping data for table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('SEO_MANAGEMENT','seo.adminFeature.seo_management.name',1,'jsp/admin/plugins/seo/ManageSEO.jsp','seo.adminFeature.seo_management.name',0,'seo',NULL,NULL,NULL,4);


--
-- Dumping data for table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('SEO_MANAGEMENT',1);


--
-- Dumping data for table core_datastore
--
INSERT INTO core_datastore VALUES 
('seo.rewrite.config.lastUpdate',''),
('seo.config.uptodate','false'),
('seo.generator.option.addPath','false'),
('seo.generator.option.addHtmlSuffix','true'),
('seo.replaceUrl.enabled','true'),
('seo.generator.daemon.enabled','true'),
('seo.canonicalUrls.enabled','true'),
('seo.sitmap.daemon.enabled','true'),
('seo.sitemap.update.log','');
