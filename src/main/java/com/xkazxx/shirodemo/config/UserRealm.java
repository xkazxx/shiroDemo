package com.xkazxx.shirodemo.config;

import com.xkazxx.shirodemo.entity.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.Md5CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.util.DigestUtils;
import sun.security.provider.MD5;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.shirodemo.config
 * date:2021/8/24
 */
public class UserRealm extends AuthorizingRealm {

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof UsernamePasswordToken; // 限制只支持用户名和密码得形式进行验证
  }

  // 授权
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    System.out.println("开始授权！");
    UserInfo userInfo = (UserInfo) principalCollection.getPrimaryPrincipal();// 获取doGetAuthenticationInfo方法附加的对象
    Set<String> permissions = userInfo.getPermissions();
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.addStringPermissions(permissions);
    return authorizationInfo;
  }

  // 认证
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    System.out.println("开始认证！");
    UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    String username = token.getUsername();
    UserInfo userInfo = getUserInfoByUsername(username);
    if (userInfo == null) {
      System.out.println("用户名有误！");
      return null;
    }
    return new SimpleAuthenticationInfo(userInfo, userInfo.getPassword(), ByteSource.Util.bytes(userInfo.getSalt()), this.getName());
  }

  private UserInfo getUserInfoByUsername(String username) {
    // 实际使用时根据username去数据库中查询用户信息
    String name = "admin";
    String salt = "salt"; // 盐值可以是用户名+固定字符串
    String password = new Md5Hash("123", salt, 1000).toHex();
    UserInfo userInfo = new UserInfo("1", name, password, new HashSet<>(Arrays.asList("user:del")), salt);
    return userInfo;
  }

  @Override
  public void clearCache(PrincipalCollection principals) {
    super.clearCache(principals); // logout时调用，清除当前用户的权限认证缓存
  }

}