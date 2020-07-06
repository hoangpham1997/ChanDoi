package vn.softdreams.ebweb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "reportEntityManagerFactory", basePackages = { "vn.softdreams.ebweb.repository.report" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseReportConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseReportConfiguration.class);

    @Inject
    private Environment env;

    @Bean(destroyMethod = "close", name = "dataSourceReport")
    @ConditionalOnExpression("#{!environment.acceptsProfiles('cloud') && !environment.acceptsProfiles('heroku')}")
    public DataSource dataSourceReport(DataSourceProperties dataSourceProperties, JHipsterProperties jHipsterProperties) {
        log.debug("Configuring Datasource Report");
        if (dataSourceProperties.getUrl() == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(env.getProperty("spring.datasourceReport.driverClassName", String.class));
        if (env.getProperty("spring.datasourceReport.url", String.class) != null) {
            config.addDataSourceProperty("url", env.getProperty("spring.datasourceReport.url", String.class));
        } else {
            config.addDataSourceProperty("url", "jdbc:sqlserver://172.16.10.72:1433;database=EASYBOOKS_NEW");
        }
        if (env.getProperty("spring.datasourceReport.username", String.class) != null) {
            config.addDataSourceProperty("user", env.getProperty("spring.datasourceReport.username", String.class));
        } else {
            config.addDataSourceProperty("user", ""); // HikariCP doesn't allow null user
        }
        if (env.getProperty("spring.datasourceReport.password", String.class) != null) {
            config.addDataSourceProperty("password", env.getProperty("spring.datasourceReport.password", String.class));
        } else {
            config.addDataSourceProperty("password", ""); // HikariCP doesn't allow null password
        }
        config.setJdbcUrl(env.getProperty("spring.datasourceReport.url", String.class));
        config.setAutoCommit(env.getProperty("spring.datasourceReport.hikari.auto-commit", Boolean.class));
        config.setMaximumPoolSize(env.getProperty("spring.datasourceReport.hikari.maximumPoolSize", Integer.class));
        config.setMaxLifetime(env.getProperty("spring.datasourceReport.hikari.maxLifetime", Long.class));
        config.setConnectionTimeout(env.getProperty("spring.datasourceReport.hikari.connectionTimeout", Long.class));
        config.setMinimumIdle(env.getProperty("spring.datasourceReport.hikari.minimumIdle", Integer.class));
        config.setIdleTimeout(env.getProperty("spring.datasourceReport.hikari.idleTimeout", Long.class));
        config.setAllowPoolSuspension(false);
        config.setPoolName(env.getProperty("spring.datasourceReport.poolName"));
        config.setLeakDetectionThreshold(0);

        return new HikariDataSource(config);
    }

    @Bean(name = "reportJdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSourceProperties dataSourceProperties, JHipsterProperties jHipsterProperties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceReport(dataSourceProperties, jHipsterProperties));
        return jdbcTemplate;
    }


    @Bean(name = "reportNamedJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate (DataSourceProperties dataSourceProperties, JHipsterProperties jHipsterProperties) {
        NamedParameterJdbcTemplate  jdbcTemplate = new NamedParameterJdbcTemplate (dataSourceReport(dataSourceProperties, jHipsterProperties));
        return jdbcTemplate;
    }


    @Bean(name = "reportEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSourceReport") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("vn.softdreams.ebweb.domain").persistenceUnit("reportPersistenUnit").build();
    }


    @Bean(name = "reportTransactionManager")
    public PlatformTransactionManager transactionManager(
        @Qualifier("reportEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
