package br.com.rollo.rafael.tuitrapi.infrastructure.configuration;

import br.com.rollo.rafael.tuitrapi.infrastructure.security.MySqlJdbcMutableAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityACLConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclCache aclCache(PermissionGrantingStrategy permissionGrantingStrategy,
                             AclAuthorizationStrategy aclAuthorizationStrategy) {
        var aclCache = cacheManager.getCache("aclCache");

        return new SpringCacheBasedAclCache(
                aclCache,
                permissionGrantingStrategy,
                aclAuthorizationStrategy);
    }

    @Bean
    public LookupStrategy lookupStrategy(AclCache aclCache,
                                         AclAuthorizationStrategy aclAuthorizationStrategy) {
        return new BasicLookupStrategy(dataSource, aclCache,
                aclAuthorizationStrategy, new ConsoleAuditLogger());
    }

    @Bean
    public MutableAclService aclService(LookupStrategy lookupStrategy, AclCache aclCache) {
        return new MySqlJdbcMutableAclService(dataSource, lookupStrategy, aclCache);
    }

    @Bean
    public AclPermissionEvaluator permissionEvaluator(AclService aclService) {
        return new AclPermissionEvaluator(aclService);
    }

    @Bean
    public AclPermissionCacheOptimizer permissionCacheOptimizer(AclService aclService){
        return new AclPermissionCacheOptimizer(aclService);
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(
            AclPermissionEvaluator permissionEvaluator,
            AclPermissionCacheOptimizer permissionCacheOptimizer) {
        var expressionHandler = new DefaultMethodSecurityExpressionHandler();

        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(permissionCacheOptimizer);

        return expressionHandler;
    }

}
