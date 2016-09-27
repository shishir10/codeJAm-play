import sbt.Keys._

name := """codeJam"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
    jdbc,
    cache,
    "org.mindrot" % "jbcrypt" % "0.3m",
    "com.typesafe.play" %% "play-mailer" % "3.0.1",
    filters
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1211.jre7"

resolvers ++= Seq(
    "Apache" at "http://repo1.maven.org/maven2/",
    "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/",
    "Sonatype OSS Snasphots" at "http://oss.sonatype.org/content/repositories/snapshots"
)
routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(play.PlayJava, PlayEbean)

fork in run := true
