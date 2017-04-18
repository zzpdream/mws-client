package com.ydd.conference.netty;

import com.ydd.conference.entity.Message;
import com.ydd.conference.util.JsonMapper;

import java.io.UnsupportedEncodingException;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by hehelt on 16/2/26.
 * <p/>
 * 解码器
 */
public class MessageDecoder extends ByteToMessageDecoder {

//    public ByteBuf byteBuf;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.capacity() >= DataConfig.LENGTH_INDEX) {
            int magicWord = in.readInt();
            if (magicWord == DataConfig.MAGIC_WORD) {
                int length = in.readInt();
                byte[] msg = new byte[length];
                in.readBytes(msg);
                String message = new String(msg, "utf-8");
                out.add(message);
//                in.clear();
            } else {
                out.add(Message.MAGIC_WORD_ERROR);
            }
        }
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
