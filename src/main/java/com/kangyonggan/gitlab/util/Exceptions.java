package com.kangyonggan.gitlab.util;


import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * @author kyg
 */
public final class Exceptions {

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private Exceptions() {

    }

    /**
     * 将CheckedException转换为UncheckedException
     *
     * @param ex 检测异常
     * @return 返回非检测异常
     */
    public static RuntimeException unchecked(Throwable ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        } else {
            return new RuntimeException(ex);
        }
    }

    /**
     * 将ErrorStack转化为String
     *
     * @param ex 异常
     * @return 返回异常信息
     */
    public static String getStackTraceAsString(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
     *
     * @param ex 异常
     * @return 返回异常信息
     */
    public static String getErrorMessageWithNestedException(Throwable ex) {
        Throwable nestedException = ex.getCause();
        return new StringBuilder().append(ex.getMessage()).append(" nested exception is ")
                .append(nestedException.getClass().getName()).append(":").append(nestedException.getMessage())
                .toString();
    }

    /**
     * 获取异常的Root Cause.
     *
     * @param ex 异常
     * @return 返回跟异常
     */
    public static Throwable getRootCause(Throwable ex) {
        Throwable cause;
        while ((cause = ex.getCause()) != null) {
            ex = cause;
        }
        return ex;
    }

    /**
     * 判断异常是否由某些底层的异常引起
     *
     * @param ex                    异常
     * @param causeExceptionClasses 异常类
     * @return 由某些底层的异常则返回true，否则返回false
     */
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }
}
