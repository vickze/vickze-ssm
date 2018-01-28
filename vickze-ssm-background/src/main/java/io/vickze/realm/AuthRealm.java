package io.vickze.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import io.vickze.constant.SysUserConstant;
import io.vickze.entity.SysUserDO;
import io.vickze.service.SysUserService;

/**
 * 认证
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:24:42
 */
@Component
public class AuthRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;


    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        long userId = Long.valueOf(principals.getPrimaryPrincipal().toString().split(SysUserConstant.USER_SPLIT)[0]);

        //用户权限列表
        Set<String> permsSet = sysUserService.getPermissionsByUserId(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String)token.getPrincipal();
        SysUserDO sysUserDO = sysUserService.getByUsername(username);

        //账号不存在
        if (sysUserDO == null) {
            throw new UnknownAccountException();
        }
        //账号锁定
        if (sysUserDO.getStatus() == SysUserConstant.StatusEnum.PAUSE.getStatus()) {
            throw new LockedAccountException();
        }

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        usernamePasswordToken.setUsername(sysUserDO.getId() + SysUserConstant.USER_SPLIT
                + usernamePasswordToken.getUsername());

        //加密算法为sha256
        HashedCredentialsMatcher sha256CredentialsMatcher = new HashedCredentialsMatcher();
        sha256CredentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        setCredentialsMatcher(sha256CredentialsMatcher);

        return new SimpleAuthenticationInfo(
                sysUserDO.getId() + SysUserConstant.USER_SPLIT  + sysUserDO.getUsername(),
                sysUserDO.getPassword(),
                ByteSource.Util.bytes(sysUserDO.getSalt()),
                getName());
    }
}
