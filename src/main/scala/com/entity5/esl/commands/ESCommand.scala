package com.entity5.esl.commands

case class ESCommand(name: String, data: Map[String, String], body:String = "")
