package com.kangyonggan.gitlab.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件上传工具类
 *
 * @author kyg
 */
public final class FileUpload {

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private FileUpload() {
    }

    /**
     * 上传文件
     *
     * @param dir      文件父目录
     * @param fileName 文件名
     * @param file     文件
     * @throws FileUploadException 可能会抛出的异常
     */
    public static void upload(String dir, String fileName, MultipartFile file) throws FileUploadException {
        if (file.getSize() != 0) {
            try {
                File fileDir = new File(dir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                File desc = getAbsolutePath(dir + fileName + "." + FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase());
                file.transferTo(desc);
            } catch (Exception e) {
                throw new FileUploadException("File Upload Exception", e);
            }
        }
    }

    /**
     * 写文件
     *
     * @param dir      文件父目录
     * @param fileName 文件名
     * @param content  内容
     * @throws Exception 可能会抛出的异常
     */
    public static void writeFile(String dir, String fileName, String content) throws Exception {
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dir + fileName))) {
            writer.write(content);
            writer.flush();
        }
    }

    /**
     * 获取文件的字节数组
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] getBytes(String file) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
            out.flush();
        }
        return out.toByteArray();
    }

    private static File getAbsolutePath(String filename) throws IOException {
        File desc = new File(filename);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }
}