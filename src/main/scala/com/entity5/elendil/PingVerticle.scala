package com.entity5.elendil

import org.vertx.scala.core.eventbus.Message
import org.vertx.scala.platform.Verticle

import scala.concurrent.Promise

/**
 * Created by mabdullah on 5/17/15.
 */
class PingVerticle extends Verticle {


  override def start():Unit ={
    val log = container.logger

    vertx.eventBus.registerHandler("ping-address",{ message: Message[String] =>
      message.reply("pong!")
      log info s"Pong message sent... ${message.body()}"
    })
    log info "Scala Pong server started..."

    vertx.setTimer(10,{ t =>
      vertx.eventBus.publish("ping-address", "Ping!")
    })
  }
}
