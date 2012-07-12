--
-- Dumping data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'SEO_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('SEO_MANAGEMENT','seo.adminFeature.seo_management.name',1,'jsp/admin/plugins/seo/ManageSEO.jsp','seo.adminFeature.seo_management.name',0,'seo',NULL,NULL,NULL,4);


--
-- Dumping data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'SEO_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('SEO_MANAGEMENT',1);
