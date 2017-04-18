/*
 * Copyright 2014 cn.mindstack. All rights reserved.
 * cn.mindstack PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @LogUtil.java - 2014年4月14日
 */

package com.ydd.conference.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * LogUtil
 *
 * @author ranfi
 */
public class LogUtil {

    private static boolean IS_ERR = true;

    private static boolean IS_DEBUG = true;

    private static boolean IS_WARN = true;

    private static boolean IS_INFO = true;

    private static boolean IS_VERBOSE = true;

    public static void init(boolean isPrintable) {
        IS_DEBUG = IS_DEBUG || isPrintable;
        IS_WARN = IS_WARN || isPrintable;
        IS_INFO = IS_INFO || isPrintable;
        IS_VERBOSE = IS_VERBOSE || isPrintable;
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, cleanStr(msg));
        }
    }

    public static void d(Class<?> className, String msg) {
        if (IS_DEBUG) {
            Log.d(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (IS_INFO) {
            Log.i(tag, cleanStr(msg));
        }
    }

    public static void i(Class<?> className, String msg) {
        if (IS_INFO) {
            Log.i(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (IS_WARN) {
            Log.w(tag, cleanStr(msg));
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (IS_WARN) {
            Log.w(tag, cleanStr(msg), tr);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_ERR) {
            Log.e(tag, cleanStr(msg));
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (IS_ERR) {
            Log.e(tag, cleanStr(msg), tr);
        }
    }

    public static void w(Class<?> className, String msg) {
        if (IS_WARN) {
            Log.w(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void e(Class<?> className, String msg) {
        if (IS_ERR) {
            Log.e(className.getSimpleName(), cleanStr(msg));
        }
    }

    public static void e(Class<?> className, String msg, Throwable tr) {
        if (IS_ERR) {
            Log.e(className.getSimpleName(), cleanStr(msg), tr);
        }
    }

    public static void v(String logTag, String msg) {
        if (IS_VERBOSE) {
            Log.v(logTag, msg);
        }
    }

    private static String cleanStr(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return "";
        }
        if (msg.length() > 3000) {
            return msg.substring(0, 3000);
        }
        return msg;
    }

}