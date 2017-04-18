package com.ydd.conference.netty;

import android.content.Context;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by hehelt on 16/2/26.
 */
public class ConferenceClientInitializer extends ChannelInitializer<SocketChannel> {

    private Context mContext;

    private final static int TIME_HEART_BEAT = 5;
    public ConferenceClient.ReConnectHandler reConnectHandler;
    public ConferenceClientHandler conferenceClientHandler;

    public ConferenceClientInitializer(ConferenceClient.ReConnectHandler handler) {
        reConnectHandler = handler;
    }

    public ConferenceClientInitializer(ConferenceClient.ReConnectHandler handler, ConferenceClientHandler conferenceClientHandler) {
        reConnectHandler = handler;
        this.conferenceClientHandler = conferenceClientHandler;
    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("reconnect", reConnectHandler);
        pipeline.addLast("idleStateHandler", new IdleStateHandler(TIME_HEART_BEAT, TIME_HEART_BEAT, TIME_HEART_BEAT));
        pipeline.addLast(new MessageEncoder());
        pipeline.addFirst("framedecoder", new MyDecoder(Integer.MAX_VALUE, 4, 4, 0, 0));
//        ch.pipeline().addLast(new StringDecoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(conferenceClientHandler);
    }

}
