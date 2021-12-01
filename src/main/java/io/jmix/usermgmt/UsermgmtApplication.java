package io.jmix.usermgmt;

import com.google.common.base.Strings;
import io.jmix.core.AccessManager;
import io.jmix.core.Metadata;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.graphql.datafetcher.PermissionDataFetcher;
import io.jmix.graphql.schema.PermissionTypesGenerator;
import io.jmix.usermgmt.graphql.datafetcher.ExtendedPermissionDataFetcher;
import io.jmix.usermgmt.graphql.schema.ExtendedPermissionTypesGenerator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@SpringBootApplication
public class UsermgmtApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(UsermgmtApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean("gql_PermissionDataFetcher")
    @Primary
    PermissionDataFetcher permissionDataFetcher(Metadata metadata,
                                                AccessManager accessManager,
                                                CurrentAuthentication currentAuthentication) {
        return new ExtendedPermissionDataFetcher(metadata, accessManager, currentAuthentication);
    }

    @Bean("gql_PermissionTypesGenerator")
    @Primary
    PermissionTypesGenerator permissionTypesGenerator() {
        return new ExtendedPermissionTypesGenerator();
    }

    @EventListener
    public void printApplicationUrl(ApplicationStartedEvent event) {
        LoggerFactory.getLogger(UsermgmtApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }
}
