package io.vickze.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import io.vickze.constant.KaptchaConstant;
import io.vickze.constant.SysUserConstant;
import io.vickze.entity.ResultDO;
import io.vickze.exception.CheckException;

/**
 * 登录相关
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysIndexController {
    @Autowired
    private Producer producer;

    @RequestMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        SecurityUtils.getSubject().getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public ResultDO login(String username, String password, String captcha) throws IOException {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            throw new CheckException(SysUserConstant.IN_LOGIN);
        }
        String kaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (kaptcha == null) {
            throw new CheckException(KaptchaConstant.KAPTCHA_INVALID_MESSAGE);
        }
        SecurityUtils.getSubject().getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(kaptcha)) {
            throw new CheckException(KaptchaConstant.KAPTCHA_INCORRECT_MESSAGE);
        }

        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, true));
        } catch (UnknownAccountException uae) {
            throw new CheckException(SysUserConstant.ACCOUNT_MESSAGE);
        } catch (IncorrectCredentialsException ice) {
            throw new CheckException(SysUserConstant.PASSWORD_MESSAGE);
        } catch (LockedAccountException lae) {
            throw new CheckException(SysUserConstant.StatusEnum.PAUSE.getMessage());
        }

        return ResultDO.success();
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/sys/logout", method = RequestMethod.POST)
    public ResultDO logout() {
        //shiro登出
        SecurityUtils.getSubject().logout();
        return ResultDO.success();
    }

    @GetMapping("/sys/unauthorized")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultDO unauthorized() {
        return ResultDO.error(HttpStatus.UNAUTHORIZED.value(), "没有权限，请联系管理员授权");
    }
}
