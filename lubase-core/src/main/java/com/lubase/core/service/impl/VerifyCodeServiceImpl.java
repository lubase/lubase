package com.lubase.core.service.impl;

import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.service.VerifyCodeService;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public int calcErrorCount(String userCode) {
        Object o = redisTemplate.opsForValue().get(getCachePre(CacheRightConstant.PRE_USER_LOGIN_ERR) + userCode);
        var errorCount = 0;
        if (o != null) {
            errorCount = TypeConverterUtils.object2Integer(o);
        }
        errorCount++;
        log.info("错误次数放入缓存");
        redisTemplate.opsForValue().set(getCachePre(CacheRightConstant.PRE_USER_LOGIN_ERR) + userCode, errorCount, 24, TimeUnit.HOURS);
        return errorCount;
    }

    @Override
    public void clearCacheErrorCount(String userCode) {
        redisTemplate.delete(getCachePre(CacheRightConstant.PRE_USER_LOGIN_ERR) + userCode);
        redisTemplate.delete(getCachePre(CacheRightConstant.PRE_USER_LOGIN_VC) + userCode);
    }

    @Override
    @Transactional
    public BufferedImage getVerifyCode(String userCode) {
        Random random = new Random();
        int w = 100;
        int h = 40;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, w, h);
        //边框颜色
        graphics.setColor(Color.BLUE);
        graphics.drawRect(0, 0, w - 1, h - 1);
        //字体
        Font font = new Font("Fixedsys", Font.PLAIN, h - 10);
        graphics.setFont(font);
        //加干扰线
        for (int i = 0; i < (4 * 2); i++) {
            graphics.setColor(getRandomColor(random));
            int x1 = random.nextInt(w);
            int x2 = random.nextInt(w);
            int y1 = random.nextInt(h);
            int y2 = random.nextInt(h);
            graphics.drawLine(x1, y1, x2, y2);
        }
        //加噪点
        for (int i = 0; i < (4 * 3); i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            graphics.setColor(getRandomColor(random));
            graphics.fillRect(x, y, 2, 2);
        }
        var code = "";
        int cw = w / (4 + 4);
        int ch = h - 5;
        for (int i = 0; i < 4; i++) {
            int x = 7;
            if (i >= 1) {
                x = (w / 5 * (i)) + 20;
            }
            var c = random.nextInt(10);
            code += TypeConverterUtils.object2String(c);
            graphics.setColor(getRandomColor(random));
            //字体旋转角度
            int degree = random.nextInt() % 15;//角度小于15度
            graphics.rotate(degree * Math.PI / 180, x, 45);//反向
            graphics.drawString(c + "", x, ch);
            graphics.rotate(-degree * Math.PI / 180, x, 45);//正向
        }
        redisTemplate.opsForValue().set(getCachePre(CacheRightConstant.PRE_USER_LOGIN_VC) + userCode, code, 1, TimeUnit.HOURS);

        return image;
    }


    private static Color getRandomColor(Random random) {
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

    @Override
    @SneakyThrows
    public LoginUser checkVerifyCode(String vcode, String userCode) {
        LoginUser user = new LoginUser();
        Object o = redisTemplate.opsForValue().get(getCachePre(CacheRightConstant.PRE_USER_LOGIN_ERR) + userCode);
        var errorCount = 0;
        if (o != null) {
            errorCount = TypeConverterUtils.object2Integer(o);
        }

        if (errorCount >= 5) {
            if (errorCount >= 5 && StringUtils.isEmpty(vcode)) {
                log.info(userCode + "错误大于5次，传入验证码为空");
                user.setErrorCount(-3);
                return user;
            }
            var code = redisTemplate.opsForValue().get(getCachePre(CacheRightConstant.PRE_USER_LOGIN_VC) + userCode);
            if (code == null || StringUtils.isEmpty(code.toString())) {
                log.info(userCode + "错误大于5次，服务端验证码已失效");
                user.setErrorCount(-2);
                return user;
            }
            if (!StringUtils.isEmpty(vcode) && !vcode.equals(code)) {
                log.info(userCode + "输入验证码错误");
                user.setErrorCount(-1);
                return user;
            }
        }
        user.setErrorCount(0);
        return user;
    }

    private String getCachePre(String preKey) {
        return preKey.replace("'", "");
    }
}
