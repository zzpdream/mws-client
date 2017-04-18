package com.ydd.conference.scanner;

import android.os.SystemClock;

import com.iznet.xixi.serialport.XxSerialPort;
import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Constant;
import com.ydd.conference.event.ScannerResultEvent;
import com.ydd.conference.util.ByteUtil;
import com.ydd.conference.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.greenrobot.event.EventBus;

/**
 * Created by hehelt on 16/3/11.
 */
public class Scanner {

    private static final String TAG = "scanner";
    private static final String RESULT_BEGIN = "7F0910000400";
    private static final String RESULT_END = "C4";
    private static final String RESULT_TWO_SPECIAL = "7F7F";
    private static final String RESULT_ONE_SPECIAL = "7F";
    private static final String COMMAND_READ_CARD = "7F021012";
    private static final String RESULT_NO_CARD = "7F0310FFEC";


    private static InputStream mInputStream;
    private static OutputStream mOutputStream;
    private static XxSerialPort mSerialPort;

    private static ReadThread readThread;
    private static SendThread sendThread;

//    public Scanner getInstance() {
//        if (scanner == null) {
//            synchronized (scanner) {
//                if (scanner == null) {
//                    scanner = new Scanner();
//                }
//            }
//        }
//        if (mSerialPort == null) {
//            mSerialPort = new XxSerialPort(new File("/dev/ttymxc3"), 9600, 0);
//            mOutputStream = mSerialPort.getOutputStream();
//            mInputStream = mSerialPort.getInputStream();
//            readThread = new ReadThread(mInputStream);
//            sendThread = new SendThread(mOutputStream);
//            AppApplication.executor.submit(readThread);
//            AppApplication.executor.submit(sendThread);
//        }
//        return scanner;
//    }

    public Scanner() {
        try {
            if (mSerialPort == null) {
                synchronized (Scanner.class) {
                    if (mSerialPort == null) {
                        mSerialPort = new XxSerialPort(new File("/dev/ttymxc3"), 9600, 0);
                        mOutputStream = mSerialPort.getOutputStream();
                        mInputStream = mSerialPort.getInputStream();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        cancel();
        readThread = new ReadThread(mInputStream);
        sendThread = new SendThread(mOutputStream);
        AppApplication.executor.submit(readThread);
        AppApplication.executor.submit(sendThread);
    }

    /**
     * 关闭串口
     */

    public void cancel() {
        if (readThread != null) {
            readThread.cancel();
        }
        if (sendThread != null) {
            sendThread.cancel();
        }
//        if (mSerialPort != null) {
//            mSerialPort.closeSerialPort();
//            mSerialPort = null;
//        }
    }


    public static class ReadThread extends Thread {

        private ScannerResultEvent scannerEvent = new ScannerResultEvent();
        private InputStream mInputStream;
        private boolean isCancel = false;

        public ReadThread(InputStream inputStream) {
            mInputStream = inputStream;
        }


        @Override
        public void run() {
            int available = 0;
            while (!isCancel) {
                try {
                    available = mInputStream.available();
                    if (available <= 0) {
                        SystemClock.sleep(50);
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (isCancel)
                    break;

                //当有数据可读时再休眠200millis后再读取
                SystemClock.sleep(160);
                String cardNumber = "";
                String[] cardNumberArray;
                try {
                    if (mInputStream.available() > 0) {
                        byte[] bytesBuffer = new byte[mInputStream.available()];
                        mInputStream.read(bytesBuffer);
                        String result = ByteUtil.Bytes2HexString(bytesBuffer);
                        if (result.equals(RESULT_NO_CARD)) {
                            LogUtil.d(TAG, "no card");
                            scannerEvent.status = Constant.STATUS_CARD_NONE;
                        } else if (result.startsWith(RESULT_BEGIN)) {
                            LogUtil.d(TAG, "card:" + result);
                            cardNumberArray = result.split(RESULT_BEGIN);
                            if (cardNumberArray.length > 1) {
                                cardNumber = cardNumberArray[1];
                                cardNumber = cardNumber.replace(RESULT_TWO_SPECIAL, RESULT_ONE_SPECIAL);
                                if (cardNumber.length() > 7) {
                                    cardNumber = cardNumber.substring(0, 8);
                                    scannerEvent.status = Constant.STATUS_CARD_SUCCESS;
                                } else {
                                    scannerEvent.status = -1;
                                }
                            } else {
                                scannerEvent.status = -1;
                            }

                        } else {
                            cardNumber = "获取卡片信息失败,请重试";
                            scannerEvent.status = -1;
                        }
                        scannerEvent.cardNumber = cardNumber;
                        EventBus.getDefault().post(scannerEvent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void cancel() {
            isCancel = true;
        }
    }

    public static class SendThread extends Thread {

        private OutputStream mOutputStream;

        private boolean isCancel = false;

        public SendThread(OutputStream outputStream) {
            mOutputStream = outputStream;
        }

        @Override
        public void run() {
            while (!isCancel) {
                write(ByteUtil.HexString2Bytes(COMMAND_READ_CARD));
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 发送指令到串口
         *
         * @param mBuffer
         * @return
         */
        private void write(byte[] mBuffer) {
            try {
                if (mOutputStream != null) {
                    mOutputStream.write(mBuffer);
                } else {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            return;
        }

        public void cancel() {
            isCancel = true;
        }
    }
}







