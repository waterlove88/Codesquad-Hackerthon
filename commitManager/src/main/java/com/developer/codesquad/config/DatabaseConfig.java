package com.developer.codesquad.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = DatabaseConfig.BASE_PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DatabaseConfig {

    public static final String BASE_PACKAGE = "com.developer.codesquad.mapper";
    public static final String TYPE_ALIASES_PACKAGE = "com.developer.codesquad.domain";
    public static final String CONFIG_LOCATION_PATH = "classpath:/config/mybatis-config.xml";
    public static final String MAPPER_LOCATIONS_PATH = "classpath:/mappers/*.xml";

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource datasource) throws Exception {
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(datasource);
        sqlSessionFactory.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        sqlSessionFactory.setConfigLocation(pathResolver.getResource(CONFIG_LOCATION_PATH));
        sqlSessionFactory.setMapperLocations(pathResolver.getResources(MAPPER_LOCATIONS_PATH));
		return sqlSessionFactory.getObject();
	}    

	@Bean
	public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
