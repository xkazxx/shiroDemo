package com.xkazxx.shirodemo.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.shirodemo.config
 * date:2021/8/24
 */
@Configuration
public class ShiroConfig {
  /**
   * 开启EhCache缓存
   */
  @Bean
  public EhCacheManager ehCacheManager() {
    EhCacheManager ehCacheManager = new EhCacheManager();
    ehCacheManager.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
    return ehCacheManager;
  }

  @Bean
  public HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
    matcher.setHashAlgorithmName("MD5"); // 使用md5散列加密
    // 使用(org.apache.shiro.crypto.hash)Md5Hash md5Hash = new Md5Hash(password, salt, 1000);生成散列后的密码存库
    matcher.setHashIterations(1000);
    return matcher;
  }

  @Bean
  public UserRealm realm(HashedCredentialsMatcher matcher, EhCacheManager ehCacheManager) {
    UserRealm userRealm = new UserRealm();
    // 用户密码加密，使用shiro的SimpleAuthenticationInfo进行处理
    userRealm.setCredentialsMatcher(matcher);
    // 查询授权，启用缓存
    userRealm.setAuthenticationCacheName("authorizationCache");
    userRealm.setAuthenticationCachingEnabled(true);
    userRealm.setCacheManager(ehCacheManager);
    return userRealm;

  }

  @Bean
  public SecurityManager getSecurityManager(UserRealm realm) {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(realm);
    return securityManager;
  }

  /**
   * Filter Chain定义说明
   * <p>
   * 1、一个URL可以配置多个Filter，使用逗号分隔
   * 2、当设置多个过滤器时，全部验证通过，才视为通过
   * 3、部分过滤器可指定参数，如perms，roles
   */
  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
    // 设置安全管理器
    factoryBean.setSecurityManager(securityManager);
    // factoryBean.setLoginUrl("/login");  设置未显示登陆页面
    // factoryBean.setUnauthorizedUrl("/login"); 设置未授权显示页面
    // factoryBean.setSuccessUrl("/login"); 设置登陆成功显示页面

    Map<String, String> chainDefinitionMap = factoryBean.getFilterChainDefinitionMap();
    // 添加shiro的内置过滤器
    // anon:无需认证即可访问；authc:必须认证了才能访问；user:必须拥有记住我功能才能使用；perms:必须对某个资源有权限才能访问；role：拥有某个角色权限才能访问
    chainDefinitionMap.put("/login", "anon");
    chainDefinitionMap.put("/add", "authc");
    chainDefinitionMap.put("/del", "perms[user:add,user:del]");// 必须同时拥有集合中的所有权限才能访问
    // 添加自己的filter实现登陆次数限制，登陆熔断
//    Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
//    filterMap.put("myFilter", new myFilter());
//    factoryBean.setFilters(filterMap);
//    chainDefinitionMap.put("/**", "myFilter");
    return factoryBean;
  }

  /**
   * 下面的代码是添加注解支持
   *
   * @return
   */
  @Bean
  @DependsOn("lifecycleBeanPostProcessor")
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    /**
     * 解决重复代理问题 github#994
     * 添加前缀判断 不匹配 任何Advisor
     */
    defaultAdvisorAutoProxyCreator.setUsePrefix(true);
    defaultAdvisorAutoProxyCreator.setAdvisorBeanNamePrefix("_no_advisor");
    return defaultAdvisorAutoProxyCreator;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
  }

  @Bean
  public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

}
