package com.kangyonggan.gitlab.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * MD5工具类
 *
 * @author kyg
 */
public final class Digests {

    /**
     * sha1
     */
    private static final String SHA1 = "SHA-1";

    /**
     * md5
     */
    private static final String MD5 = "MD5";

    /**
     * random
     */
    private static SecureRandom random = new SecureRandom();

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private Digests() {

    }

    /**
     * 对输入字节进行sha1散列.
     *
     * @param input 输入字节
     * @return 返回散列后的字节
     */
    public static byte[] sha1(byte[] input) {
        return digest(input, SHA1, null, 1);
    }

    /**
     * 对输入字节进行sha1散列，并指定盐
     *
     * @param input 输入字节
     * @param salt  盐
     * @return 返回散列后的字节
     */
    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, SHA1, salt, 1);
    }

    /**
     * 对输入字节进行sha1散列，并指定盐和迭代
     *
     * @param input      输入字节
     * @param salt       盐
     * @param iterations 迭代
     * @return 返回散列后的字节
     */
    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA1, salt, iterations);
    }

    /**
     * 对输入字节进行散列, 支持md5与sha1算法.
     *
     * @param input      输入字节
     * @param algorithm  算法
     * @param salt       盐
     * @param iterations 迭代
     * @return 返回散列后的字节
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                digest.update(salt);
            }

            byte[] result = digest.digest(input);

            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     * @return 返回随机生成的salt
     */
    public static byte[] generateSalt(int numBytes) {
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 对文件进行md5散列
     *
     * @param input 输入流
     * @return 返回md5散列
     * @throws IOException 可能会抛出的异常
     */
    public static byte[] md5(InputStream input) throws IOException {
        return digest(input, MD5);
    }

    /**
     * 对文件进行sha1散列
     *
     * @param input 输入流
     * @return 返回sha1散列
     * @throws IOException 可能会抛出的异常
     */
    public static byte[] sha1(InputStream input) throws IOException {
        return digest(input, SHA1);
    }

    /**
     * 对文件进行sha1或md5散列
     *
     * @param input     输入流
     * @param algorithm 算法
     * @return 散列
     * @throws IOException 可能会抛出的异常
     */
    private static byte[] digest(InputStream input, String algorithm) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            int bufferLength = 8 * 1024;
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);

            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }

            return messageDigest.digest();
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

}
