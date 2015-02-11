import sbt._

import Keys._

import spray.revolver.RevolverPlugin._

Revolver.settings

name := "PhoneX-Command and Control Console"

organization := "com.entity5.esl"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.4"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:postfixOps")

resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "io.netty"                      % "netty-all"                 % "4.0.25.Final",
  "ch.qos.logback"                %  "logback-classic"          % "1.1.2",
  "com.typesafe.akka"             %% "akka-actor"               % "2.3.9",
  "com.typesafe.akka"             %% "akka-testkit"             % "2.3.9",
  "com.googlecode.libphonenumber" %  "libphonenumber"           % "7.0.1",
  "org.specs2"                    %% "specs2"                   % "2.4.2"         % "test"
)

// fork in run := true
//javaOptions in Revolver.reStart += "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=127.0.0.1:5757"
Revolver.enableDebugging(port = 5757, suspend = false)
