package com.iznet.xixi.serialport;

import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口通讯的核心类，负责桥接java和c/c++的通讯,一切由这里开始...
 * <p/>
 * Created by ranfi on 12/13/14.
 */
public class XxSerialPort {

    private static final String TAG = "XxSerialPort1111";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private volatile boolean closed = true;
    private String devicePath = null;

    public XxSerialPort() {

    }

    public boolean isClosed() {
        return closed;
    }

    public XxSerialPort(File device, int baudRate, int flags) throws IOException {

		/* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec("/system/xbin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        if (device.exists() && device.canRead() && device.canWrite()) {

            closed = false;

            devicePath = device.getAbsolutePath();
            mFd = open(devicePath, baudRate, flags);

            if (mFd == null) {
                Log.e(TAG, "native open returns null");
                throw new IOException();
            }
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);

        }else{

            //         /dev/ttymxc0 是字符设备，不是文件 !!! ？？
            throw new IOException("Do not have access to file " + device.getPath() + "!!!"
                    + ",is file?" + device.isFile() + ", exists?" + device.exists() +", can read?"
                    + device.canRead() + ",can write?" + device.canWrite());
        }
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor  open(String path, int baudrate, int flags);

    private native void close();

    //buffer
    private byte[] recvBuff = new byte[128];
    private static final int sleepTime = 100;

    /**
     * 阻塞直到读到数据
     *
     * @param timeout 超时
     * @param
     * @return data received or null when timeout(1000 millis)
     */
    public byte[] read(int timeout) {

        if (mFileInputStream == null) {
            return null;
        }
        int maxWaitCount = timeout / sleepTime + 1;
        try {
            int sumRead = 0;
            while (maxWaitCount -- > 0) {
                int size = mFileInputStream.available();
                if( size > 0 ) {
//                    sumRead += size;
//                    int len = mFileInputStream.read(recvBuff,0,size);
                    int len = mFileInputStream.read(recvBuff,sumRead,size);
                    sumRead += size;
                }
                if(sumRead < 12) {
                    SystemClock.sleep(100);
                }
            }
            if (sumRead <= 0)
                return null;
            byte[] bytesRead = new byte[sumRead];
            System.arraycopy(recvBuff, 0, bytesRead, 0, sumRead);
            Log.i(TAG, "return data received from serial port" + devicePath
                    + " of size :" + String.valueOf(sumRead) + ": "
                    + new String(recvBuff, 0, sumRead));
            return bytesRead;
        }catch (Exception e){
            e.printStackTrace();
            closeSerialPort();
        }
        return null;
    }


    public void write(byte[] b) throws IOException {

        if (mFileOutputStream == null) {
            return;
        }
        mFileOutputStream.write(b);
    }

    public void closeSerialPort() {
        if (!closed) {
            closed = true;
            this.close();
        }
    }

    static {
        //load serial port jni
        try {
            System.loadLibrary("XxSerialPort");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
