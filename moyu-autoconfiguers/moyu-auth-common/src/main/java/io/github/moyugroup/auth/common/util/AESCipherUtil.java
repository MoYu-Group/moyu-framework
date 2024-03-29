package io.github.moyugroup.auth.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 基于 hutool 的 AES-GCM 加密解密操作工具类
 * <p>
 * Created by fanfan on 2024/03/29.
 */
@Slf4j
public class AESCipherUtil {

    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final String SPLIT = ":";

    /**
     * 通过字符串的秘钥生成 AES-256 加密密钥
     *
     * @param secret
     * @return
     */
    private static byte[] getAESKeyFromSecret(String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(secret.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 随机生成指定长度的IV
     *
     * @param length 长度
     * @return
     */
    private static byte[] generateRandomIV(int length) {
        byte[] iv = new byte[length];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * 使用 AES-GCM 模式加密数据
     *
     * @param key  AES 加密 Key
     * @param data 需要加密的数据
     * @return 加密后的数据 base64 字符串，格式 iv:加密数据
     */
    public static String encryptData(String key, String data) {
        // 转换为 AES-256 加密密钥
        byte[] aesKey = getAESKeyFromSecret(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
        // 随机生成12字节的IV（GCM推荐的长度）
        byte[] iv = generateRandomIV(12);
        // 初始化GCM参数规格
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        // 创建并初始化加密器
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
            // 加密
            byte[] encryptData = cipher.doFinal(data.getBytes());
            // 转为 base64
            Base64.Encoder base64Encoder = Base64.getEncoder();
            // 将 iv 和 base64 一起返回
            return base64Encoder.encodeToString(iv) + SPLIT + base64Encoder.encodeToString(encryptData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用 AES-GCM 模式解密数据
     *
     * @param key  AES 加密 Key
     * @param data 需要解密的数据
     * @return 解密后的数据
     */
    public static String decryptData(String key, String data) {
        // 转换为 AES-256 加密密钥
        byte[] aesKey = getAESKeyFromSecret(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
        // 获取数据中携带的iv
        String[] splitData = data.split(SPLIT);
        Assert.notEmpty(splitData, "data 数据不合法");
        Base64.Decoder base64Decoder = Base64.getDecoder();
        byte[] iv = base64Decoder.decode(splitData[0]);
        // 获取加密数据
        byte[] encryptData = base64Decoder.decode(splitData[1]);
        // 初始化GCM参数规格
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        // 创建并初始化加密器
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 解密
        try {
            byte[] decryptData = cipher.doFinal(encryptData);
            // 转为字符串返回
            return new String(decryptData);
        } catch (Exception e) {
            log.error("AES解密失败 errorMsg:{}", e.getMessage());
            return null;
        }
    }

}
