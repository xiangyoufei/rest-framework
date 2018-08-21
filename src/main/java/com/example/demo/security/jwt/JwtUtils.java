package com.example.demo.security.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {


	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	private static final int EXPIRE_TIME = 5*60*1000;


	/**
	 * 密钥
	 */
	private String secret;
	/**
	 * 有效期限
	 */
	private static	 int expire;
	/**
	 * 存储 token
	 */
	private String header;

	/**
	 * 生成jwt token
	 *
	 * @param userId 用户ID
	 * @return token
	 */
	public String generateToken(long userId) {
		Date nowDate = new Date();

		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject(userId + "")
				.setIssuedAt(nowDate)
				.setExpiration(DateUtils.addDays(nowDate, expire))
				// 这里我采用的是 HS512 算法
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	/**
	 * 解析 token，
	 * 利用 jjwt 提供的parser传入秘钥，
	 *
	 * @param token token
	 * @return 数据声明 Map<String, Object>
	 */
	public Claims getClaimByToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			return null;
		}
	}






	/**
	 * token是否过期
	 *
	 * @return true：过期
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}






	/**
	 * 校验token是否正确
	 * @param token 密钥
	 * @param secret 用户的密码
	 * @return 是否正确
	 */
	public static boolean verify(String token, String username, String secret) {
		try {
			Algorithm algorithm  = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm)
					.withClaim("username", username)
					.build();
			/*DecodedJWT jwt = */verifier.verify(token);
			return true;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JWTVerificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 * @return token中包含的用户名
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 生成签名,5min后过期
	 * @param username 用户名
	 * @param secret 用户的密码
	 * @return 加密的token
	 */
	public static String sign(String username, String secret) {
		try {
			// 附带username信息
			return JWT.create()
					.withClaim("username", username)
					.withExpiresAt(DateUtils.addDays(new Date(), EXPIRE_TIME))
					.sign(Algorithm.HMAC256(secret));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

















}