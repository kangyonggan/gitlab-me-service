package com.kangyonggan.gitlab.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kyg
 */
public final class ShellUtil {

    private ShellUtil() {
    }

    /**
     * 执行shell命令
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static String execSimple(String command) throws Exception {
        return new String(execByte(command), StandardCharsets.UTF_8);
    }

    /**
     * 执行shell命令
     *
     * @param command
     * @param args
     * @return
     * @throws Exception
     */
    public static String execSimple(String command, String... args) throws Exception {
        return new String(execByte(command + buildArgs(args)), StandardCharsets.UTF_8);
    }

    /**
     * 构建参数
     *
     * @param args
     * @return
     */
    private static String buildArgs(String... args) {
        if (args == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(" '").append(args[i]).append("' ");
        }
        return sb.toString();
    }

    /**
     * 执行shell命令
     *
     * @param command
     * @param args
     * @return
     * @throws Exception
     */
    public static byte[] execByte(String command, String... args) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = null;
        byte[] buff = new byte[2048];
        int len;
        try {
            input = execStream(command + buildArgs(args));
            while ((len = input.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return out.toByteArray();
    }

    /**
     * 执行shell命令
     *
     * @param command
     * @param args
     * @return
     * @throws Exception
     */
    public static InputStream execStream(String command, String... args) throws Exception {
        Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command + buildArgs(args)}, null, null);
        return process.getInputStream();
    }

    /**
     * 执行shell命令
     *
     * @param command
     * @param args
     * @return
     * @throws Exception
     */
    public static List<String> exec(String command, String... args) throws Exception {
        List<String> result = new ArrayList<>();
        LineNumberReader reader = null;
        InputStreamReader input = null;

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command + buildArgs(args)}, null, null);
            input = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
            reader = new LineNumberReader(input);
            String line;
            process.waitFor();
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (input != null) {
                input.close();
            }
        }
        return result;
    }

}
