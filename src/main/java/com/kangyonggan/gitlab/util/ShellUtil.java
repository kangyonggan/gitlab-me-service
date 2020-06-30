package com.kangyonggan.gitlab.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = null;
        byte[] buff = new byte[2048];
        int len;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command}, null, null);
            input = process.getInputStream();
            process.waitFor();
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
        return new String(out.toByteArray());
    }

    /**
     * 执行shell命令
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static List<String> exec(String command) throws Exception {
        List<String> result = new ArrayList<>();
        LineNumberReader reader = null;
        InputStreamReader input = null;

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command}, null, null);
            input = new InputStreamReader(process.getInputStream());
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
