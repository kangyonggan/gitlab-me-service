package com.kangyonggan.gitlab.util;

import org.apache.commons.lang3.StringUtils;

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
        List<String> result = exec(command);
        return result.isEmpty() ? StringUtils.EMPTY : result.get(0);
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
