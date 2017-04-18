package com.ydd.conference.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ydd.conference.config.AppApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hehelt on 16/3/9.
 */
public class InstallHelper {

    private final static String TAG = "InstallHelper";

    public static final String HELPER_APP_PACKAGE_NAME = "com.iznet.helper";
    public static final String HELPER_APP_MAIN_ACTIVITY = "com.iznet.helper.MainActivity";
    public static final String HELPER_APP_APK_PATH = "/sdcard/helper.apk";
    public static final String HELPER_APP_APK_NAME = "helper.apk";


    public static void init() {
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "开始升级APK");
                copyFilesAssets(AppApplication.mContext, HELPER_APP_APK_NAME, HELPER_APP_APK_PATH);
                if (isNeedInstallHelper()) {
                    installApk(HELPER_APP_APK_PATH);
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    startHelperApp(AppApplication.mContext);
                } else {
                    Log.e(TAG, "当前APK软件已是最新版本");
                }
            }
        });
    }

    public static void installApk(final String filePath) {
        Process process;
        try {
            // 请求root权限
            process = Runtime.getRuntime().exec("su");
            DataOutputStream localDataOutputStream = new DataOutputStream(process.getOutputStream());
            localDataOutputStream.writeBytes("pm install -r " + filePath + "\n");
            localDataOutputStream.flush();

            InputStream inputStream = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder result = new StringBuilder();
            String temp = bufferedReader.readLine();
            while (temp != null) {
                result.append(temp);
                temp = bufferedReader.readLine();
            }
            Log.e(TAG, "安装结果:" + result.toString());

            localDataOutputStream.close();
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            Log.e(TAG, "静默安装apk[" + filePath + "]出现异常", e);
        }

    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesAssets(Context context, String oldPath, String newPath) {
        try {
            System.out.println("copy start");
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
            System.out.println("copy end");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("copy error");

        }
    }


    public static void startHelperApp(Context context) {
        ComponentName componentName = new ComponentName(
                //这个是另外一个应用程序的包名
                HELPER_APP_PACKAGE_NAME,
                //这个参数是要启动的Activity
                HELPER_APP_MAIN_ACTIVITY);
        try {
            Intent intent = new Intent();
            intent.setComponent(componentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            System.out.print("启动");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败");
            System.out.println(e.getMessage());

        }
    }

    public static boolean isNeedInstallHelper() {
        int currentVersion = getVersion(HELPER_APP_PACKAGE_NAME, AppApplication.mContext);
        int apkVersion = getApkVersionCode(HELPER_APP_APK_PATH, AppApplication.mContext);
        if (currentVersion < apkVersion) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersion(String packageName, Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取APK版本号(versionCode)
     *
     * @param apkPath
     * @return
     */
    public static int getApkVersionCode(String apkPath, Context context) {

        int versionCode = -1;
        File file = new File(apkPath);
        if (file.exists()) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                versionCode = pi.versionCode;
            }
        }
        return versionCode;
    }
}
