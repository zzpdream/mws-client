package com.ydd.conference.util;//package com.iznet.thailandtong.util;
//

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

/**
 * Created by LT on 2015/10/9.
 */
public class ExtractFileUtil {

    private String mZipFilePath;
    private String mResultFilePath;
    private String mPassword;
    private ProgressMonitor mProgressMonitor;
    private PublicProgress mPublicProgress;

    public ExtractFileUtil(String in, String out, PublicProgress publicProgress) {
        this(in, out, null, publicProgress);
    }

    public ExtractFileUtil(String in, String out) {
        this(in, out, null, null);
    }

    public ExtractFileUtil(String in, String out, String password, PublicProgress publicProgress) {
        mZipFilePath = in;
        mResultFilePath = out;
        mPassword = password;
        mPublicProgress = publicProgress;
    }

    public void extract() {
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(mZipFilePath);
            zipFile.setFileNameCharset("utf-8");
            mProgressMonitor = zipFile.getProgressMonitor();

            if (zipFile.isEncrypted()) {
                if (mPassword == null)
                    throw new IllegalArgumentException("没有解压密码?");
                zipFile.setPassword(mPassword);
            }
            if (mPublicProgress != null) {
                progressThread.start();
            }
            zipFile.setRunInThread(true);
            zipFile.extractAll(mResultFilePath);

        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public interface PublicProgress {
        void updateUi(int progress);
    }


    private Thread progressThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int degree = mProgressMonitor.getPercentDone();
                if (mProgressMonitor.getState() == ProgressMonitor.RESULT_SUCCESS) {
                    mPublicProgress.updateUi(100);
                    break;
                }
                mPublicProgress.updateUi(degree);
                if (degree >= 100) {
                    break;
                }

            }
        }
    });

}
