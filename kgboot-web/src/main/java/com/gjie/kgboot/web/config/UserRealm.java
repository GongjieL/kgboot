package com.gjie.kgboot.web.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

public class UserRealm extends AuthorizingRealm {


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");

        //SimpleAuthorizationInfo
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //拿到当前登录的这个对象
        Subject subject = SecurityUtils.getSubject();
        //拿到User
        User currentUser = (User) subject.getPrincipal();
        //设置当前用户的权限
        info.addRole(currentUser.getPerms());

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthorizationInfo");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;

        //连接真实数据库
        //假装获取用户
        User user = new User();
        user.setId(1);
        user.setUsername("zhangsan");
        user.setPassword("123456");
        user.setPerms("abc");

        //没有这个人
        if (user == null) {
            //UnknownAccountException
            return null;
        }

//        Subject currentSubject = SecurityUtils.getSubject();
//        Session session = currentSubject.getSession();
//        session.setAttribute("loginUser",user);

        //可以加密，MD5:e10adc3949ba59abbe56e057f20f883e  MD5:盐值加密
        //密码认证，shiro做
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
    }

}
