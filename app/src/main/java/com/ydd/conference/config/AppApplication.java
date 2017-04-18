package com.ydd.conference.config;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ydd.conference.R;
import com.ydd.conference.ui.MainActivity;

import org.xutils.x;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hehelt on 16/2/22.
 */
public class AppApplication extends Application {

    public static Context mContext;
    public static ExecutorService executor = Executors.newCachedThreadPool();
//    public static int emptyImgRes = R.mipmap.zanweitu;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        x.Ext.init(this);
        initCrashRestart();
        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    public class UnCeHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler mDefaultHandler;
        public static final String TAG = "AppContext";
        AppApplication application;

        public UnCeHandler(AppApplication application) {
            //获取系统默认的UncaughtException处理器
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            this.application = application;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            if (!handleException(ex) && mDefaultHandler != null) {
                //如果用户没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "error : ", e);
                }
                Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                PendingIntent restartIntent = PendingIntent.getActivity(
                        application.getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                //退出程序
                AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                        restartIntent); // 1秒钟后重启应用
                application.finishActivity();
            }
        }

        /**
         * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
         *
         * @param ex
         * @return true:如果处理了该异常信息;否则返回false.
         */
        private boolean handleException(Throwable ex) {
            if (ex == null) {
                return false;
            }
            //使用Toast来显示异常信息
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(application.getApplicationContext(), "马上回来",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();
            return true;
        }
    }


    //初始化崩溃重启
    public void initCrashRestart() {
        UnCeHandler catchExcep = new UnCeHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        //exitApp();
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}