package com.kangyonggan.gitlab.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.net.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 封装各种格式的编码解码工具类
 *
 * @author kyg
 */
public final class Encodes {

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private Encodes() {

    }

    /**
     * 默认编码
     */
    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    /**
     * 字符集
     */
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * Hex编码
     *
     * @param input 输入字节
     * @return 返回编码后的字符串
     */
    public static String encodeHex(byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
     * Hex解码
     *
     * @param input 输入字符串
     * @return 返回解码后的字节
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 解析八进制字符串
     *
     * @param str
     * @return
     */
    public static String decodeOct(String str) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '\\') {
                int sum = 0;
                int base = 64;
                for (int j = i + 1; j < i + 4; j++) {
                    sum += base * (str.charAt(j) - '0');
                    base /= 8;
                }
                if (sum >= 128) {
                    sum = sum - 256;
                }
                out.write(sum);
                i += 3;
            } else {
                out.write(ch);
            }
        }
        return new String(out.toByteArray());
    }

    /**
     * Base64编码
     *
     * @param input 输入字节
     * @return 返回编码后的字符串
     */
    public static String encodeBase64(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
     *
     * @param input 输入字节
     * @return 返回编码后的字符串
     */
    public static String encodeUrlSafeBase64(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input);
    }

    /**
     * Base62编码
     *
     * @param input 输入字节
     * @return 返回编码后的字符串
     */
    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[(input[i] & 0xFF) % BASE62.length];
        }
        return new String(chars);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     *
     * @param part url内容
     * @return 返回编码后的url
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     *
     * @param part 编码后的url
     * @return 返回解码后的url
     */
    public static String urlDecode(String part) {

        try {
            return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
