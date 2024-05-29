package com.lubase.core.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.constant.CommonConstant;
import com.lubase.core.exception.LoginErrorException;
import com.lubase.core.model.NavVO;
import com.lubase.core.service.AppNavDataService;
import com.lubase.core.service.UserInfoService;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.service.userright.model.UserRightInfo;
import com.lubase.core.util.StringEncodeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author A
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Value("${custom.password-reg:^(?![a-zA-Z]+$)(?![a-z0-9]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9~!@#$%^&*()\\-_+?/\\.]{8,16}$}")
    private String passwordReg;
    @Value("${custom.password-tip:密码不符合复杂度(需包括数字、小写字母、大写字母、特殊字符四种的三种，长度在8-16位)}")
    private String passwordTip;
    @Autowired
    DataAccess dataAccess;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    UserRightService userRightService;

    @Autowired
    AppNavDataService appNavDataService;

    @Autowired
    RedisTemplate redisTemplate;
    /**
     * jwt 默认的secret
     */
    public static String secretKey;


    @Value("${custom.jwt-secret:abcdef}")
    public void setSecretKey(String preSecretKey) {
        if (StringUtils.isAllEmpty(preSecretKey)) {
            secretKey = CommonConstant.JWT_SECRET_KEY;
        } else {
            secretKey = preSecretKey + CommonConstant.JWT_SECRET_KEY;
        }
    }
    @Override
    public LoginUser getUser(String uid, String pwd, String vcode) throws LoginErrorException {
        //判断验证码
        LoginUser user = checkVerifyCode(vcode, uid);
        if (user.getErrorCount() < 0) {
            return user;
        }
        String encodePwd = StringEncodeUtil.strToMd5Str(pwd);
        QueryOption queryOption = new QueryOption("sa_account");
        TableFilter filter = TableFilterWrapper.and().eq("user_code", uid).eq("password", encodePwd).build();
        queryOption.setTableFilter(filter);
        DbCollection collection = dataAccess.query(queryOption);
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            Integer enableTag = TypeConverterUtils.object2Integer(entity.get("enable_tag"), 0);
            if (enableTag.equals(0)) {
                throw new LoginErrorException("402", "账号被禁用");
            }
            user = new LoginUser();
            user.setId(entity.getId());
            user.setCode(entity.get("user_code").toString());
            user.setName(entity.get("user_name").toString());
            user.setOrgId(TypeConverterUtils.object2String(entity.get("organization_id"), "0"));
            user.setErrorCount(0);
            userRightService.getUserRight(user.getId());
            //TODO 清缓存失败了怎么办？
            clearCacheErrorCount(uid);
        } else {
            var count = calcErrorCount(uid);
            log.info(String.format("%s 登录失败%s次", uid, count));
            user.setErrorCount(count);
        }
        return user;
    }

    @SneakyThrows
    @Override
    public Integer modifyUserPwd(String uid, String newPwd, String oldPwd) {
        String encodePwd = StringEncodeUtil.strToMd5Str(newPwd);
        String encodeOldPwd = null;
        if (oldPwd != null) {
            encodeOldPwd = StringEncodeUtil.strToMd5Str(oldPwd);
        }
        QueryOption queryOption = new QueryOption("sa_account");
        TableFilter filter = TableFilterWrapper.and().eq("user_code", uid).build();
        queryOption.setTableFilter(filter);
        DbCollection collection = dataAccess.query(queryOption);
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            var originalPwd = entity.get("password");
            if (encodeOldPwd != null && !encodeOldPwd.equals(originalPwd)) {
                return -10;
            }
            entity.setState(EDBEntityState.Modified);
            entity.put("password", encodePwd);
            return dataAccess.update(collection);
        }
        return 0;
    }

    @Override
    public String createUserToken(LoginUser user) {
        // 设置token 有效期，8小时
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(8);
        String token = JWT.create().withAudience(user.getId().toString())
                .withClaim("uco", user.getCode())
                .withClaim("una", user.getName())
                .withClaim("uor", user.getOrgId())
                .withExpiresAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(secretKey));
        return token;
    }


    @Override
    public LoginUser verifyToken(String token) throws AuthenticationException {
        Long userId = null;
        String code, name, orgId;
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            userId = Long.valueOf(decodedJWT.getAudience().get(0));
            code = decodedJWT.getClaim("uco").asString();
            name = decodedJWT.getClaim("una").asString();
            orgId = decodedJWT.getClaim("uor").asString();
        } catch (JWTDecodeException exception) {
            throw new AuthenticationException("401 payload");
        } catch (Exception exception) {
            throw new AuthenticationException("401 payload 2");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthenticationException("401 Signature");
        } catch (Exception exception) {
            throw new AuthenticationException("401 Signature 2");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setId(userId);
        loginUser.setName(name);
        loginUser.setCode(code);
        loginUser.setOrgId(orgId);
        loginUser.setToken(token);
        appHolderService.setUser(loginUser);
        return loginUser;
    }

    @Override
    public List<NavVO> getAdminNavData() {
        List<NavVO> allNavVOList = appNavDataService.getAdminNavData();

        List<NavVO> rightNavList = new ArrayList<>();
        LoginUser user = appHolderService.getUser();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        if (rightInfo.getIsSupperAdministrator()) {
            rightNavList = allNavVOList;
        } else if (rightInfo.getIsAppAdministrator()) {
            for (NavVO nav : allNavVOList) {
                if (userRightService.checkFuncRight(rightInfo, nav.getId())) {
                    rightNavList.add(nav);
                }
            }
        }
        return rightNavList;
    }

    @SneakyThrows
    @Override
    public List<NavVO> getSettingNavData(Long appId) {
        if (appId == null) {
            throw new ParameterNotFoundException("appId");
        }
        //应用配置需要管理员才可以进行配置，其他人无权进行配置
        LoginUser user = appHolderService.getUser();
        if (!appNavDataService.getAppManager(appId).contains(user.getId().toString())) {
            return new ArrayList<>();
        }

        List<NavVO> allNavVOList = appNavDataService.getSettingNavData();
        List<NavVO> rightNavList = new ArrayList<>();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        if (rightInfo.getIsSupperAdministrator()) {
            for (NavVO nav : allNavVOList) {
                if (!CommonConstant.SUPPER_UNACCESS_PAGE.contains(nav.getId().toString())) {
                    rightNavList.add(nav);
                }
            }
        } else if (rightInfo.getIsAppAdministrator()) {
            for (NavVO nav : allNavVOList) {
                if (userRightService.checkFuncRight(rightInfo, nav.getId())) {
                    rightNavList.add(nav);
                }
            }
        }
        return rightNavList;
    }

    @SneakyThrows
    @Override
    public List<NavVO> getAppPreviewNavDataId(Long appId) {
        if (appId == null) {
            throw new ParameterNotFoundException("appId");
        }
        List<NavVO> allNavVOList = appNavDataService.getNavDataByAppId(appId);

        LoginUser user = appHolderService.getUser();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        //超级管理员无应用的具体访问权限，子管理员对自己的应用具有全部访问权限
        if (rightInfo.getIsAppAdministrator() && appNavDataService.getAppManager(appId).contains(user.getId().toString())) {
            return allNavVOList;
        } else {
            return new ArrayList<>();
        }
    }

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

    @Override
    public String validatePassword(String password) {
        var pattern = Pattern.compile(passwordReg);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            return passwordTip;
        } else {
            return "";
        }
    }

    private static Color getRandomColor(Random random) {
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

    @SneakyThrows
    LoginUser checkVerifyCode(String vcode, String userCode) {
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
