package com.cy.usercenter.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    //定义默认有效期为一个小时 单位：毫秒
    public static final Long JWT_DEFAULT_EXPIRES = 24 * 60 * 60 * 1000L;

    //设置明文密钥(注意 不能含有非法字符)
    public static final String JWT_KEY = "FakeItEasyTo";

    //签发者
    public static final String ISSUER = "chenye";

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 创建一个token
     * @param subject   可以理解为存放实际需要传递的数据
     */
    public static String createJWT(String id, String subject, Long settingExpiresTime){
        //创建我们将要使用的JWT签名算法对（token）令牌进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //获取当前时间并转换为date对象
        long nowMillis = System.currentTimeMillis();

        //签发时间
        Date now = new Date(nowMillis);

        //SettingExpiresTime判断用户是否需要设置token有效时间
        if (settingExpiresTime == null) {
            //如果为空，将默认有效期赋值给SettingExpiresTime
            settingExpiresTime = JWT_DEFAULT_EXPIRES;
        }

        //结束时间 = 当前时间 + 设定的有效期时间
        long expiresTime = nowMillis + settingExpiresTime;
        //转换为date数据类型
        Date date = new Date(expiresTime);

        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")    //一下两行就是Header
                .setHeaderParam("alg", "HS256")
                .setId(id)
                .setSubject(subject) //主题，可以是json数据
                .setIssuer(ISSUER)   //签发者
                .setIssuedAt(now)   //签发时间
                .signWith(signatureAlgorithm,generalKey()) //使用ES256对称加密算法签名，第二个参数为加密后的明文密钥
                .setExpiration(date);   //设置过期时间

        //compact:规则构建JWT并将其序列化为紧凑的URL安全字符串
        return builder.compact();
    }

    public static String createJwt(String subject,Long settingExpiresTime) {
        return createJWT(getUUID(),subject,settingExpiresTime);
    }

    public static String createJwt(String subject) {
        return createJWT(getUUID(),subject,null);
    }

    /**
     * 加密明文密钥
     */
    public static SecretKey generalKey(){
        //调用base64中的getDecoder方法获取解码器，调用解码器中的decode方法将明文密钥进行编码
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        //AES：加密方式
        return new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
    }

    /**
     * 解析jwt
     */
    //此处当解析不出的时候会抛出异常
    public static Claims parseJWT(String jwt) throws Exception{
        return Jwts.parser()    //获取jwts的解析器
                .setSigningKey(generalKey())    //设置加密后的密钥进行比对
                .parseClaimsJws(jwt)    //解析传入的jwt字符串
                .getBody();     // 拿到claims对象返回
    }


}
