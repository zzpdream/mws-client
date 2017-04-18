package com.ydd.conference.netty.exmaple.other;

import java.io.IOException;

/**
 * Created by hehelt on 16/2/23.
 */
public class ClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        NettyClientOld client = new NettyClientOld("127.0.0.1", 8080);

    }
}
