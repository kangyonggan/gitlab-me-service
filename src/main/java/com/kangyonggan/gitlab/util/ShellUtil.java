package com.kangyonggan.gitlab.util;

import java.io.IOException;

/**
 * @author kyg
 */
public final class ShellUtil {

    private ShellUtil() {}

    /**
     * 执行简单shell命令
     *
     * @param command
     * @throws IOException
     */
    public static void exec(String command) throws IOException {
        Runtime.getRuntime().exec(command);
    }

}
