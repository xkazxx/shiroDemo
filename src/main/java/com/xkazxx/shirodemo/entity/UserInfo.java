package com.xkazxx.shirodemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author created by xkazxx
 * @version v0.0.1
 * description: com.xkazxx.shirodemo.entity
 * date:2021/8/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {
  private String id;
  private String username;
  private String password;
  private Set<String> permissions;
  private String salt;
}
