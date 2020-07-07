package com.kangyonggan.gitlab.util;

import com.kangyonggan.gitlab.constants.FileType;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author kyg
 */
public class FileUtil {

    /**
     * 二进制转化为16进制
     *
     * @param bytes
     * @return
     */
    private static String bytes2hex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                hex.append("0");
            }
            hex.append(temp.toLowerCase());
        }
        return hex.toString();
    }

    /**
     * 读取文件头
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    private static String getFileHeader(String filePath) throws IOException {
        byte[] b = new byte[28];
        try (InputStream in = new FileInputStream(filePath)) {
            in.read(b, 0, 28);
        }

        return bytes2hex(b);
    }

    /**
     * 读取文件头
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static String getFileHeader(InputStream in) throws IOException {
        byte[] b = new byte[28];
        in.read(b, 0, 28);

        return bytes2hex(b);
    }

    /**
     * 判断文件类型
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static FileType getType(byte[] bytes) {
        byte[] b = new byte[28];
        for (int i = 0; i < 28 && i < bytes.length; i++) {
            b[i] = bytes[i];
        }
        return getFileType(bytes2hex(b));
    }

    /**
     * 判断文件类型
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static FileType getType(String filePath) throws IOException {
        return getFileType(getFileHeader(filePath));
    }

    /**
     * 判断文件类型
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static FileType getType(InputStream in) throws IOException {
        return getFileType(getFileHeader(in));
    }

    private static FileType getFileType(String fileHeader) {
        String fileHead = fileHeader;
        if (StringUtils.isEmpty(fileHead)) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }
        return null;
    }

}
