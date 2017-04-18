package com.ydd.conference.util;

import com.ydd.conference.config.AppApplication;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hehelt on 16/3/24.
 */
public class RebootUtil {
    public static final String CMD_REBOOT = "reboot";
    public static final String CMD_SHUTDOWN = "reboot -p";

    private static final String strEnter = "\n";
    private static final String cmd_su = "su";
    private static final String cmd_exit = "exit";

    public static void reboot() {
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Process localProcess = Runtime.getRuntime().exec(cmd_su);
                    DataOutputStream localDataOutputStream = new DataOutputStream(
                            localProcess.getOutputStream());
                    localDataOutputStream.writeBytes(CMD_REBOOT + strEnter);
                    localDataOutputStream.writeBytes(cmd_exit + strEnter);
                    localDataOutputStream.flush();
                    localDataOutputStream.close();
                    localProcess.waitFor();
                    localProcess.destroy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void shutdown() {
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Process localProcess = Runtime.getRuntime().exec(cmd_su);
                    DataOutputStream localDataOutputStream = new DataOutputStream(
                            localProcess.getOutputStream());
                    localDataOutputStream.writeBytes(CMD_SHUTDOWN + strEnter);
                    localDataOutputStream.writeBytes(cmd_exit + strEnter);
                    localDataOutputStream.flush();
                    localDataOutputStream.close();
                    localProcess.waitFor();
                    localProcess.destroy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
