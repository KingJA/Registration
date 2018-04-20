package com.tdr.registration.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES对称加解密算法
 * Created by Linus_Xie on 2016/9/20.
 */
public class DESCoder {
    public static String encrypt(String content, String key) {
        byte[] result = null;
        try {
            Key secretKey = getKey(key);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            result = cipher.doFinal(content.getBytes());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return android.util.Base64.encodeToString(result, android.util.Base64.DEFAULT);
    }

   /* public static String decrypt(String content, String key) {
        byte[] result = null;
        try {
            Key secretKey = getKey(key);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            result = cipher.doFinal(android.util.Base64.decodeBase64(content));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new String(result);

    }*/

    private static Key getKey(String key)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = secretKeyFactory.generateSecret(desKeySpec);
        return secretKey;
    }

}
