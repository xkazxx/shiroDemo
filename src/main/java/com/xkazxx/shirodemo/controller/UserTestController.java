package com.xkazxx.shirodemo.controller;

import com.xkazxx.shirodemo.exception.BusinessException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.shirodemo.controller
 * date:2021/8/24
 */
@RestController
public class UserTestController {


  @RequestMapping(value = "/add")
  public String userAdd() {
    Subject subject = SecurityUtils.getSubject();
    Session session = subject.getSession();
    Collection<Object> attributeKeys = session.getAttributeKeys();
    attributeKeys.forEach(key -> {
      Object attribute = session.getAttribute(key);
      System.out.println("[key]=" + key +" [value]=" + attribute);
    });
    return "添加成功";
  }

  @RequestMapping(value = "/del")
  public String userDel() {
    return "删除成功";
  }

  @RequestMapping(value = "/login")
  public String login(String username, String password) {
    if (StringUtils.hasLength(username) && StringUtils.hasLength(password)) {
      Subject subject = SecurityUtils.getSubject();
      Session session = subject.getSession();
      Collection<Object> attributeKeys = session.getAttributeKeys();
      attributeKeys.forEach(System.out::println);
      UsernamePasswordToken token = new UsernamePasswordToken(username, password);
      try {
        token.setRememberMe(true); // 设置记住我
        subject.login(token);
        attributeKeys = session.getAttributeKeys();
        attributeKeys.forEach(key -> {
          Object attribute = session.getAttribute(key);
          System.out.println("[key]=" + key +" [value]=" + attribute);
        });
        return "登陆成功";
      } catch (AuthenticationException e) {
        System.out.println(e);
        return "用户名或密码错误";
      }
    } else {
      return "请输入用户名或密码";
    }
  }

  @RequestMapping(value = "/logout")
  public String logout() {
    Subject subject = SecurityUtils.getSubject();
    subject.logout();
    return "退出成功";
  }

  @RequestMapping(value = "/exnull")
  public String exnull() {
    throw new NullPointerException("test null point exception");
  }

  @RequestMapping(value = "/exex")
  public String exex() throws Exception {
    throw new Exception("test null point Exception");
  }

  @RequestMapping(value = "/exbe")
  public String exbe() {
    throw new BusinessException("test null point BusinessException", 1);
  }

}
