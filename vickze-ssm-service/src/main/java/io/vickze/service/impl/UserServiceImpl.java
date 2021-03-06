package io.vickze.service.impl;




import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import io.vickze.constant.MQConstant;
import io.vickze.constant.UserConstant;
import io.vickze.dao.BaseDao;
import io.vickze.dao.UserDao;
import io.vickze.entity.ResultDO;
import io.vickze.entity.TokenDO;
import io.vickze.entity.UpdatePasswordDO;
import io.vickze.entity.UserDO;
import io.vickze.exception.CheckException;
import io.vickze.service.TokenService;
import io.vickze.service.UserService;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:42
 */
public class UserServiceImpl extends BaseServiceImpl<Long, UserDO> implements UserService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    protected BaseDao<Long, UserDO> getBaseDao() {
        return userDao;
    }

    @Override
    public TokenDO register(UserDO userDO) {
        if (userDao.getByMobile(userDO.getMobile()) != null) {
            throw new CheckException("该手机号已被注册");
        }

        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        userDO.setPassword(new Sha256Hash(userDO.getPassword(), salt).toHex());
        userDO.setSalt(salt);
        super.save(userDO);

        return tokenService.generateToken(userDO.getId());
    }

    @Override
    public TokenDO login(UserDO userDO) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserDO dbUser = userDao.getByMobile(userDO.getMobile());
        if (dbUser == null) {
            throw new CheckException("手机号未注册");
        }

        if (!dbUser.getPassword().equals(new Sha256Hash(userDO.getPassword(), dbUser.getSalt()).toHex())) {
            throw new CheckException("密码错误");
        }

        TokenDO tokenDO = tokenService.generateToken(dbUser.getId());
        //发送用户登录消息
        rabbitTemplate.convertAndSend(MQConstant.USER_LOGIN_QUEUE, dbUser.getId());
        return tokenDO;
    }

    @Override
    public ResultDO logout(long userId) {
        tokenService.removeUserToken(userId);
        return ResultDO.success();
    }

    @Override
    public ResultDO updatePassword(UserDO userDO, UpdatePasswordDO updatePasswordDO) {
        updatePasswordDO.setOldPassword(new Sha256Hash(updatePasswordDO.getOldPassword(), userDO.getSalt()).toHex());
        updatePasswordDO.setNewPassword(new Sha256Hash(updatePasswordDO.getNewPassword(), userDO.getSalt()).toHex());

        if (userDao.updatePassword(userDO.getId(), updatePasswordDO.getOldPassword(), updatePasswordDO.getNewPassword()) != 1) {
            return ResultDO.error("旧密码错误");
        }
        //修改密码成功，移除用户token
        tokenService.removeUserToken(userDO.getId());
        return ResultDO.error("修改密码成功");
    }

    @Override
    public TokenDO refreshToken(String token) {
        long userId = tokenService.validTokenCanRefresh(token);
        if (userId == UserConstant.UN_LOGIN) {
            throw new CheckException("登录凭据已失效，请重新登录");
        }

        return tokenService.generateToken(userId);
    }
}
