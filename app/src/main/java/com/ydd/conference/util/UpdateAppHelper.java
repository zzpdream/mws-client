package com.ydd.conference.util;

import com.ydd.conference.config.AppApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by hehelt on 16/1/18.
 */
public class UpdateAppHelper {

    private static String url = "";
    private static String path = "/sdcard/mws.apk";
    private static Callback.Cancelable cancelable;
    private static boolean isUpdate = false;

    private static int reInstallCount = 0;

    public static void update(String downloadUrl) {
        if (!isUpdate)
            download(downloadUrl, path);
    }

    public static void download(String url, final String path) {
        isUpdate = true;
        final RequestParams params = new RequestParams(url);
        //params.setAutoResume(true);
        params.setAutoRename(true);
        params.setSaveFilePath(path);
        params.setExecutor(AppApplication.executor);
        params.setCancelFast(true);

        x.http().get(params, new DownloadCallback() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                System.out.println("download:" + current * 100f / total);
                isUpdate = true;
            }

            @Override
            public void onSuccess(final File result) {

                AppApplication.executor.submit(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("------download success------");
                        result.renameTo(new File(path));
                        System.out.println("------rename file success------");
//                        reInstallCount = 0;
                        AppApplication.executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(40 * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                RebootUtil.reboot();
                            }
                        });
                        InstallHelper.installApk(path);
                        System.out.println("------install apk success------");
                        isUpdate = false;
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("download error");
                isUpdate = false;
            }
        });
    }

    public static void checkInstall() {
        reInstallCount++;
        if (reInstallCount > 3) {
            RebootUtil.reboot();
        }

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkInstall();
        InstallHelper.installApk(path);
    }

}
