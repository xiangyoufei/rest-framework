package com.example.demo.util;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.minidev.json.JSONObject;
/**
 *
* 类描述：    生成token的工具类
* 创建人：yang   
* 创建时间：2018-05-24 08:58:53   
* @version
 */
@Component
public class JwtTokenProvider implements InitializingBean {
    SecretKeySpec key;

    /**
     * @param key
     *            密钥(例如：12345678)
     */
   /* public JwtTokenProvider(String key) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        this.key = secretKeySpec;
    }*/

    /**
     * 生成token
     * 
     * @return
     */
    public String createToken(Claims claims) {
        String compactJws = Jwts.builder()
        		.setPayload(JSONObject.toJSONString(claims))//负荷 {里面包含自己定义的json串，及验证数据}
        		.setExpiration(new Date(System.currentTimeMillis() + 3600000))//设置超时间
        		.signWith(SignatureAlgorithm.HS512, key)//设置加密方式
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();
        return compactJws;
    }

    /** token转换为 */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		 SecretKeySpec secretKeySpec = new SecretKeySpec(Constants.SECRETKEYSPEC.getBytes(), SignatureAlgorithm.HS512.getJcaName());
	        this.key = secretKeySpec;
	}

}
