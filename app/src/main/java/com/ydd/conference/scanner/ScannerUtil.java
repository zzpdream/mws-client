package com.ydd.conference.scanner;

import android.os.SystemClock;
import android.util.Log;

import com.iznet.xixi.serialport.XxSerialPort;
import com.ydd.conference.config.AppApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by wwm-iznet on 15-1-6.
 */
public class ScannerUtil {

    private static final String TAG = "QRCodeReader";

    private static class SingletonHolder {
        private static final ScannerUtil mInstance = new ScannerUtil();
    }

    private ConcurrentHashMap<String, MyReadQrOrBarCodeTask> submittedTasks;

    public static ScannerUtil getInstance() {

        return SingletonHolder.mInstance;
    }

    private ScannerUtil() {
        //exec = Executors.newCachedThreadPool();
        submittedTasks = new ConcurrentHashMap<String, MyReadQrOrBarCodeTask>();
    }


    public void openScanningDevice(String path) {
        File f = new File(path);

        MyReadQrOrBarCodeTask task = null;
        try {
            task = new MyReadQrOrBarCodeTask(path);
        } catch (IOException e) {
            e.printStackTrace();

            task = null;
        }

        if (task == null)
            return;

        MyReadQrOrBarCodeTask previousTask = submittedTasks.putIfAbsent(f.getAbsolutePath(), task);

        if (previousTask == null || previousTask.isFinished()) {
            //override the previous task
            submittedTasks.put(f.getAbsolutePath(), task);

            AppApplication.executor.submit(task);
            Log.d(TAG, "Submit task successfully, file path = " + path);
        } else {
            Log.e(TAG, "No need to open the device again, file path = " + path);
        }
    }

    public void tearDown() {

        Set<String> keys = submittedTasks.keySet();

        for (String key : keys) {
            MyReadQrOrBarCodeTask task = submittedTasks.get(key);
            if (!task.isFinished())
                task.cancel();
        }

        //exec.shutdown();
        submittedTasks.clear();
    }

    public class MyReadQrOrBarCodeTask implements Runnable {

        private XxSerialPort serialPort = null;
        private boolean taskFinished = false;
        private InputStream is = null;

        public MyReadQrOrBarCodeTask(String path) throws IOException {
            serialPort = new XxSerialPort(new File(path), 9600, 0);
        }

        public boolean isFinished() {
            return taskFinished;
        }

        public void cancel() {
            if (serialPort != null) {
                serialPort.closeSerialPort();
                serialPort = null;
                taskFinished = true;
            }
        }

        @Override
        public void run() {

            Log.d("qr_code", "-----------------thread start");
            final byte[] bytesBuffer = new byte[256];
            is = serialPort.getInputStream();
            int available = -1;
            boolean interrupted = false;
            while (!taskFinished) {
                while (available <= 0) {
                    SystemClock.sleep(50);
                    try {
                        available = is.available();
                    } catch (IOException e) {
                        e.printStackTrace();
                        interrupted = true;
                        cancel();
                        break;
                    }
                }
                if (interrupted)
                    break;

                //当有数据可读时再休眠200millis后再读取
                SystemClock.sleep(160);

                //==========================
                StringBuilder sb = new StringBuilder();
                try {
                    int lengthRead = 0;
                    int readCount = 2;
                    //防止一次扫描多次返回
                    while (readCount-- > 0) {
                        if (is.available() > 0) {
                            Log.d(TAG, "-----------------available:" + is.available());

                            lengthRead = is.read(bytesBuffer);
                            Log.d(TAG, "counting read times in one scanning operation,data length is " + lengthRead);

                            for (int i = 0; i < lengthRead; i++) {
                                char c = (char) bytesBuffer[i];
                                //Log.d(TAG, "c-----------:" + c+"---");
                                if (c == '\n' || c == '\r' || c == ' ')
                                    continue;

                                sb.append(c);
                            }
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    cancel();
                    break;
                }

                if (sb.length() > 1) {
                    //EventBus.getDefault().post(new CodeReadEventImpl(sb.toString()));
                    sb.setLength(0);
                }
                available = 0;
            }
            taskFinished = true;
        }
    }
}


