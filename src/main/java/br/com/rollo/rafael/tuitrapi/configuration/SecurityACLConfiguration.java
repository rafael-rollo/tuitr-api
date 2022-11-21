package br.com.rollo.rafael.tuitrapi.configuration;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityACLConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean(EhCacheManagerFactoryBean aclCacheManagerFactory) {
        CacheManager aclCacheManager = aclCacheManagerFactory.getObject();

        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager);
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public EhCacheBasedAclCache aclCache(EhCacheFactoryBean ehCacheFactoryBean,
                                         PermissionGrantingStrategy permissionGrantingStrategy,
                                         AclAuthorizationStrategy aclAuthorizationStrategy) {
        Ehcache ehCache = ehCacheFactoryBean.getObject();
        return new EhCacheBasedAclCache(ehCache, permissionGrantingStrategy, aclAuthorizationStrategy);
    }

    @Bean
    public LookupStrategy lookupStrategy(AclCache aclCache,
                                         AclAuthorizationStrategy aclAuthorizationStrategy) {
        return new BasicLookupStrategy(dataSource, aclCache,
                aclAuthorizationStrategy, new ConsoleAuditLogger());
    }

    @Bean
    public MutableAclService aclService(LookupStrategy lookupStrategy, AclCache aclCache) {
        JdbcMutableAclService aclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
        aclService.setClassIdentityQuery("call identity()");
        aclService.setSidIdentityQuery("call identity()");
        return aclService;
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
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();

        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(permissionCacheOptimizer);

        return expressionHandler;
    }



}
