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
public class QRCodeReader {

    private static final String TAG = "QRCodeReader";
    //private ExecutorService exec;

    private static class SingletonHolder {
        private static final QRCodeReader mInstance = new QRCodeReader();
    }

    private ConcurrentHashMap<String, MyReadQrOrBarCodeTask> submittedTasks;

    public static QRCodeReader getInstance() {

        return SingletonHolder.mInstance;
    }

    private QRCodeReader() {
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
            if(!task.isFinished())
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

            Log.d("qr_code","-----------------thread start");
            final StringBuilder sbPreviousStringRead = new StringBuilder();
            final byte[] bytesBuffer = new byte[256];
            is = serialPort.getInputStream();
            int available = -1;
            boolean interrupted = false;
            while (!taskFinished) {

//                int maxWaitCount = 10;
//                try {
//                    int sumRead = 0;
//                    while (maxWaitCount -- > 0) {
//                        int size = is.available();
//                        if( size > 0 ) {
////                    sumRead += size;
////                    int len = mFileInputStream.read(recvBuff,0,size);
//                            int len = is.read(bytesBuffer,sumRead,size);
//                            sumRead += size;
//                        }
//                        if(sumRead < 12) {
//                            SystemClock.sleep(100);
//                        }
//                    }
//                    if (sumRead <= 0)
//                        continue;
//                    byte[] bytesRead = new byte[sumRead];
//                    System.arraycopy(bytesBuffer, 0, bytesRead, 0, sumRead);
//                    //return bytesRead;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                if(1==1)
//                    return;
                //waiting for data
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
                    while(readCount-- > 0) {
                        if (is.available() > 0) {
                            Log.d(TAG,"-----------------available:"+is.available());

                            lengthRead = is.read(bytesBuffer);
                            Log.d(TAG, "counting read times in one scanning operation,data length is " + lengthRead);

                            for (int i = 0; i < lengthRead; i++) {
                                char c = (char) bytesBuffer[i];
                                //Log.d(TAG, "c-----------:" + c+"---");
                                if (c == '\n' || c == '\r' || c == ' ')
                                    continue;

                                sb.append(c);
                            }
                        }else{
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    cancel();
                    break;
                }

                if(sb.length() > 1) {
                    //EventBus.getDefault().post(new CodeReadEventImpl(sb.toString()));
                    sb.setLength(0);
                }
                available = 0;
            }
            taskFinished = true;
        }





    }



}


    /*
    *


                int length = sb.length();
                int index = sb.indexOf("$");

                int countRestore = 0;

                if (index >= 0) {
                    //scanning from tail to head,
                    //hint: ignore the bar code when got qr code.
                    int lastIndex = sb.lastIndexOf("$");

                    if(lastIndex == 0) {
                        Log.d(TAG, "at the end of code" + sb.toString());

                        sbPreviousStringRead.append(sb);

                        continue;
                    }

                    int previousIndex = sb.lastIndexOf("$", lastIndex-1);

                    while (previousIndex >= 0 ) {
                        String qrcode = sb.substring(previousIndex + 1, lastIndex);
//                        char[] value = new char[(lastIndex-1) - (previousIndex+1) + 1];
//                        sb.getChars(previousIndex + 1, lastIndex - 1, value, 0);
//                        String qrcode = new String(value);
                        //todo
                        if (qrcode.length() > 10) {
                            Log.d("QR_code========post QrCodeReadEvent:", qrcode);

                            EventBus.getDefault().post(new QRCodeReadEvent(qrcode));
                        }else{

                            //reset
                            if(countRestore++ ==3){
                                sbPreviousStringRead.setLength(0);
                            }

                            Log.d("QR_code========ignore too short qr_code : ", qrcode+"\n");
                        }

                        lastIndex = previousIndex > 0 ? sb.lastIndexOf("$", previousIndex - 1 ) : 0;
                        if (lastIndex > 0) {
                            previousIndex = sb.lastIndexOf("$", lastIndex - 1);
                        }else{
                            previousIndex = -1;
                        }
                    }
                    sbPreviousStringRead.setLength(0);
                    //read at most two qr code in 200 millis

                } else {

                    //13 is the length of a valid bar code
                    if (length >= 13) {
                        int count = length / 13;

                        int cc = 0;
                        while (cc < count) {

                            String   ss = sb.substring(cc*13, (cc + 1) * 13);
                            if (OADMActivity.isValidBarCode(ss)) {
                                Log.d("QR_code========post BarCodeReadEvent:", ss);

                                EventBus.getDefault().post(new BarCodeReadEvent(ss));
                            } else {
                                Log.d("QR_codec========ignore code:", ss);
                            }
                            cc++;
                        }

                    } else{
                        sbPreviousStringRead.append(sb);
                    }
                }
            }

            taskFinished = true;
        }
    * */




