package com.ydd.conference.netty.exmaple.other;//package com.yihaifarm.cabinet.netty;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 *
 * create by heheLT
 *
 * netty service 运行在后台
 * 应用启动时,service也启动
 *
 */
public class NettyService extends IntentService {

    public NettyService() {
        super("NettyService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context) {
        Intent intent = new Intent(context, NettyService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       // startNetty();

    }

    /**
     * 启动netty
     */
    private void startNetty() {
        try {
            NettyClientOld client = new NettyClientOld("192.168.1.112",9999
                    );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
