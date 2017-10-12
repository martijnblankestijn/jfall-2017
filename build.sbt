organization := "Ordina Codestar"
name := "jfall-2017-akka-persistence-demo-app"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.3"


val akkaVersion = "2.5.6"
val akkaHttpVersion = "10.0.10"


libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion, // needed for logging filter akka.event.slf4j.Slf4jLoggingFilter
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion, // query-side of cqrs

    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

    "org.iq80.leveldb" % "leveldb" % "0.9",
    "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",

    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,

    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
    "com.google.code.findbugs" % "jsr305" % "2.0.3" // only to prevent error when compiling like [error] Class javax.annotation.CheckReturnValue not found - continuing with a stub.
  )

  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-Ywarn-dead-code",
    "-Ywarn-infer-any",
    "-Xfatal-warnings"
  )

