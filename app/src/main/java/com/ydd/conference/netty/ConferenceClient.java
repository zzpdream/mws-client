package com.ydd.conference.netty;

import android.app.Activity;
import android.content.Context;

import com.ydd.conference.config.Constant;
import com.ydd.conference.event.ConnectEvent;
import com.ydd.conference.util.NetUtil;
import com.ydd.conference.util.SharedPreferencesUtil;

import java.net.InetSocketAddress;
import java.util.IllegalFormatException;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by hehelt on 16/2/26.
 */
public class ConferenceClient {


    private static ConferenceClient instance;

    private volatile EventLoopGroup workerGroup;
    private volatile Bootstrap bootstrap;
    private volatile boolean closed = false;
    private String remoteHost;
    private int remotePort;

    private Activity mContext;

    public static ConferenceClient getInstance() {
        if (instance == null) {
            synchronized (ConferenceClient.class) {
                if (instance == null) {
//                    instance = new ConferenceClient("192.168.1.106", 5678);
                    instance = new ConferenceClient(SharedPreferencesUtil.getServerIp(), SharedPreferencesUtil.getServerPort());
//                    instance = new ConferenceClient("0.tcp.ngrok.io", 13901);
                }
            }
        }
        return instance;
    }

    public ConferenceClient(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public void init() {

        closed = false;
        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);

        ReConnectHandler reConnectHandler = new ReConnectHandler();
        ConferenceClientHandler conferenceClientHandler = new ConferenceClientHandler(mContext);
        ConferenceClientInitializer channelInitializer = new ConferenceClientInitializer(reConnectHandler, conferenceClientHandler);

        bootstrap.handler(channelInitializer);
        doConnect();
    }

    private void doConnect() {
        if (closed) {
            return;
        }
        ChannelFuture future;
        future = bootstrap.connect(new InetSocketAddress(remoteHost, remotePort));
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                if (!f.isSuccess()) {
                    if (Constant.isConnected) {
                        Constant.isConnected = false;
                        EventBus.getDefault().post(new ConnectEvent(false));
                    }
                    //重连的时候刷新网络,主要解决插拔网线重连,此方法测试不稳定
//                    NetUtil.initIp(SharedPreferencesUtil.getLocalIp());

                    f.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 1, TimeUnit.SECONDS);
                    System.out.println("client fail");
                } else {
                    Constant.isConnected = true;
                    EventBus.getDefault().post(new ConnectEvent(true));
                    System.out.println("client success");
                }
            }
        });

    }


    public void setContext(Activity context) {
        mContext = context;
    }


    public void close() {
        closed = true;
        workerGroup.shutdownGracefully();
    }

    public void restart() {
        this.remoteHost = SharedPreferencesUtil.getServerIp();
        this.remotePort = SharedPreferencesUtil.getServerPort();
        close();
        init();
    }

    @ChannelHandler.Sharable
    public class ReConnectHandler extends ChannelHandlerAdapter {
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            System.out.println("inactive");
            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    doConnect();
                }
            }, 1, TimeUnit.SECONDS);
        }
    }


}
