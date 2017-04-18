package com.ydd.conference.netty.exmaple.other;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * Dozer @ 5/24/15
 * mail@dozer.cc
 * http://www.dozer.cc
 */
public final class TcpServer {
    private volatile EventLoopGroup bossGroup;

    private volatile EventLoopGroup workerGroup;

    private volatile ServerBootstrap bootstrap;

    private volatile boolean closed = false;

    private final int localPort;

    public TcpServer(int localPort) {
        this.localPort = localPort;
    }

    public void close() {
        closed = true;

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        System.out.println("Stopped Tcp Server: " + localPort);
    }

    public void init() {
        closed = false;

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //todo: add more handler
                ch.pipeline().addLast(new TimeServerHandler());
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                int magicWord = 0x116699ee;
                ByteBuf firstMessage;
                firstMessage = Unpooled.buffer();
                firstMessage.writeInt(magicWord);
                ctx.writeAndFlush(firstMessage);

            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                int magicWord = 0x116699ee;
                ByteBuf firstMessage;
                firstMessage = Unpooled.buffer();
                firstMessage.writeInt(magicWord);
                ctx.writeAndFlush(firstMessage);

            }

        });

        doBind();
    }

    protected void doBind() {
        if (closed) {
            return;
        }

        bootstrap.bind(localPort).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    System.out.println("Started Tcp Server: " + localPort);
                } else {
                    System.out.println("Started Tcp Server Failed: " + localPort);

//                    f.channel().eventLoop().schedule(() -> doBind(), 1, TimeUnit.SECONDS);
                    f.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doBind();
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        });
    }


    public class TimeServerHandler extends ChannelInboundHandlerAdapter {


        //ChannelHandlerContext通道处理上下文
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws InterruptedException { // (1)

            while (true) {
                byte[] data = "{\"cmd\":\"heartbeat\",\"params\":{\"seatId\":\"1\"}}".getBytes();
                int magicWord = 0x116699ee;
                int len = data.length;
                ByteBuf firstMessage;
                firstMessage = Unpooled.buffer();
                firstMessage.writeInt(magicWord);
                firstMessage.writeInt(len);
                firstMessage.writeBytes(data);
                ctx.writeAndFlush(firstMessage);
                Thread.sleep(2000);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("dddddddd");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            super.channelReadComplete(ctx);
        }


        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            super.read(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
