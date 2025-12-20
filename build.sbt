import sbt.Keys.scalaVersion

// Supported versions
val scala3 = "3.7.3"

ThisBuild / description := "Generic WebServices library currently only with Play WS impl./backend"

ThisBuild / organization := "io.cequence"
ThisBuild / scalaVersion := scala3
ThisBuild / version := "0.7.4"
ThisBuild / isSnapshot := false

// POM settings for Sonatype
ThisBuild / homepage := Some(
  url("https://github.com/cequence-io/ws-client")
)

ThisBuild / sonatypeProfileName := "io.cequence"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/cequence-io/ws-client"),
    "scm:git@github.com:cequence-io/ws-client.git"
  )
)

ThisBuild / developers := List(
  Developer(
    "bburdiliak",
    "Boris Burdiliak",
    "boris.burdiliak@cequence.io",
    url("https://cequence.io")
  ),
  Developer(
    "bnd",
    "Peter Banda",
    "peter.banda@protonmail.com",
    url("https://peterbanda.net")
  )
)

ThisBuild / licenses += "MIT" -> url("https://opensource.org/licenses/MIT")
ThisBuild / publishMavenStyle := true
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / publishTo := sonatypePublishToBundle.value

inThisBuild(
  List(
    scalacOptions += "-Ywarn-unused",
    //    scalaVersion := "2.12.15",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

// JSON
lazy val playJsonVersion = "3.0.6"

// Pekko
lazy val pekkoStreamLibs = Seq("org.apache.pekko" %% "pekko-stream" % "1.4.0")

val loggingLibs = Def.setting {
  Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    "ch.qos.logback" % "logback-classic" % "1.5.18" // requires JDK11, in order to use JDK8 switch to 1.3.5
  )
}

val pekkoHttpVersion = "1.3.0"

// Play WS

def typesafePlayWS(version: String) = Seq(
  "com.typesafe.play" %% "play-ahc-ws-standalone" % version exclude ("com.typesafe.play", "play-json"),
  "com.typesafe.play" %% "play-ws-standalone-json" % version exclude ("com.typesafe.play", "play-json")
)

def orgPlayWS(version: String) = Seq(
  "org.playframework" %% "play-ahc-ws-standalone" % version,
  "org.playframework" %% "play-ws-standalone-json" % version
)

lazy val playWsDependencies = orgPlayWS("3.0.9")

lazy val `ws-client-core` =
  (project in file("ws-client-core")).settings(
    name := "ws-client-core",
    libraryDependencies += "org.playframework" %% "play-json" % playJsonVersion,
    libraryDependencies ++= pekkoStreamLibs,
    libraryDependencies ++= loggingLibs.value,
    publish / skip := false
  )

lazy val `json-repair` =
  (project in file("json-repair")).settings(
    name := "json-repair",
    libraryDependencies += "org.playframework" %% "play-json" % playJsonVersion,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.16",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % Test,
    libraryDependencies ++= loggingLibs.value,
    publish / skip := false
  )

lazy val `ws-client-play` =
  (project in file("ws-client-play"))
    .settings(
      name := "ws-client-play",
      libraryDependencies ++= playWsDependencies,
      publish / skip := false
    )
    .dependsOn(`ws-client-core`)
    .aggregate(`ws-client-core`, `json-repair`)

lazy val `ws-client-play-stream` =
  (project in file("ws-client-play-stream"))
    .settings(
      name := "ws-client-play-stream",
      libraryDependencies += "org.apache.pekko" %% "pekko-http" % pekkoHttpVersion, // JSON WS Streaming
      publish / skip := false
    )
    .dependsOn(`ws-client-core`, `ws-client-play`)
    .aggregate(`ws-client-core`, `ws-client-play`)
