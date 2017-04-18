package com.ydd.conference.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Constant;
import com.ydd.conference.ui.MainActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by hehelt on 16/2/22.
 */
public class SystemUtil {

    private final static String TAG = "SystemUtil";

    /**
     * 静默安装软件
     *
     * @param filePath
     */
    public static void installApk(final String filePath) {
        Process process = null;
        OutputStream out = null;
        try {
            // 请求root
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            // 调用安装
            out.write(("pm install -r " + filePath + "\n").getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                out.flush();
                out.close();
                System.out.println("安装成功！");
                //startApk();
            } catch (IOException e) {
                System.out.println("安装失败！");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 关机
     */
    public static void reboot() {
//        String cmd = "su -c reboot";
        String cmd = "su reboot -p";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            Log.d("reBoot", "" + e.toString());
        }
    }

    /**
     * 关机
     */
    public static void configNet() {
//        String cmd = "su -c reboot";
        String cmd = "su ifup eth0";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            Log.d("reBoot", "" + e.toString());
        }
    }

    /**
     * 获取系统版本code
     *
     * @return
     */
    public static int getVersion() {
        try {
            PackageManager manager = AppApplication.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(AppApplication.mContext.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取系统版本code
     *
     * @return
     */
    public static String getVersionName() {
        try {
            PackageManager manager = AppApplication.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(AppApplication.mContext.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "获取不到,请检查网络";
    }


    public static String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                        System.out.println(ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;

    }


    public static void showSystemBar() {
        Constant.barIsShow = true;
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                Process su = null;
                try {
                    su = Runtime.getRuntime().exec("su");
                    String cmd = "am startservice -n com.android.systemui/.SystemUIService\n";
                    su.getOutputStream().write(cmd.getBytes());
                    String exit = "exit\n";
                    su.getOutputStream().write(exit.getBytes());
                    su.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (su != null) {
                        su.destroy();
                    }
                }
            }
        });
    }

    /**
     * @Description: 影藏导航栏
     * @date: May 14, 2014
     */
    public static void hideSystemBar() {

        Constant.barIsShow = false;
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                Process su = null;
                try {
                    su = Runtime.getRuntime().exec("su");
                    String cmd = "service call activity 42 s16 com.android.systemui\n";
                    su.getOutputStream().write(cmd.getBytes());
                    String exit = "exit\n";
                    su.getOutputStream().write(exit.getBytes());
                    su.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (su != null) {
                        su.destroy();
                    }
                }
            }
        });
    }

    public static String getIpAddress() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) AppApplication.mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static void start(final Context context) {
//        SystemUtil.showSystemBar();
//        optionBarText.setText(R.string.hide_bar);
        ComponentName componentName = new ComponentName(
                //这个是另外一个应用程序的包名
                "com.fsl.ethernet",
                //这个参数是要启动的Activity
                "com.fsl.ethernet.MainActivity");
        try {
            Intent intent = new Intent();
            intent.setComponent(componentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Toast.makeText(context, "启动成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "启动IP设置APK失败", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
        SharedPreferencesUtil.setIsNetConfig(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    Log.e("dd", "error : ", e);
//                }
//                SharedPreferences share = context.getSharedPreferences("first", Context.MODE_PRIVATE);
//                share.edit().putString();
//                MainActivity.actionStart(context);
                Intent intent = new Intent(AppApplication.mContext, MainActivity.class);
                PendingIntent restartIntent = PendingIntent.getActivity(
                        AppApplication.mContext, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                //退出程序
                AlarmManager mgr = (AlarmManager) AppApplication.mContext.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                        restartIntent); // 1秒钟后重启应用
                finishActivity();
            }
        }, 2000);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public static void finishActivity() {
        //exitApp();
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
