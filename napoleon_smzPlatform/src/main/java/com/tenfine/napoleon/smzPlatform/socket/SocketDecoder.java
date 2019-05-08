package com.tenfine.napoleon.smzPlatform.socket;

import java.util.List;

import com.tenfine.napoleon.framework.util.ByteUtil;
import com.tenfine.napoleon.smzPlatform.message.ReceiveMessageBo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 参考ProtobufVarint32FrameDecoder 和 ProtobufDecoder
 */

public class SocketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {



        while (in.readableBytes() > 32) { // 如果可读长度小于包头长度，退出。
            in.markReaderIndex();

            //1.获取头部
            byte[] header = new byte[1];
            in.readBytes(header);
            
            //2.内容长度
            byte[] length = new byte[4];
            in.readBytes(length);
            int contentLength = ByteUtil.byteTo4Int(length);
            
            if(header[0] != 0x01 ){
            	in.resetReaderIndex();
                return;
            }
            
            //3|4分包
            byte[] fblength = new byte[4];
            in.readBytes(fblength);

            byte[] fblength2 = new byte[4];
            in.readBytes(fblength2);
            
            //5版本
            byte[] version = new byte[1];
            in.readBytes(version);
            
            //6命令
            byte[] cmdb = new byte[2];
            in.readBytes(cmdb);
            int cmd = ByteUtil.byteTo2Int(cmdb);
            
            //7.session
            byte[] sessionb = new byte[16];
            in.readBytes(sessionb);
            String session = new String(sessionb);
            
            if(in.readableBytes() < contentLength){
            	in.resetReaderIndex();
            	return;
            }
            
           //8内容
            byte[] contentb = new byte[contentLength];
            if(contentLength >0)
            	in.readBytes(contentb);
            
            //9状态
            byte[] flag = new byte[1];
            in.readBytes(flag);
            		
            
            //10结束
            byte[] end = new byte[1];
            in.readBytes(end);
            
            out.add(new ReceiveMessageBo(contentb, session, cmd, flag[0]));
        }
    }

}