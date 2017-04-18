package com.ydd.conference.util;

import org.xutils.common.Callback;

import java.io.File;

/**
 * Created by hehelt on 16/1/18.
 */


public class DownloadCallback implements
        Callback.CommonCallback<File>,
        Callback.ProgressCallback<File>,
        Callback.Cancelable {


    @Override
    public void cancel() {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }

    @Override
    public void onSuccess(File result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}

