package com.tenfine.napoleon.smzPlatform.socket;

import com.tenfine.napoleon.framework.util.ByteUtil;
import com.tenfine.napoleon.smzPlatform.message.SendMessageBo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 参考ProtobufVarint32LengthFieldPrepender 和 ProtobufEncoder
 */
@Sharable
public class SocketEncoder extends MessageToByteEncoder<SendMessageBo> {

	public SocketEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, SendMessageBo sendMsgBo, ByteBuf out) throws Exception {

		//发送内容
		byte[]content = sendMsgBo.getConternt();
		//1添加头
		byte[] header = {0x01};
		out.writeBytes(header);
		
		//2添加长度
		byte[] length = ByteUtil.intTo4Bytes(content.length);
		out.writeBytes(length);
				
		//3分包索引
		byte[] partIndex = {0x00, 0x00, 0x00, 0x00};
		out.writeBytes(partIndex);
		
		//4分包数量
		byte[] partCount = {0x00, 0x00, 0x00, 0x00};
		out.writeBytes(partCount);
		
		//5版本
		byte[] version = ByteUtil.intTo1Bytes(sendMsgBo.getVersion());
		out.writeBytes(version);
		
		//6添加命令
		byte[] cmd = ByteUtil.intTo2Bytes(sendMsgBo.getCmd());
		out.writeBytes(cmd);

		//7.session
		byte[] session = sendMsgBo.getSession().getBytes();
		out.writeBytes(session);
		
		//8.内容
		if(content.length>0){
			out.writeBytes(content);
		}
		
		//9.状态
		byte[] status = {sendMsgBo.getFlag()};
		out.writeBytes(status);
		
		//10.结束标记
		byte[] end = {0x01};
		out.writeBytes(end);
		
		return;
	}

	
}