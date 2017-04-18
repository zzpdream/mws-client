package com.ydd.conference.netty.exmaple.other;//package com.yihaifarm.cabinet.netty;

import android.app.Activity;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by hehelt on 15/12/23.
 * <p/>
 * netty封装类  发送和接受服务器的消息  在NettyService中启动
 */
public class NettyClientOld {

    /*
     * 服务器端口号
     */
    private int port;

    /*
     * 服务器IP
     */
    private String host;

    private Activity context;
    private Bootstrap bootstrap;

    public NettyClientOld(String host, int port)
            throws InterruptedException {
        this.port = port;
        this.host = host;
        start();
    }

    /**
     * 启动netty
     *
     * @throws InterruptedException
     */
    private void start() throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {

            bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host, port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    socketChannel.pipeline().addLast(new NettyClientHandler());
                }
            });
            //doConnect();
            ChannelFuture future = bootstrap.connect(host, port).sync();
            if (future.isSuccess()) {
                SocketChannel socketChannel = (SocketChannel) future.channel();
                System.out.println("----------------connect server success----------------");
            }
            future.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    private void doConnect() {

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    System.out.println("Started Tcp Client: ");
                } else {
                    System.out.println("Started Tcp Client Failed: ");
                    //f.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
                    f.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        });
    }

    public class NettyClientHandler extends ChannelHandlerAdapter {

        private ByteBuf firstMessage;

        //连接上服务器后的回调方法
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

//            byte[] data = "服务器，给我一个APPLE".getBytes();
//
//            firstMessage = Unpooled.buffer();
//            firstMessage.writeBytes(data);
//
//            ctx.writeAndFlush(firstMessage);
            byte[] data = "{\"cmd\":\"heartbeat\",\"params\":{\"seatId\":\"1\"}}".getBytes();
            int magicWord = 0x116699ee;
            int len = data.length;
            ByteBuf firstMessage;
            firstMessage = Unpooled.buffer();
            firstMessage.writeInt(magicWord);
            firstMessage.writeInt(len);
            firstMessage.writeBytes(data);
            ctx.writeAndFlush(firstMessage);
        }

        //接受到服务器的回调方法
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {

            ByteBuf buf = (ByteBuf) msg;
            int magicWord = getMagicWord(buf);
            int dataLength = getDataLength(buf);
            String data = getDataString(buf);
            System.out.println("client:" + data);

//            byte[] data = "{\"cmd\":\"heartbeat\",\"params\":{\"seatId\":\"1\"}}".getBytes();
//            int magicWord = 0x116699ee;
//            int len = data.length;
            ByteBuf firstMessage;
            firstMessage = Unpooled.buffer();
            firstMessage.writeInt(magicWord);
            firstMessage.writeInt(dataLength);
            firstMessage.writeBytes(data.getBytes());
            ctx.writeAndFlush(firstMessage);
//            int a = buf.getInt(4);
//            final String rev = getMessage(buf);
//            System.out.println("客户端收到服务器数据:" + rev);
//
//            byte[] magicWords = new byte[4];
//            byte[] lens = new byte[4];
//            buf.getBytes()
//
//            int magicWord = StringHelper.bytes2int(magicWords);
//
//            buf.mark();
//            buf.get(lens, 0, 4);
//            buf.mark();
//            int length = StringHelper.bytes2int(lens);
//            byte[] bytes = new byte[length];
//            buf.get(bytes);
//
//            String message = new String(bytes, charset);
//            buf.clear();
//            output.write(message);

//            Message message = new Message();
//            message.messageHeader = message.new MessageHeader();
//            message.messageHeader.msgLen = 8;
//            message.messageHeader.msgType = Message.MessageType.MSG_TYPE_POSITION;
//            message.messageBody = "hello,我是客户端";
//            String response = JsonMapper.toJson(message);
//            byte[] data = "服务器".getBytes();
//            firstMessage = Unpooled.buffer();
//            firstMessage.writeBytes(data);
//            ctx.writeAndFlush(firstMessage);
        }

        private int getMagicWord(ByteBuf buf) {
            return buf.getInt(0);
        }

        private int getDataLength(ByteBuf buf) {
            return buf.getInt(4);
        }

        private String getDataString(ByteBuf buf) {
            ByteBuf byteBuf = Unpooled.buffer();
            buf.getBytes(8, byteBuf, getDataLength(buf));
            return getMessage(byteBuf);
        }


        /**
         * 解析从服务器接受的消息
         *
         * @param buf
         * @return
         */
        private String getMessage(ByteBuf buf) {

            byte[] con = new byte[buf.readableBytes()];
            buf.readBytes(con);
            try {
                return new String(con, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}





