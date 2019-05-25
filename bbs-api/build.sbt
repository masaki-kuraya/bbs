name := """bbs-api"""
organization := "name.kuraya.masaki.bbs.api"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += javaJdbc
libraryDependencies += javaWs
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.2"
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.14"
libraryDependencies += "com.github.t3hnar" %% "scala-bcrypt" % "3.1"

PlayKeys.devSettings += "play.server.http.port" -> "8080"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "name.kuraya.masaki.bbs.api.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "name.kuraya.masaki.bbs.api.binders._"
