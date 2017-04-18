package com.ydd.conference.netty;

import android.util.Log;

import com.ydd.conference.config.Command;
import com.ydd.conference.entity.Message;
import com.ydd.conference.util.JsonMapper;
import com.ydd.conference.util.SharedPreferencesUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by hehelt on 16/4/15.
 */
public class ReceiveMessage {

    private static final String TAG = "ReceiveMessage";

    public static void receive(ChannelHandlerContext ctx, Object msg) {
        Log.d(TAG, "read data-------" + msg.toString());
        String message = (String) msg;
        if (!"error".equals(msg.toString())) {
            Message messageEntity = JsonMapper.fromJson(message, Message.class);
            String cmd = messageEntity.getCmd();
            Command command = Command.getCommand(cmd);
            if (command == null) {
                Message response = new Message();
                response.setCmd(command.value);
                response.setType(Message.TYPE_MESSAGE_RESPONSE);
                response.setStatus(Message.STATUS_COMMAND_ERROR);
                ctx.writeAndFlush(response);
                return;
            }
            if (command.status != Command.HEARTBEAT.status) {
                SharedPreferencesUtil.setStatus(command.status);
            }

            Message responseMessage = new Message();
            responseMessage.setCmd(command.value);
            responseMessage.setType(Message.TYPE_MESSAGE_RESPONSE);
            responseMessage.setStatus(Message.STATUS_SUCCESS);
        }
    }


    public static void before(ChannelHandlerContext ctx, String msg) {

        if (Message.MAGIC_WORD_ERROR.equals(msg)) {
            Message response = new Message();
            response.setType(Message.TYPE_MESSAGE_RESPONSE);
            response.setStatus(Message.STATUS_MAGIC_WORD_ERROR);
            ctx.writeAndFlush(response);
            return;
        }

        Message messageEntity = JsonMapper.fromJson(msg, Message.class);
        String cmd = messageEntity.getCmd();
        Command command = Command.getCommand(cmd);

        if (command == null) {
            Message response = new Message();
            response.setCmd(command.value);
            response.setType(Message.TYPE_MESSAGE_RESPONSE);
            response.setStatus(Message.STATUS_COMMAND_ERROR);
            ctx.writeAndFlush(response);
            return;
        }

        if (command.status != Command.HEARTBEAT.status) {
            SharedPreferencesUtil.setStatus(command.status);
        }

        Message responseMessage = new Message();
        responseMessage.setCmd(command.value);
        responseMessage.setType(Message.TYPE_MESSAGE_RESPONSE);
        responseMessage.setStatus(Message.STATUS_SUCCESS);
    }
}
