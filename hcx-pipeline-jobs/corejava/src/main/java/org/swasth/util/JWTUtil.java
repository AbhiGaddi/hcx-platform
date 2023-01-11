package org.swasth.util;

import com.typesafe.config.Config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.swasth.job.BaseJobConfig;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

public class JWTUtil() {

    public static BaseJobConfig config;

    public JWTUtil(Config config) {
        JWTUtil.config = (BaseJobConfig) config;
    }

    public static String generateHCXGatewayToken() throws NoSuchAlgorithmException, InvalidKeySpecException {
        long date = new Date().getTime();
        HashMap<String,Object> headers = new HashMap<>();
        headers.put(Constants.TYP, Constants.JWT);
        HashMap<String,Object> payload = new HashMap<>();
        payload.put(Constants.JTI, UUID.randomUUID());
        payload.put(Constants.ISS, config.hcxRegistryCode);
        payload.put(Constants.SUB, config.hcxRegistryCode);
        payload.put(Constants.IAT, date);
        payload.put(Constants.EXP, new Date(date + config.expiryTime).getTime());
        return generateJWS(headers, payload);
    }

    public static String generateJWS(Map<String, Object> headers, Map<String, Object> payload) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyDecoded = Base64.getDecoder().decode(config.getPrivateKey());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyDecoded);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
        return Jwts.builder().setHeader(headers).setClaims(payload).signWith(SignatureAlgorithm.RS256, privateKey).compact();
    }
}
