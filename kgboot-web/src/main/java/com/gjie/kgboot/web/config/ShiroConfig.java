package com.gjie.kgboot.web.config;


import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

@Configuration
public class ShiroConfig {

    //1、创建realm对象，需要自定义类
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    //2、DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getdefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联userRealm
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager());
        return securityManager;

    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // sessionId cookie
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName("sid");
        // 设置cookie的存活时间为30分钟，与session timeout时间一致
        cookie.setMaxAge(1800);
        cookie.setHttpOnly(true);
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setSessionIdCookieEnabled(true);
        //默认使用MemerySessionDao，设置为EnterpriseCacheSessionDAO以配合ehcache实现分布式集群缓存支持
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionManager.setSessionDAO(sessionDAO);
        return sessionManager;
    }


    //3、shiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(
            @Qualifier("getdefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        //实现shiro的内置过滤器
        /**
         * anon:无需认证就可以访问
         * authc:必须认证了才能访问
         * user:必须拥有记住我功能才能用
         * perms:拥有对某个资源的权限才能访问
         * role:拥有某个角色权限
         */

        //拦截
        Map<String, String> filter = new LinkedHashMap<>();

        //授权，正常的情况下，未授权跳转到未授权页面
        filter.put("/test/abc", "anon");
        filter.put("/**", "authc");
        bean.setFilterChainDefinitionMap(filter);

        //设置登录的请求
        bean.setLoginUrl("/toLogin");

        //未授权页面
        bean.setUnauthorizedUrl("/noauth");

        return bean;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        /*
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        advisorAutoProxyCreator.setUsePrefix(true);
        // advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(UserRealm userRealm) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(getdefaultWebSecurityManager(userRealm));
        return authorizationAttributeSourceAdvisor;
    }


//    @Bean("sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(multipleDataSource(db1(), db2()));
//        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*.xml"));
//
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        configuration.setJdbcTypeForNull(JdbcType.NULL);
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setCacheEnabled(false);
//        sqlSessionFactory.setConfiguration(configuration);
//        //PerformanceInterceptor(),OptimisticLockerInterceptor()
//        //添加分页功能
//        sqlSessionFactory.setPlugins(new Interceptor[]{
//                paginationInterceptor()
//        });
////        sqlSessionFactory.setGlobalConfig(globalConfiguration()); //注释掉全局配置，因为在xml中读取就是全局配置
//        return sqlSessionFactory.getObject();
//    }



}
