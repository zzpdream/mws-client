package com.ydd.conference.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by hehelt on 16/4/1.
 */
public class NetHelper {

    private static String TAG = NetHelper.class.getSimpleName();
    private Context mContext = null;

    public NetHelper(Context contect) {
        mContext = contect;
    }

    public void useDexClassLoader() {
        SharedPreferences sp = mContext.getSharedPreferences("ethernet",
                Context.MODE_WORLD_WRITEABLE);
        Intent intent = new Intent();
        intent.setPackage("com.fsl.ethernet");
        PackageManager pm = mContext.getPackageManager();
        final List<ResolveInfo> plugins = pm.queryIntentActivities(intent, 0);
        if (plugins.size() <= 0) {
            Log.i(TAG, "resolve info size is:" + plugins.size());
            return;
        }
        ResolveInfo resolveInfo = plugins.get(0);
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        String div = System.getProperty("path.separator");
        String packageName = activityInfo.packageName;
        String dexPath = activityInfo.applicationInfo.sourceDir;
        //目标类所在的apk或者jar的路径，class loader会通过这个路径来加载目标类文件
        String dexOutputDir = mContext.getApplicationInfo().dataDir;
        //由于dex文件是包含在apk或者jar文件中的,所以在加载class之前就需要先将dex文件解压出来，dexOutputDir为解压路径
        String libPath = activityInfo.applicationInfo.nativeLibraryDir;
        //目标类可能使用的c或者c++的库文件的存放路径

        Log.i(TAG, "div:" + div + "   " +
                "packageName:" + packageName + "   " +
                "dexPath:" + dexPath + "   " +
                "dexOutputDir:" + dexOutputDir + "   " +
                "libPath:" + libPath);

        DexClassLoader dcLoader = new DexClassLoader(dexPath, dexOutputDir, libPath, this.getClass().getClassLoader());
        try {
            Class<?> clazz = dcLoader.loadClass(packageName + ".EthernetEnabler");
            Constructor c0 = null;
            c0 = clazz.getDeclaredConstructor(Context.class);
            c0.setAccessible(true);
            Object obj = c0.newInstance(mContext);
//            Object obj = clazz.newInstance();


//            Class[] param = new Class[1];
//            param[0] = String.class;
//            Method action = clazz.getMethod("invoke", param);
//            action.invoke(obj, "test this function");
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "ClassNotFoundException");
        } catch (InstantiationException e) {
            Log.i(TAG, "InstantiationException");
        } catch (IllegalAccessException e) {
            Log.i(TAG, "IllegalAccessException");
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "IllegalArgumentException");
        } catch (InvocationTargetException e) {
            handleException(e);
            Log.i(TAG, "InvocationTargetException");
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "NoSuchMethodException");
        }
    }
//    java.lang.SecurityException: NetworkManagementService: Neither user 10055 nor current process has android.permission.CONNECTIVITY_INTERNAL.
    private static void handleException(Exception e)
    {
        String msg = null;
        if (e instanceof InvocationTargetException)
        {
            Throwable targetEx = ((InvocationTargetException) e)
                    .getTargetException();
            if (targetEx != null)
            {
                msg = targetEx.getMessage();
            }
        } else
        {
            msg = e.getMessage();
        }
        System.out.println(msg);
        e.printStackTrace();
    }

}
