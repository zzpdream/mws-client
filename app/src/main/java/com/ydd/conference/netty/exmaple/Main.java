package com.ydd.conference.netty.exmaple;

import com.ydd.conference.netty.ConferenceClient;
import com.ydd.conference.util.ExtractFileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Dozer @ 5/24/15
 * mail@dozer.cc
 * http://www.dozer.cc
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ExtractFileUtil util = new ExtractFileUtil("/Users/hehelt/study/photo.zip", "/Users/hehelt/study/mws/", new ExtractFileUtil.PublicProgress() {
            @Override
            public void updateUi(int progress) {
                if(progress>=100){
                    File file = new File("/Users/hehelt/study/mws/");
                    String[] list =file.list();
                    int length = list.length;
                }
            }
        });
        util.extract();
//        TcpServer server = new TcpServer(5678);
//        ConferenceClient client = new ConferenceClient("127.0.0.1", 5678);
//        client.init();
        //ConferenceClient client = ConferenceClient.getInstance();
//        server.init();

//        System.out.println("==========Start Server First==========");
        //client.init();
        //Thread.sleep(2000);

//
//
//        Thread.sleep(10000);
//        server.close();
//        Thread.sleep(5000);
//        server.init();


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

//        client.close();
//        server.close();

//        System.in.read();
    }
}
