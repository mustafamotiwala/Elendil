package com.entity5.esl

import java.io.{StringWriter, StringReader}
import java.util

import com.entity5.esl.commands.ESCommand
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.socket.SocketChannel
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.{MessageToMessageDecoder, ByteToMessageDecoder, LineBasedFrameDecoder}
import io.netty.util.CharsetUtil
import org.slf4j.LoggerFactory

/**
 * Created by mabdullah on 1/27/15.
 */
object Server {
  def main(args: Array[String]) {
    val serverGroup = new NioEventLoopGroup()
    val workerGroup = new NioEventLoopGroup()
    val SO_BACKLOG = ChannelOption.SO_BACKLOG

    val bootStrap = new ServerBootstrap()
    bootStrap.group(serverGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel])
      .childHandler(
        new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel) ={
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024))
            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8))
            ch.pipeline().addLast(new EventSocketMessageDecoder())
            ch.pipeline().addLast(new EslHandler())
          }
        }
      )
      .option(SO_BACKLOG.asInstanceOf[ChannelOption[Any]], 128)
      .childOption(ChannelOption.SO_KEEPALIVE.asInstanceOf[ChannelOption[Any]], true)
    bootStrap.bind("127.0.0.1", 8021).sync()
  }
}


class EventSocketMessageDecoder extends MessageToMessageDecoder[String] {
  private val log = LoggerFactory.getLogger(this.getClass)
  var contentLength = 0
  val bufferedMessage = new StringBuilder()

  /**
   * A client should do the framing of the socket by reading headers until 2 LFs are encountered. All the bytes up to
   * that point will be a list of `name: value` pairs, one line each. (Any multi-line header data is URL encoded so it
   * still appears as 1 line on the socket) If a Content-Length header is encountered you then read exactly that many
   * bytes from the socket. Note since this is TCP this may take more than one read so if you are supposed to read 200
   * bytes and the next read only returns 50 you must continue to read another 150 and so on until you have read 200
   * bytes or the socket has an error. Once you have read all the bytes in the content length the next packet will start
   * on the subsequent byte.
   * NOTE:
   * Content-Length is the length of the event beginning AFTER the double LF line ("\n\n") of the event header!
   * Psuedo-Code:
   * Look for \n\n in your receive buffer
   *
   * Examine data for existence of Content-Length
   *
   * If NOT present, process event and remove from receive buffer
   *
   * IF present, Shift buffer to remove `header`
   * Evaluate content-length value
   *
   * Loop until receive buffer size is >= Content-length
   * Extract content-length bytes from buffer and process
   * @param ctx
   * @param in
   * @param out
   */
  override def decode(ctx: ChannelHandlerContext, in: String, out: util.List[AnyRef]): Unit = {
    bufferedMessage append in
    if(in startsWith "Content-Length"){

    }

//    log.info("Remote Says: {}", in)
  }

}

class EslHandler extends ChannelInboundHandlerAdapter {
  private val log = LoggerFactory.getLogger(this.getClass)

  override def channelActive(ctx: ChannelHandlerContext) = {
    super.channelActive(ctx)
    ctx.writeAndFlush(Unpooled.copiedBuffer("connect\n\n", CharsetUtil.UTF_8))
    ctx.writeAndFlush(Unpooled.copiedBuffer("myevents\n\n", CharsetUtil.UTF_8))
  }

//  override def channelRead(ctx: ChannelHandlerContext, msg: scala.Any): Unit = {
//    val command = msg.asInstanceOf[ESCommand]
//    channelRead(ctx, command)
//  }

  def channelRead(ctx: ChannelHandlerContext, command: ESCommand) {
      log.info("Received Command: ", command.name)
//      val byteBuffer = msg.asInstanceOf[ByteBuf]
//      val available = byteBuffer.readableBytes()
//      val buffer = new Array[Byte](available)
//      byteBuffer.readBytes(buffer)
//      println("Event Data:")
//      print(new String(buffer, "utf-8"))
  }


}