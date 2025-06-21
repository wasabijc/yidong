package com.example.utils;

import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils
{
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    jwt附带的信息
     * @return jwt
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims)
    {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date expirationTime = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(expirationTime);

        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return jwt附带的信息
     */
    public static Claims parseJWT(String secretKey, String token) throws ExpiredJwtException
    {
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }


    /**
     * 检查 JWT 是否过期。
     *
     * @param secretKey JWT 签名所使用的秘钥
     * @param token     要验证的 JWT
     * @return 如果 JWT 已过期返回 true，否则返回 false
     */
    public boolean isExpiredJwt(String secretKey, String token)
    {
        try
        {
            // 解析 JWT，如果未抛出异常则表示 JWT 有效
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return false; // JWT 未过期
        }
        catch (ExpiredJwtException e)
        {
            return true; // JWT 已过期
        }
    }

}
