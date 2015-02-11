package com.entity5.esl

import java.io.{StringWriter, StringReader}

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.{Unpooled, ByteBuf}
import io.netty.channel.socket.SocketChannel
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.CharsetUtil

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
            ch.pipeline().addLast(new ESLHandler())
          }
        }
      )
//      .option(SO_BACKLOG, 128)
//      .childOption(ChannelOption.SO_KEEPALIVE, true)
    bootStrap.bind("127.0.0.1", 8021).sync()
  }
}


class ESLHandler extends ChannelInboundHandlerAdapter{
  override def channelRead(ctx: ChannelHandlerContext, msg: scala.Any) = {
    val byteBuffer = msg.asInstanceOf[ByteBuf]
    val available = byteBuffer.readableBytes()
    val buffer = new Array[Byte](available)
    byteBuffer.readBytes(buffer)
    println("Event Data:")
    print(new String(buffer, "utf-8"))
  }

  override def channelActive(ctx: ChannelHandlerContext) = {
    super.channelActive(ctx)
    ctx.writeAndFlush(Unpooled.copiedBuffer("connect\n\n", CharsetUtil.UTF_8))
    ctx.writeAndFlush(Unpooled.copiedBuffer("myevents\n\n", CharsetUtil.UTF_8))
  }
}