package com.ydd.conference.netty;

import com.ydd.conference.entity.Message;
import com.ydd.conference.util.JsonMapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by hehelt on 16/2/26.
 * <p/>
 * 编码器
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    private String charset="utf-8";
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        out.writeInt(DataConfig.MAGIC_WORD);
        String msg = JsonMapper.toJson(message);
        out.writeInt(msg.getBytes(charset).length);
        out.writeBytes(msg.getBytes(charset));
    }
}
