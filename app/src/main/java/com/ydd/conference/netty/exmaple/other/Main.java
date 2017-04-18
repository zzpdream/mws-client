package com.ydd.conference.netty.exmaple.other;

import java.io.IOException;

/**
 * Dozer @ 5/24/15
 * mail@dozer.cc
 * http://www.dozer.cc
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        TcpServer server = new TcpServer(9998);

        System.out.println("==========Start Server First==========");
        server.init();
        NettyClientOld client = new NettyClientOld("127.0.0.1", 9998);
        //NettyClientOld nettyClient = new NettyClientOld("127.0.0.1", 8080);
        //client.init();
//        Thread.sleep(2000);
//
//        client.close();
//        server.close();
//        Thread.sleep(2000);
//
//        System.out.println("==========Start Client First==========");
//        client.init();
//        server.init();
//        Thread.sleep(2000);
//
//        client.close();
//        server.close();
//        Thread.sleep(2000);
//
//        System.out.println("==========Client Auto Reconnect==========");
//        server.init();
//        client.init();
//        Thread.sleep(2000);
//
//        server.close();
//        Thread.sleep(2000);
//
//        server.init();
//        Thread.sleep(2000);
//
//        client.close();
//        server.close();
//
//        System.in.read();
    }


//    @Override
//    public void encode(IoSession session, Object message, ProtocolEncoderOutput output)
//            throws Exception {
//
//        logger.info("encode data:" + message.toString());
//        IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
//        int magicWord = 0x116699ee;
//        buf.putInt(magicWord);
//        buf.putInt(message.toString().getBytes().length);
//        buf.put(message.toString().getBytes());
//        buf.flip();
//        output.write(buf);
//    }
//
//
//    while (in.hasRemaining()) {
//        byte b = in.get();
//        buf.put(b);
//    }
//    buf.flip();
//
//    byte[] magicWords = new byte[4];
//    byte[] lens = new byte[4];
//    buf.get(magicWords, 0, 4);
//
//    int magicWord = StringHelper.bytes2int(magicWords);
//
//    buf.mark();
//    buf.get(lens, 0, 4);
//    buf.mark();
//    int length = StringHelper.bytes2int(lens);
//    byte[] bytes = new byte[length];
//    buf.get(bytes);
//
//    String message = new String(bytes, charset);
//    buf.clear();
//    output.write(message);
}
