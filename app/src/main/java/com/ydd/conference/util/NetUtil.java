package com.ydd.conference.util;

import android.util.Log;

import com.ydd.conference.config.AppApplication;

import java.io.DataOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hehelt on 16/3/24.
 */
public class NetUtil {

    public final static String TAG = "NetUtil";

    public static String IF_CONFIG = "";

    private static final String strEnter = "\n";
    private static final String cmd_su = "su";
    private static final String cmd_exit = "exit";


    /**
     * 初始化设置的IP，如果IP合法则通过静态方式设定，否则DHCP
     *
     * @param ip
     */
    public static void initIp(String ip) {
        try {
            if (!isIP(ip)) {
//                setDhcpIp();    //注销掉
            } else {
                setStaticIp(ip);
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化网络异常", e);
        }
    }

    /**
     * 在线程里面设置静态IP
     *
     * @param ip
     */
    public static void setIPWithThread(final String ip) {
        if (!isIP(ip)) {
            return;
        }
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                setStaticIp(ip);
            }
        });
    }

    /**
     * 暂停网络接口
     */
    public static void stopNetInterface() {
        IF_CONFIG = "ifconfig eth0 down";
        try {
            Process localProcess = Runtime.getRuntime().exec(cmd_su);
            DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            localDataOutputStream.writeBytes(IF_CONFIG + strEnter);
            localDataOutputStream.writeBytes(cmd_exit + strEnter);
            localDataOutputStream.flush();
            localDataOutputStream.close();
            localProcess.waitFor();
            localProcess.destroy();
        } catch (Exception e) {
            Log.e(TAG, "设置静态IP异常", e);
        }
    }

    /**
     * 设置固定IP
     *
     * @param staticIp
     */
    public static void setStaticIp(String staticIp) {
        IF_CONFIG = "ifconfig eth0 " + staticIp + " up";
        try {
            Process localProcess = Runtime.getRuntime().exec(cmd_su);
            DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            localDataOutputStream.writeBytes(IF_CONFIG + strEnter);
            localDataOutputStream.writeBytes(cmd_exit + strEnter);
            localDataOutputStream.flush();
            localDataOutputStream.close();
            localProcess.waitFor();
            localProcess.destroy();
        } catch (Exception e) {
            Log.e(TAG, "设置静态IP异常", e);
        }
    }


    /**
     * 通过DHCP方式获取IP
     */
    public static void setDhcpIp() {
        IF_CONFIG = "netcfg eth0 up dhcp";
        try {
            Process localProcess = Runtime.getRuntime().exec(cmd_su);
            DataOutputStream localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
            localDataOutputStream.writeBytes(IF_CONFIG + strEnter);
            localDataOutputStream.writeBytes(cmd_exit + strEnter);
            localDataOutputStream.flush();
            localDataOutputStream.close();
            localProcess.waitFor();
            localProcess.destroy();
        } catch (Exception e) {
            Log.e(TAG, "获取DHCP地址异常", e);
        }
    }

    /**
     * 判断IP是否合法
     *
     * @param addr
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr) || "0.0.0.0".equals(addr)) {
            return false;
        }
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();

        return ipAddress;
    }

}
