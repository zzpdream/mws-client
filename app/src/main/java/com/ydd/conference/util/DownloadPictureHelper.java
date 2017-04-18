package com.ydd.conference.util;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ydd.conference.config.AppApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by hehelt on 16/1/18.
 */
public class DownloadPictureHelper {

    public static final String TAG = "downloadPicture";

    private final static String PATH = "/sdcard/photo.zip";
    private final static String EXTRA_PATH = "/sdcard";
    private static boolean isUpdate = false;

    public static void update(String downloadUrl) {
        if (downloadUrl == null || downloadUrl.equals("")) {
            LogUtil.d(TAG, "地址错误");
            return;
        }
        if (isUpdate) {
            LogUtil.d(TAG, "正在下载");
            return;
        }
        download(downloadUrl, PATH);

    }

    public static void download(String url, final String path) {
        isUpdate = true;
        final RequestParams params = new RequestParams(url);
        params.setAutoRename(true);
        params.setSaveFilePath(path);
        params.setExecutor(AppApplication.executor);

        params.setCancelFast(true);

        x.http().get(params, new DownloadCallback() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.d(TAG, "loading:" + current * 100f / total);
                isUpdate = true;
            }

            @Override
            public void onSuccess(final File result) {
                LogUtil.d(TAG, "SUCCESS");
                ExtractFileUtil extractFileUtil = new ExtractFileUtil(path, EXTRA_PATH, new ExtractFileUtil.PublicProgress() {
                    @Override
                    public void updateUi(int progress) {
                        if (progress >= 100)
                            SharedPreferencesUtil.saveMd5(MD5Util.getFileMD5(result));
                    }
                });
                extractFileUtil.extract();
//                result.renameTo(new File(path));
                isUpdate = false;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.d(TAG, "ERROR");
                isUpdate = false;
            }
        });
    }


    public static Drawable getPicture() {
        String picFolder = EXTRA_PATH + "/photo/" + SharedPreferencesUtil.getMemberName();
        String path = picFolder + ".jpg";
        if (!new File(path).exists()) {
            path = picFolder + ".png";
            System.out.println("before:" + path);
            if (!new File(path).exists()){
                System.out.println("image null");
                return null;
            }
        }
        System.out.println("after");
        Drawable drawable = BitmapDrawable.createFromPath(path);
        return drawable;
    }

}
